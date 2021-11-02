package whiz.sspark.library.data.entity

data class ClassMemberItem(
        val title: String? = null,
        val instructor: ClassMember? = null,
        val student: ClassMember? = null,
        val isChatEnable: Boolean = false
)