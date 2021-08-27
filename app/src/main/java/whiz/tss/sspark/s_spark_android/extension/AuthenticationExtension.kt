package whiz.tss.sspark.s_spark_android.extension

import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.tss.sspark.s_spark_android.data.enum.RoleType

fun AuthenticationInformation.getRoleType() = when(this.role) {
    RoleType.INSTRUCTOR.type -> RoleType.INSTRUCTOR
    RoleType.GUARDIAN.type -> RoleType.GUARDIAN
    else -> RoleType.STUDENT
}
