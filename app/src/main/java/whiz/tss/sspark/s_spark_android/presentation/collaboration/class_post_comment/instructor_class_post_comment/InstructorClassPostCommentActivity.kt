package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.instructor_class_post_comment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
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
import whiz.sspark.library.data.entity.ClassMember
import whiz.sspark.library.data.entity.Instructor
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.entity.Post
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.data.static.SocketPath
import whiz.sspark.library.data.viewModel.ClassPostCommentViewModel
import whiz.sspark.library.extension.convertToDate
import whiz.sspark.library.utility.showAlertWithMultipleItems
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_post_comment.instructor.InstructorClassPostCommentAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityInstructorClassPostCommentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.utility.logout
import whiz.tss.sspark.s_spark_android.utility.refreshToken
import whiz.tss.sspark.s_spark_android.utility.retrieveAuthenticationInformation
import whiz.tss.sspark.s_spark_android.utility.showImage
import java.net.URISyntaxException
import java.util.*

class InstructorClassPostCommentActivity : BaseActivity() {

    private val loadingDialog by lazy {
//        activity!!.indeterminateProgressDialog(resources.getString(R.string.general_alertmessage_pleasewait_title)) {
//            setCancelable(false) TODO waiting for loading dialog implement
//        }
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

    private lateinit var instructor: Instructor

    private val classGroupId by lazy {
        intent?.getStringExtra("classGroupId") ?: ""
    }

    private val post by lazy {
        intent?.getParcelableExtra("post") ?: Post()
    }

    private val color by lazy {
        intent?.getIntExtra("color", Color.BLACK) ?: Color.BLACK
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

                    if (postId.contains(post.id.toString(), true)) {
                        post.run {
                            isLike = if (userId == instructor?.userId) isSocketLiked else isLike
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

                    if (postId.contains(post.id.toString(), true)) {
                        post.run {
                            isLike = if (instructor.userId == userId) isSocketLiked else isLike
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

                    if (postId.contains(post.id.toString(), true)) {
                        var member = members?.students?.singleOrNull { it.userId == userId } ?: members?.instructors?.singleOrNull { it.userId == userId }

                        if (member == null && instructor != null && userId == instructor?.userId) {
                            with(instructor!!) {
                                member = ClassMember(
                                    code = code,
                                    userId = userId,
                                    _firstNameEn = firstNameEn,
                                    _firstNameTh = firstNameTh,
                                    _lastNameEn = lastNameEn,
                                    _lastNameTh = lastNameTh,
                                    profileImageUrl = profileImageUrl,
                                    colorCode = colorCode
                                )
                            }
                        }

                        member?.let { member ->
                            val createdAt = createdAtString.convertToDate(DateTimePattern.serviceDateFullFormat) ?: Date()

                            val comment = Post(
                                _id = commentId.toString().toLowerCase(),
                                author = member,
                                message = message,
                                createdAt = createdAt
                            )

                            runOnUiThread {
                                insertComment(comment)

                                if (instructor.userId == userId) {
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

                    if (postId.contains(post.id.toString(), true)) {
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

    private var isDataFetched = false
    private val comments = mutableListOf<Post>()
    private var members: Member? = null

    private val postCommentItems = mutableListOf<InstructorClassPostCommentAdapter.PostCommentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructorClassPostCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            profileManager.instructor.collect {
                it?.let {
                    instructor = it

                    initView()
                } ?: run {
                    logout(this@InstructorClassPostCommentActivity)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

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

        postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(type = InstructorClassPostCommentAdapter.PostCommentType.POST, post = post))

        binding.vPostDetailSheetDialog.init(
            color = color,
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
                emitComment(message)
            },
            onDisplayLikedUsersClicked = {
//                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, post.id as? String ?: "", color, PostInteraction.LIKE.type)
//                dialog.show(childFragmentManager, "") TODO waiting for PostInteraction Dialog implementation
            },
            onDisplaySeenUsersClicked = {
//                val dialog = ClassPostInteractionDialogFragment.newInstance(classGroupId, post.id as? String ?: "", color, PostInteraction.SEEN.type)
//                dialog.show(childFragmentManager, "") TODO waiting for PostInteraction Dialog implementation
            }
        )

        renderPost()
    }

    private fun showCommentOption(comment: Post) {
        if (comment.author.userId == instructor.userId) {
            showAlertWithMultipleItems(resources.getStringArray(R.array.class_post_comment_action_owner).toList()) { index ->
                when (index) {
                    0 -> (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(comment.message, comment.message))
                    1 -> deleteComment(comment)
                    2 -> {
                    }
                }
            }
        } else {
            showAlertWithMultipleItems(resources.getStringArray(R.array.class_post_comment_action_owner).toList()) { index ->
                when (index) {
                    0 -> (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(comment.message, comment.message))
                    1 -> {
                    }
                }
            }
        }
    }

    private fun emitComment(text: String) {
        val data = JSONObject().apply {
            put("userId", instructor?.userId)
            put("postId", post.id.toString())
            put("message", text)
            put("classId", classGroupId)
        }

        socket?.emit(SocketPath.collaborationSocketEmitterPostCommentPath, data)
    }

    private fun deleteComment(comment: Post) {
        val data = JSONObject().apply {
            put("postId", post.id.toString())
            put("commentId", comment.id.toString())
        }

        socket?.emit(SocketPath.collaborationSocketEmitterPostDeleteCommentPath, data)
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this, Observer { isLoading ->
                if (isLoading == true) {
                    //TODO waiting for loading dialog
                } else {
                    //TODO waiting for loading dialog
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
                with(comments) {
                    clear()
                    addAll(it)
                }

                renderComments()
                binding.vPostDetailSheetDialog.showKeyboardMessageEdittext(isKeyboardShown)
            }
        })
    }

    private fun renderPost() {
        if (postCommentItems.isEmpty()) {
            postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(type = InstructorClassPostCommentAdapter.PostCommentType.POST, post = post))
            binding.vPostDetailSheetDialog.notifyRecycleViewItemInserted(0)
        } else {
            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
        }
    }

    private fun renderComments() {
        while (postCommentItems.size > 1) {
            postCommentItems.removeAt(1)
        }

        postCommentItems.addAll(1, comments.map { InstructorClassPostCommentAdapter.PostCommentItem(type = InstructorClassPostCommentAdapter.PostCommentType.COMMENT, post = it) })
        binding.vPostDetailSheetDialog.notifyRecycleViewItemRangeInserted(1, comments.size)
    }

    private fun insertComment(comment: Post) {
        comments.add(comment)
        postCommentItems.add(InstructorClassPostCommentAdapter.PostCommentItem(type = InstructorClassPostCommentAdapter.PostCommentType.COMMENT, post = comment))

        binding.vPostDetailSheetDialog.notifyRecycleViewItemInserted(comments.size + 1)
        binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
    }

    private fun updateCommentDeletion(postId: String) {
        val commentPosition = postCommentItems.indexOfFirst { it.post.id.toString().contains(postId, true) }
        if (commentPosition > -1) {
            comments.removeAll { it.id == postId }
            postCommentItems.removeAll { it.post.id == postId }

            binding.vPostDetailSheetDialog.notifyRecycleViewItemRemoved(commentPosition)
            binding.vPostDetailSheetDialog.notifyRecycleViewItemChanged(0)
        }
    }

    private fun likePost(post: Post) {
        val data = JSONObject().apply {
            put("userId", instructor.userId.toString())
            put("postId", post.id.toString())
        }

        if (post.isLike) {
            socket?.emit(SocketPath.collaborationSocketEmitterPostUnLikePath, data)
        } else {
            socket?.emit(SocketPath.collaborationSocketEmitterPostLikePath, data)
        }
    }

    override fun observeError() {
        with (viewModel) {
            listOf(commentErrorResponse, memberErrorResponse).forEach {
                it.observe(this@InstructorClassPostCommentActivity, Observer {
                    it?.let {
                        showApiResponseXAlert(this@InstructorClassPostCommentActivity, it)
                    }
                })
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
}