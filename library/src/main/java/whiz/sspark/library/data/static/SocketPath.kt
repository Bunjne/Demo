package whiz.sspark.library.data.static

object SocketPath {
    //Collaboration
    var collaborationSocketEmitterAuthenticationPath = "\"authenticate\""
    var collaborationSocketListenerAuthenticationPath = "\"authenticated\""
    var collaborationSocketListenerUnAuthenticationPath = "\"unauthorized\""
    var collaborationSocketListenerPostSeenPath = "\"seenPostCount\""
    var collaborationSocketEmitterPostSeenPath = "\"seenPost\""
    var collaborationSocketListenerPostLikePath = "\"likePostCount\""
    var collaborationSocketEmitterPostLikePath = "\"likePost\""
    var collaborationSocketListenerPostUnLikePath = "\"unlikePostCount\""
    var collaborationSocketEmitterPostUnLikePath = "\"unlikePost\""
    var collaborationSocketListenerPostCommentPath = "\"newComment\""
    var collaborationSocketEmitterPostCommentPath = "\"comment\""
    var collaborationSocketListenerPostDeleteCommentPath = "\"deletedComment\""
    var collaborationSocketEmitterPostDeleteCommentPath = "\"deleteComment\""
}