package whiz.tss.sspark.s_spark_android.presentation.class_detail.class_activity

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.tss.sspark.s_spark_android.BuildConfig
import whiz.tss.sspark.s_spark_android.SSparkApp
import whiz.tss.sspark.s_spark_android.presentation.BaseFragment
import whiz.tss.sspark.s_spark_android.unility.retrieveAuthenticationInformation
import java.net.URISyntaxException

class ClassActivityFragment : BaseFragment() {

    companion object {
        fun newInstance(classGroupId: String, color: Int, allMemberCount: Int) = ClassActivityFragment().apply {
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

            IO.socket(SSparkApp.collaborationSocketBaseURL, options)
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

    private val items = mutableListOf<ClassPostAdapter.Item>()

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
                            vClassActivity.notifyRecycleViewItemChanged(itemPosition)
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
                            vClassActivity.notifyRecycleViewItemChanged(itemPosition)
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
                            vClassActivity.notifyRecycleViewItemChanged(itemPosition)
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
                            vClassActivity.notifyRecycleViewItemChanged(itemPosition)
                        }
                    }
                }
            } catch (exception: Exception) {
            }
        }
    }

    private val onSocketAuthenticated by lazy {
        Emitter.Listener {
            socket?.off(BuildConfig.COLLABORATION_SOCKET_LISTENER_AUTHENTICATION)
            socket?.on(BuildConfig.COLLABORATION_SOCKET_LISTENER_AUTHENTICATION) {
                establishSocketListener()
            }
            socket?.off(BuildConfig.COLLABORATION_SOCKET_LISTENER_UNAUTHENTICATION)
            socket?.on(BuildConfig.COLLABORATION_SOCKET_LISTENER_UNAUTHENTICATION) {

                if (socket?.connected() == true) {
                    socket?.disconnect()
                }

                activity?.runOnUiThread {
                    refreshToken(activity!!) {
                        val authenticationInformation = retrieveAuthenticationInformation(activity!!)
                        socket?.emit(BuildConfig.COLLABORATION_SOCKET_EMITTER_AUTHENTICATION, JSONObject().apply {
                            put("token", authenticationInformation?.accessToken)
                        })
                    }
                }
            }
        }
    }

    private val viewModel: ClassActivityViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_class_activity, container, false)
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            socket?.run {
                if (!connected()) {

                    off(Socket.EVENT_CONNECT, onSocketAuthenticated)
                    on(Socket.EVENT_CONNECT, onSocketAuthenticated)

                    connect()

                    val authenticationInformation = retrieveAuthenticationInformation(activity!!)
                    socket?.emit(BuildConfig.COLLABORATION_SOCKET_EMITTER_AUTHENTICATION, JSONObject().apply {
                        put("token", authenticationInformation?.accessToken)
                    })
                }
            }
        }
    }

    private fun establishSocketListener() {
        socket?.run {
            off(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_LIKE, onPostLiked)
            off(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_UNLIKE, onPostUnliked)
            off(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_COMMENT, onCommentAdded)
            off(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_DELETE_COMMENT, onCommentDeleted)

            on(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_LIKE, onPostLiked)
            on(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_UNLIKE, onPostUnliked)
            on(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_COMMENT, onCommentAdded)
            on(BuildConfig.COLLABORATION_SOCKET_LISTENER_POST_DELETE_COMMENT, onCommentDeleted)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initView()
            observeView()
            observeData()
            observeError()

            viewModel.listOnlineClassesV3(classGroupId)
        }
    }

    private fun initView() {
        vClassActivity.init(
            items = items,
            allMemberCount = allMemberCount,
            color = color,
            onRefresh = {
                viewModel.listOnlineClassesV3(classGroupId)
            },
            onPostClicked = { post, isKeyboardShown ->
                openDetail(post, isKeyboardShown)
            },
            onImageClicked = { imageView, imageUrl ->
                showImage(activity!!, imageView, imageUrl)
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
                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, postId as? String ?: "", color, PostInteraction.LIKE.type)
                dialog.show(childFragmentManager, "")
            },
            onPostSeenUsersClicked = { postId ->
                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, postId as? String ?: "", color, PostInteraction.SEEN.type)
                dialog.show(childFragmentManager, "")
            },
            onShowAllOnlineClassPlatformsClicked = {

            },
            onOnlineClassPlatformClicked = { url ->
                if (url.isUrlValid() && (url.contains("http://") || url.contains("https://"))) {
                    CustomTabsIntent.Builder().build().apply {
                        launchUrl(context, Uri.parse(url))
                    }
                }
            }
        )
    }

    private fun openDetail(post: Post, isKeyboardShown: Boolean) {
        val dialog = PostDetailSheetDialog.newInstance(classGroupId, post, color, allMemberCount, isKeyboardShown)
        dialog.show(childFragmentManager, "")
    }

    private fun observeView() {
        viewModel.state.postLoading.observe(this, Observer { isLoading ->
            vClassActivity.setSwipeRefreshLayout(isLoading)
        })

        viewModel.state.onlineClassesLoading.observe(this, Observer { isLoading ->
            vClassActivity.setSwipeRefreshLayout(isLoading)
        })
    }

    private fun observeData() {
        viewModel.posts.observe(this, Observer {
            it?.let { posts ->
                items.removeAll { it.post != null }

                with(items) {
                    posts.forEach {
                        add(ClassPostAdapter.Item(post = it))
                    }
                }

                with (vClassActivity) {
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
                    add(ClassPostAdapter.Item(onlineClasses = shownOnlineClasses))
                }

                viewModel.listPostsV3(classGroupId)
            }
        })
    }

    private fun likePost(post: Post) {
        val data = JSONObject()
        data.put("userId", student?.userId)
        data.put("postId", post.id.toString())

        if (post.isLike) {
            socket?.emit(BuildConfig.COLLABORATION_SOCKET_EMITTER_POST_UNLIKE, data)
        } else {
            socket?.emit(BuildConfig.COLLABORATION_SOCKET_EMITTER_POST_LIKE, data)
        }
    }

    private fun readPost(postId: String) {
        val data = JSONObject()
        data.put("userId", student?.userId)
        data.put("postId", postId.toString())
        socket?.emit(BuildConfig.COLLABORATION_SOCKET_EMITTER_POST_SEEN, data)
    }

    private fun observeError() {
        viewModel.postErrorResponseV3.observe(this, Observer {
            it?.let {
                if (it.statusCode != 404) {
                    showApiResponseXAlert(activity, it)
                }
            }

            items.removeAll { it.post != null }

            with (vClassActivity) {
                refreshRecyclerView(items)
                checkPostAmount()
            }
        })

        viewModel.onlineClassesErrorResponseV3.observe(this, Observer {
            it?.let {
                showApiResponseXAlert(activity, it)
            }

            items.removeAll { it.onlineClasses != null }

            viewModel.listPostsV3(classGroupId)
        })
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
}