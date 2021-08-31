package whiz.tss.sspark.s_spark_android.extension

import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import java.lang.IllegalStateException

fun AuthenticationInformation.getRoleType() = when(this.role) {
    RoleType.INSTRUCTOR.type -> RoleType.INSTRUCTOR
    RoleType.GUARDIAN.type -> RoleType.GUARDIAN
    RoleType.SENIOR.type -> RoleType.SENIOR
    RoleType.JUNIOR.type -> RoleType.JUNIOR
    else -> RoleType.JUNIOR
}
