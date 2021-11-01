package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_activity.student

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.data.static.SocketPath
import whiz.sspark.library.data.viewModel.StudentClassActivityViewModel
import whiz.sspark.library.extension.isUrlValid
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_activity.post.student.StudentClassPostAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassActivityBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction.LikeBySeenByDialogFragment
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.student.StudentClassPostCommentActivity
import whiz.tss.sspark.s_spark_android.utility.refreshToken
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.utility.retrieveUserID
import whiz.tss.sspark.s_spark_android.utility.showImage
import java.net.URISyntaxException

class StudentClassActivityFragment : BaseFragment() {

    companion object {
        const val LIKE_BY_DIALOG = "LIKE_BY_DIALOG"
        const val SEEN_BY_DIALOG = "SEEN_BY_DIALOG"

        fun newInstance(classGroupId: String, startColor: Int, endColor: Int, allMemberCount: Int) = StudentClassActivityFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putInt("startColor", startColor)
                putInt("endColor", endColor)
                putInt("allMemberCount", allMemberCount)
            }
        }
    }

    private val socket by lazy {
        try {
            val options = IO.Options().apply {
                transports = arrayOf(WebSocket.NAME)
            }

            IO.socket(SSparkLibrary.collaborationSocketBaseURL, options)
        } catch (exception: URISyntaxException) {
            null
        }
    }

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val startColor by lazy {
        arguments?.getInt("startColor", ContextCompat.getColor(requireContext(), R.color.primaryStartColor)) ?: ContextCompat.getColor(requireContext(), R.color.primaryStartColor)
    }

    private val endColor by lazy {
        arguments?.getInt("endColor", ContextCompat.getColor(requireContext(), R.color.primaryEndColor)) ?: ContextCompat.getColor(requireContext(), R.color.primaryEndColor)
    }

    private val allMemberCount by lazy {
        arguments?.getInt("allMemberCounts", 0) ?: 0
    }

    private val studentUserId by lazy {
        retrieveUserID(requireContext())
    }

    private val items = mutableListOf<StudentClassPostAdapter.Item>()

    private var _binding: FragmentStudentClassActivityBinding? = null
    private val binding get() = _binding!!

    private val onPostLiked by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val userId = data.getString("userId")
                    val postId = data.getString("postId")
                    val likeCounts = data.getInt("likeCounts")
                    val isSocketLiked = data.getBoolean("isLike")

                    val itemPosition = items.indexOfFirst { postId.contains(it.post?.id.toString(), true) }
                    if (itemPosition > -1) {
                        items[itemPosition].post?.run {
                            likeCount = likeCounts
                            isLike = if (studentUserId == userId) {
                                isSocketLiked
                            } else {
                                isLike
                            }
                        }

                        activity?.runOnUiThread {
                            binding.vClassActivity.notifyRecycleViewItemChanged(itemPosition)
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private val onPostUnliked by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val userId = data.getString("userId")
                    val postId = data.getString("postId")
                    val likeCounts = data.getInt("likeCounts")
                    val isSocketLiked = data.getBoolean("isLike")

                    val itemPosition = items.indexOfFirst { postId.contains(it.post?.id.toString(), true) }
                    if (itemPosition > -1) {
                        items[itemPosition].post?.run {
                            likeCount = likeCounts
                            isLike = if (studentUserId == userId) {
                                isSocketLiked
                            } else {
                                isLike
                            }
                        }

                        activity?.runOnUiThread {
                            binding.vClassActivity.notifyRecycleViewItemChanged(itemPosition)
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private val onCommentAdded by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val postId = data.getString("postId")

                    val itemPosition = items.indexOfFirst { postId.contains(it.post?.id.toString(), true) }
                    if (itemPosition > -1) {
                        items[itemPosition].post?.run {
                            commentCount += 1
                        }

                        activity?.runOnUiThread {
                            binding.vClassActivity.notifyRecycleViewItemChanged(itemPosition)
                        }
                    }
                }
            } catch (exception: Exception) {
            }
        }
    }

    private val onCommentDeleted by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val postId = data.getString("postId")

                    val itemPosition = items.indexOfFirst { postId.contains(it.post?.id.toString(), true) }
                    if (itemPosition > -1) {
                        items[itemPosition].post?.run {
                            commentCount -= 1
                        }

                        activity?.runOnUiThread {
                            binding.vClassActivity.notifyRecycleViewItemChanged(itemPosition)
                        }
                    }
                }
            } catch (exception: Exception) {
            }
        }
    }

    private val onSocketAuthenticated by lazy {
        Emitter.Listener {
            socket?.off(SocketPath.collaborationSocketListenerAuthenticationPath)
            socket?.on(SocketPath.collaborationSocketListenerAuthenticationPath) {
                establishSocketListener()
            }
            socket?.off(SocketPath.collaborationSocketListenerUnAuthenticationPath)
            socket?.on(SocketPath.collaborationSocketListenerUnAuthenticationPath) {

                if (socket?.connected() == true) {
                    socket?.disconnect()
                }

                activity?.runOnUiThread {
                    refreshToken(requireActivity()) {
                        val authenticationInformation = retrieveAuthenticationInformation(requireActivity())
                        socket?.emit(SocketPath.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
                            put("token", authenticationInformation?.accessToken)
                        })
                    }
                }
            }
        }
    }

    private val viewModel: StudentClassActivityViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStudentClassActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (items.isNotEmpty()) {
            viewModel.listPosts(classGroupId)
        }

        activity?.let {
            socket?.run {
                if (!connected()) {

                    off(Socket.EVENT_CONNECT, onSocketAuthenticated)
                    on(Socket.EVENT_CONNECT, onSocketAuthenticated)

                    connect()

                    val authenticationInformation = retrieveAuthenticationInformation(requireActivity())
                    socket?.emit(SocketPath.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
                        put("token", authenticationInformation?.accessToken)
                    })
                }
            }
        }
    }

    private fun establishSocketListener() {
        socket?.run {
            off(SocketPath.collaborationSocketListenerPostLikePath, onPostLiked)
            off(SocketPath.collaborationSocketListenerPostUnLikePath, onPostUnliked)
            off(SocketPath.collaborationSocketListenerPostCommentPath, onCommentAdded)
            off(SocketPath.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)

            on(SocketPath.collaborationSocketListenerPostLikePath, onPostLiked)
            on(SocketPath.collaborationSocketListenerPostUnLikePath, onPostUnliked)
            on(SocketPath.collaborationSocketListenerPostCommentPath, onCommentAdded)
            on(SocketPath.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()

            viewModel.listOnlineClasses(classGroupId)
        }
    }

    override fun initView() {
        binding.vClassActivity.init(
            allMemberCount = allMemberCount,
            color = startColor,
            onRefresh = {
                viewModel.listOnlineClasses(classGroupId)
            },
            onPostClicked = { post, isKeyboardShown ->
                openDetail(post, isKeyboardShown)
            },
            onImageClicked = { imageView, imageUrl ->
                showImage(requireActivity(), imageView, imageUrl)
            },
            onLikeClicked = { post ->
                likePost(post)
            },
            onCommentClicked = { post, isKeyboardShown ->
                openDetail(post, isKeyboardShown)
            },
            onPostRead = { postId ->
                readPost(postId)
            },
            onPostLikedUsersClicked = { postId ->
                val isLikeByDialogNotShown = childFragmentManager.findFragmentByTag(LIKE_BY_DIALOG) == null

                if (isLikeByDialogNotShown) {
                    val dialog = LikeBySeenByDialogFragment.newInstance(classGroupId, postId, startColor, endColor, PostInteraction.LIKE.type)
                    dialog.show(childFragmentManager, LIKE_BY_DIALOG)
                }
            },
            onPostSeenUsersClicked = { postId ->
                val isSeenByDialogNotShown = childFragmentManager.findFragmentByTag(SEEN_BY_DIALOG) == null

                if (isSeenByDialogNotShown) {
                    val dialog = LikeBySeenByDialogFragment.newInstance(classGroupId, postId, startColor, endColor, PostInteraction.SEEN.type)
                    dialog.show(childFragmentManager, SEEN_BY_DIALOG)
                }
            },
            onOnlineClassPlatformClicked = { url ->
                if (url.isUrlValid() && (url.contains("http://") || url.contains("https://"))) {
                    CustomTabsIntent.Builder().build().apply {
                        launchUrl(requireContext(), Uri.parse(url))
                    }
                }
            }
        )
    }

    private fun openDetail(post: Post, isKeyboardShown: Boolean) {
        val intent = Intent(requireContext(), StudentClassPostCommentActivity::class.java).apply {
            putExtra("classGroupId", classGroupId)
            putExtra("post", post.toJson())
            putExtra("startColor", startColor)
            putExtra("endColor", endColor)
            putExtra("allMemberCount", allMemberCount)
            putExtra("isKeyboardShown", isKeyboardShown)
        }

        startActivity(intent)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
            binding.vClassActivity.setSwipeRefreshLayout(isLoading)
        })
    }

    override fun observeData() {
        viewModel.posts.observe(this, Observer {
            it?.let { posts ->
                items.removeAll { it.post != null }

                with(items) {
                    posts.forEach {
                        add(StudentClassPostAdapter.Item(post = it))
                    }
                }

                binding.vClassActivity.refreshRecyclerView(items)
            }
        })

        viewModel.onlineClassesResponse.observe(this, Observer {
            it?.let {
                val shownOnlineClasses = it.filter { it.isShown }
                with (items) {
                    removeAll { it.onlineClasses != null }
                    add(StudentClassPostAdapter.Item(onlineClasses = shownOnlineClasses))
                }

                viewModel.listPosts(classGroupId)
            }
        })
    }

    private fun likePost(post: Post) {
        val data = JSONObject()
        data.put("userId", studentUserId)
        data.put("postId", post.id)

        if (post.isLike) {
            socket?.emit(SocketPath.collaborationSocketEmitterPostUnLikePath, data)
        } else {
            socket?.emit(SocketPath.collaborationSocketEmitterPostLikePath, data)
        }
    }

    private fun readPost(postId: String) {
        val data = JSONObject()
        data.put("userId", studentUserId)
        data.put("postId", postId)
        socket?.emit(SocketPath.collaborationSocketEmitterPostSeenPath, data)
    }

    override fun observeError() {
        viewModel.postErrorResponse.observe(this) {
            it?.let {
                if (it.statusCode != 404) {
                    showApiResponseXAlert(activity, it)
                }
            }

            items.removeAll { it.post != null }

            binding.vClassActivity.refreshRecyclerView(items)
        }

        viewModel.onlineClassesErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            items.removeAll { it.onlineClasses != null }

            viewModel.listPosts(classGroupId)
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                context?.showAlertWithOkButton(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (socket?.connected() == true) {
            socket?.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (socket?.connected() == true) {
            socket?.disconnect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}