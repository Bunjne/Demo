package whiz.tss.sspark.s_spark_android.extension

import whiz.sspark.library.data.entity.AuthenticationInformation
import whiz.tss.sspark.s_spark_android.data.enum.RoleType
import whiz.tss.sspark.s_spark_android.utility.getRoleFromToken
import java.lang.StringBuilder

fun AuthenticationInformation.getRoleType(): RoleType {
    val role = getRoleFromToken(accessToken)
    return when(role) {
        RoleType.INSTRUCTOR_SENIOR.type -> RoleType.INSTRUCTOR_SENIOR
        RoleType.INSTRUCTOR_JUNIOR.type -> RoleType.INSTRUCTOR_JUNIOR
        RoleType.GUARDIAN.type -> RoleType.GUARDIAN
        RoleType.STUDENT_SENIOR.type -> RoleType.STUDENT_SENIOR
        RoleType.STUDENT_JUNIOR.type -> RoleType.STUDENT_JUNIOR
        else -> RoleType.STUDENT_JUNIOR
    }
}

fun AuthenticationInformation.getAuthorizationToken(): String {
    val builder = StringBuilder()
    builder.append(token_type)
    builder.append(" ")
    builder.append(accessToken)

    return builder.toString()
}
