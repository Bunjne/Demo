package whiz.tss.sspark.s_spark_android.presentation.class_detail.student_class_activity

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.viewModel.StudentClassActivityViewModel
import whiz.sspark.library.extension.isUrlValid
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_activity.post.student.StudentClassPostAdapter
import whiz.tss.sspark.s_spark_android.databinding.FragmentStudentClassActivityBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.utility.logout
import whiz.tss.sspark.s_spark_android.utility.refreshToken
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URISyntaxException

class StudentClassActivityFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String, color: Int, allMemberCount: Int) = StudentClassActivityFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putInt("color", color)
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

    private val color by lazy {
        arguments?.getInt("color", Color.BLACK) ?: Color.BLACK
    }

    private val allMemberCount by lazy {
        arguments?.getInt("allMemberCounts", 0) ?: 0
    }

    private var student: Student? = null

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
                            isLike = if (student?.userId == userId) isSocketLiked else isLike
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
                            isLike = if (student?.userId == userId) isSocketLiked else isLike
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
            socket?.off(SSparkLibrary.collaborationSocketListenerAuthenticationPath)
            socket?.on(SSparkLibrary.collaborationSocketListenerAuthenticationPath) {
                establishSocketListener()
            }
            socket?.off(SSparkLibrary.collaborationSocketListenerUnAuthenticationPath)
            socket?.on(SSparkLibrary.collaborationSocketListenerUnAuthenticationPath) {

                if (socket?.connected() == true) {
                    socket?.disconnect()
                }

                activity?.runOnUiThread {
                    refreshToken(requireActivity()) {
                        val authenticationInformation = retrieveAuthenticationInformation(requireActivity())
                        socket?.emit(SSparkLibrary.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
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

    override fun onStart() {
        super.onStart()

        activity?.let {
            socket?.run {
                if (!connected()) {

                    off(Socket.EVENT_CONNECT, onSocketAuthenticated)
                    on(Socket.EVENT_CONNECT, onSocketAuthenticated)

                    connect()

                    val authenticationInformation = retrieveAuthenticationInformation(requireActivity())
                    socket?.emit(SSparkLibrary.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
                        put("token", authenticationInformation?.accessToken)
                    })
                }
            }
        }
    }

    private fun establishSocketListener() {
        socket?.run {
            off(SSparkLibrary.collaborationSocketListenerPostLikePath, onPostLiked)
            off(SSparkLibrary.collaborationSocketListenerPostUnLikePath, onPostUnliked)
            off(SSparkLibrary.collaborationSocketListenerPostCommentPath, onCommentAdded)
            off(SSparkLibrary.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)

            on(SSparkLibrary.collaborationSocketListenerPostLikePath, onPostLiked)
            on(SSparkLibrary.collaborationSocketListenerPostUnLikePath, onPostUnliked)
            on(SSparkLibrary.collaborationSocketListenerPostCommentPath, onCommentAdded)
            on(SSparkLibrary.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.student.collect {
                it?.let {
                    student = it
                    initView()
                } ?: run {
                    logout(requireContext())
                }
            }
        }

        activity?.let {
            initView()

            viewModel.listOnlineClasses(classGroupId)
        }
    }

    override fun initView() {
        binding.vClassActivity.init(
            allMemberCount = allMemberCount,
            color = color,
            onRefresh = {
                viewModel.listOnlineClasses(classGroupId)
            },
            onPostClicked = { post, isKeyboardShown ->
                openDetail(post, isKeyboardShown)
            },
            onImageClicked = { imageView, imageUrl ->
//                showImage(activity!!, imageView, imageUrl)
            },
            onLikeClicked = { post ->
                likePost(post)
            },
            onCommentClicked = { post, isKeyboardShown ->
                openDetail(post, isKeyboardShown)
            },
            onPostRead = { postId ->
                readPost(postId as? String ?: "")
            },
            onPostLikedUsersClicked = { postId ->
//                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, postId as? String ?: "", color, PostInteraction.LIKE.type)
//                dialog.show(childFragmentManager, "")
            },
            onPostSeenUsersClicked = { postId ->
//                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, postId as? String ?: "", color, PostInteraction.SEEN.type)
//                dialog.show(childFragmentManager, "")
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
//        val dialog = PostDetailSheetDialog.newInstance(classGroupId, post, color, allMemberCount, isKeyboardShown)
//        dialog.show(childFragmentManager, "")
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

                with (binding.vClassActivity) {
                    refreshRecyclerView(items)
                    checkPostAmount()
                }
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
        data.put("userId", student?.userId)
        data.put("postId", post.id.toString())

        if (post.isLike) {
            socket?.emit(SSparkLibrary.collaborationSocketEmitterPostUnLikePath, data)
        } else {
            socket?.emit(SSparkLibrary.collaborationSocketEmitterPostLikePath, data)
        }
    }

    private fun readPost(postId: String) {
        val data = JSONObject()
        data.put("userId", student?.userId)
        data.put("postId", postId.toString())
        socket?.emit(SSparkLibrary.collaborationSocketEmitterPostSeenPath, data)
    }

    override fun observeError() {
        viewModel.postErrorResponse.observe(this) {
            it?.let {
                if (it.statusCode != 404) {
                    showApiResponseXAlert(activity, it)
                }
            }

            items.removeAll { it.post != null }

            val reader = BufferedReader(InputStreamReader(requireContext().resources.assets.open("sample.json")))
            val objects = reader.readText().toObjects(Array<Post>::class.java)

            objects.forEach {
                items.add(StudentClassPostAdapter.Item(post = it))
            }

            with (binding.vClassActivity) {
                refreshRecyclerView(items)
                checkPostAmount()
            }
        }

        viewModel.onlineClassesErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            items.removeAll { it.onlineClasses != null }

            val reader = BufferedReader(InputStreamReader(requireContext().resources.assets.open("onlineClasses.json")))
            val objects = reader.readText().toObjects(Array<PlatformOnlineClass>::class.java)

            items.add(StudentClassPostAdapter.Item(onlineClasses = objects))

            viewModel.listPosts(classGroupId)
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