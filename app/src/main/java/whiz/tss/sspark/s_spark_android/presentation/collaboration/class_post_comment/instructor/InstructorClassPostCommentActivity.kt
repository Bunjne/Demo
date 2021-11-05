package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.instructor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.SSparkLibrary
import whiz.sspark.library.data.entity.Comment
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.static.SocketPath
import whiz.sspark.library.data.viewModel.ClassPostCommentViewModel
import whiz.sspark.library.extension.convertToDate
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.showAlertWithMultipleItems
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_post_comment.instructor.InstructorClassPostCommentAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityInstructorClassPostCommentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction.LikeBySeenByDialogFragment
import whiz.tss.sspark.s_spark_android.utility.*
import java.net.URISyntaxException
import java.util.*

class InstructorClassPostCommentActivity : BaseActivity() {

    companion object {
        const val LIKE_BY_DIALOG = "LIKE_BY_DIALOG"
        const val SEEN_BY_DIALOG = "SEEN_BY_DIALOG"
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

    private val viewModel: ClassPostCommentViewModel by viewModel()

    private lateinit var binding: ActivityInstructorClassPostCommentBinding

    private val instructorUserId by lazy {
        retrieveUserID(this)
    }

    private val classGroupId by lazy {
        intent?.getStringExtra("classGroupId") ?: ""
    }

    private val post by lazy {
        intent?.getStringExtra("post")?.toObject<Post>() ?: Post()
    }

    private val startColor by lazy {
        intent?.getIntExtra("startColor", ContextCompat.getColor(this, R.color.primaryStartColor)) ?: ContextCompat.getColor(this, R.color.primaryStartColor)
    }

    private val endColor by lazy {
        intent?.getIntExtra("endColor", ContextCompat.getColor(this, R.color.primaryEndColor)) ?: ContextCompat.getColor(this, R.color.primaryEndColor)
    }

    private val allMemberCount by lazy {
        intent?.getIntExtra("allMemberCount", 0) ?: 0
    }

    private val isKeyboardShown by lazy {
        intent?.getBooleanExtra("isKeyboardShown", false) ?: false
    }

    private val onPostLiked by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val userId = data.getString("userId")
                    val postId = data.getString("postId")
                    val likeCounts = data.getInt("likeCounts")
                    val isSocketLiked = data.getBoolean("isLike")

                    if (postId.contains(post.id, true)) {
                        post.run {
                            isLike = if (userId == instructorUserId) {
                                isSocketLiked
                            } else {
                                isLike
                            }

                            likeCount = likeCounts
                        }

                        runOnUiThread {
                            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
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

                    if (postId.contains(post.id, true)) {
                        post.run {
                            isLike = if (instructorUserId == userId) {
                                isSocketLiked
                            } else {
                                isLike
                            }

                            likeCount = likeCounts
                        }

                        runOnUiThread {
                            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private val onCommentUpdated by lazy {
        Emitter.Listener {
            try {
                val data = it.getOrNull(0) as? JSONObject
                data?.let { data ->
                    val userId = data.getString("userId")
                    val postId = data.getString("postId")
                    val commentId = data.getString("commentId")
                    val message = data.getString("message")
                    val createdAtString = data.getString("createdAt")

                    if (postId.contains(post.id, true)) {
                        val member = members?.students?.singleOrNull { it.id == userId } ?: members?.instructors?.singleOrNull { it.id == userId }

                        member?.let { member ->
                            val createdAt = createdAtString.convertToDate(DateTimePattern.serviceDateFullFormat) ?: Date()

                            val comment = Comment(
                                id = commentId.toString().toLowerCase(),
                                author = member,
                                message = message,
                                datetime = createdAt
                            )

                            runOnUiThread {
                                insertComment(comment)

                                if (instructorUserId == userId) {
                                    binding.vPostDetailSheetDialog.scrollToPosition(postCommentItems.lastIndex)
                                }
                            }
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

                    if (postId.contains(post.id, true)) {
                        val commentId = data.getString("commentId").toLowerCase()

                        runOnUiThread {
                            updateCommentDeletion(commentId)
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

                runOnUiThread {
                    refreshToken(this) {
                        val authenticationInformation = retrieveAuthenticationInformation(this)
                        socket?.emit(SocketPath.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
                            put("token", authenticationInformation?.accessToken)
                        })
                    }
                }
            }
        }
    }

    private var members: Member? = null

    private val postCommentItems = mutableListOf<InstructorClassPostCommentAdapter.PostCommentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructorClassPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        viewModel.getMember(classGroupId, false)
    }

    override fun onResume() {
        super.onResume()

        viewModel.listComments(classGroupId, post.id)

        socket?.run {
            if (!connected()) {

                off(Socket.EVENT_CONNECT, onSocketAuthenticated)
                on(Socket.EVENT_CONNECT, onSocketAuthenticated)

                connect()

                val authenticationInformation = retrieveAuthenticationInformation(this@InstructorClassPostCommentActivity)
                socket?.emit(SocketPath.collaborationSocketEmitterAuthenticationPath, JSONObject().apply {
                    put("token", authenticationInformation?.accessToken)
                })
            }
        }
    }

    private fun establishSocketListener() {
        socket?.run {
            off(SocketPath.collaborationSocketListenerPostCommentPath, onCommentUpdated)
            off(SocketPath.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)
            off(SocketPath.collaborationSocketListenerPostLikePath, onPostLiked)
            off(SocketPath.collaborationSocketListenerPostUnLikePath, onPostUnliked)

            on(SocketPath.collaborationSocketListenerPostCommentPath, onCommentUpdated)
            on(SocketPath.collaborationSocketListenerPostDeleteCommentPath, onCommentDeleted)
            on(SocketPath.collaborationSocketListenerPostLikePath, onPostLiked)
            on(SocketPath.collaborationSocketListenerPostUnLikePath, onPostUnliked)
        }
    }

    override fun initView() {
        if (SSparkLibrary.isDarkModeEnabled) {
            window?.statusBarColor = ContextCompat.getColor(this, R.color.viewBaseSecondaryColor)
        }

        with(binding.vProfile) {
            registerLifecycleOwner(lifecycle)
            setBackgroundGradientColor(startColor, endColor)
        }

        postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(post = post))

        binding.vPostDetailSheetDialog.init(
            color = startColor,
            postCommentItems = postCommentItems,
            allMemberCount = allMemberCount,
            onImageClicked = { imageView, url ->
                showImage(this, imageView, url)
            },
            onCommentItemClicked = { comment ->
                showCommentOption(comment)
            },
            onPostLiked = { post ->
                likePost(post)
            },
            onCommentSent = { message ->
                addComment(message)
            },
            onDisplayLikedUsersClicked = {
                val isLikeByDialogNotShown = supportFragmentManager.findFragmentByTag(LIKE_BY_DIALOG) == null

                if (isLikeByDialogNotShown) {
                    val dialog = LikeBySeenByDialogFragment.newInstance(classGroupId, post.id, startColor, endColor, PostInteraction.LIKE.type)
                    dialog.show(supportFragmentManager, LIKE_BY_DIALOG)
                }
            },
            onDisplaySeenUsersClicked = {
                val isSeenByDialogNotShown = supportFragmentManager.findFragmentByTag(SEEN_BY_DIALOG) == null

                if (isSeenByDialogNotShown) {
                    val dialog = LikeBySeenByDialogFragment.newInstance(classGroupId, post.id, startColor, endColor, PostInteraction.SEEN.type)
                    dialog.show(supportFragmentManager, SEEN_BY_DIALOG)
                }
            }
        )

        renderPost()
    }

    private fun showCommentOption(comment: Comment) {
        if (comment.author.id == instructorUserId) {
            showAlertWithMultipleItems(resources.getStringArray(R.array.class_post_comment_action_owner).toList()) { index ->
                when (index) {
                    0 -> (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(comment.message, comment.message))
                    1 -> deleteComment(comment)
                    2 -> { }
                }
            }
        } else {
            showAlertWithMultipleItems(resources.getStringArray(R.array.class_post_comment_action_other).toList()) { index ->
                when (index) {
                    0 -> (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(comment.message, comment.message))
                    1 -> { }
                }
            }
        }
    }

    private fun addComment(message: String) {
        viewModel.addComment(classGroupId, post.id, message)
    }

    private fun deleteComment(comment: Comment) {
        viewModel.deleteComment(classGroupId, post.id, comment.id)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
                if (isLoading == true) {
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
        })
    }

    override fun observeData() {
        viewModel.memberResponses.observe(this, Observer {
            it?.let {
                members = it
            }
        })

        viewModel.commentResponses.observe(this, Observer {
            it?.let {
                renderComments(it)
                binding.vPostDetailSheetDialog.showKeyboardMessageEdittext(isKeyboardShown)
            }
        })
    }

    private fun renderPost() {
        if (postCommentItems.isEmpty()) {
            postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(post = post))
            binding.vPostDetailSheetDialog.notifyRecycleViewItemInserted(0)
        } else {
            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
        }
    }

    private fun renderComments(comments: List<Comment>) {
        postCommentItems.removeAll { it.comment != null }

        postCommentItems.addAll(comments.map { InstructorClassPostCommentAdapter.PostCommentItem(comment = it) })

        binding.vPostDetailSheetDialog.updateItem()
    }

    private fun insertComment(comment: Comment) {
        postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(comment = comment))

        postCommentItems.singleOrNull { it.post != null }?.post?.apply {
            commentCount += 1
        }

        binding.vPostDetailSheetDialog.notifyRecycleViewItemInserted(postCommentItems.size)
        binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
    }

    private fun updateCommentDeletion(commentId: String) {
        val commentPosition = postCommentItems.indexOfFirst { it.post?.id?.contains(commentId, true) ?: false }
        if (commentPosition > -1) {
            postCommentItems.removeAll { it.comment?.id == commentId }

            postCommentItems.singleOrNull { it.post != null }?.post?.apply {
                commentCount -= 1
            }

            binding.vPostDetailSheetDialog.notifyRecycleViewItemRemoved(commentPosition)
            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
        }
    }

    private fun likePost(post: Post) {
        val data = JSONObject().apply {
            put("userId", instructorUserId)
            put("postId", post.id)
        }

        if (post.isLike) {
            socket?.emit(SocketPath.collaborationSocketEmitterPostUnLikePath, data)
        } else {
            socket?.emit(SocketPath.collaborationSocketEmitterPostLikePath, data)
        }
    }

    override fun observeError() {
        with (viewModel) {
            listOf(commentErrorResponse, memberErrorResponse, addCommentErrorResponse, deleteCommentErrorResponse).forEach {
                it.observe(this@InstructorClassPostCommentActivity, Observer {
                    it?.let {
                        showApiResponseXAlert(this@InstructorClassPostCommentActivity, it)
                    }
                })
            }

            errorMessage.observe(this@InstructorClassPostCommentActivity, Observer {
                it?.let {
                    showAlertWithOkButton(it)
                }
            })
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
}