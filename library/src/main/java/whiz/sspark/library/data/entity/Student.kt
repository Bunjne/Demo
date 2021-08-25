package whiz.sspark.library.data.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class Student(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("userId") val userId: String = "",
    @SerializedName("advisorId") val advisorId: String = "",
    @SerializedName("code") val code: String = "",
    @SerializedName("batchCode") val batchCode: String = "",
    @SerializedName("titleNameEn") val titleNameEn: String = "",
    @SerializedName("titleNameTh") val titleNameTh: String = "",
    @SerializedName("levelCode") val levelCode: String = "",
    @SerializedName("levelNameEn") val _levelNameEn: String = "",
    @SerializedName("levelNameTh") val _levelNameTh: String = "",
    @SerializedName("levelNameCn") val _levelNameCn: String = "",
    @SerializedName("firstNameEn") val _firstNameEn: String = "",
    @SerializedName("firstNameTh") val _firstNameTh: String = "",
    @SerializedName("firstNameCn") val _firstNameCn: String = "",
    @SerializedName("lastNameEn") val _lastNameEn: String = "",
    @SerializedName("lastNameTh") val _lastNameTh: String = "",
    @SerializedName("lastNameCn") val _lastNameCn: String = "",
    @SerializedName("gender") val gender: String = "0",
    @SerializedName("nationCode") val nationalityCode: String = "",
    @SerializedName("nationNameEn") val nationalityNameEn: String = "",
    @SerializedName("nationNameTh") val nationalityNameTh: String = "",
    @SerializedName("nationNameCn") val nationalityNameCn: String = "",
    @SerializedName("citizenId") val citizenId: String? = null,
    @SerializedName("programCode") val programCode: String = "",
    @SerializedName("programNameEn") val _programNameEn: String = "",
    @SerializedName("programNameTh") val _programNameTh: String = "",
    @SerializedName("facultyCode") val facultyCode: String = "",
    @SerializedName("facultyNameEn") val _facultyNameEn: String = "",
    @SerializedName("facultyNameTh") val _facultyNameTh: String = "",
    @SerializedName("facultyNameCn") val _facultyNameCn: String = "",
    @SerializedName("facultyLogoUrl") val facultyLogoUrl: String = "",
    @SerializedName("departmentCode") val departmentCode: String = "",
    @SerializedName("departmentNameEn") val _departmentNameEn: String = "",
    @SerializedName("departmentNameTh") val _departmentNameTh: String = "",
    @SerializedName("departmentNameCn") val _departmentNameCn: String = "",
    @SerializedName("universityNameEn") val _universityNameEn: String = "",
    @SerializedName("universityNameTh") val _universityNameTh: String = "",
    @SerializedName("universityLogoUrl") val universityLogoUrl: String = "",
    @SerializedName("isGraduated") val isGraduated: Boolean = false,
    @SerializedName("gpa") val gpa: Double = 0.0,
    @SerializedName("credit") val credit: Int = 0,
    @SerializedName("studyYear") val studyYear: Double = 0.0,
    @SerializedName("creditMax") val creditMax: Int = 0,
    @SerializedName("creditMin") val creditMin: Int = 0,
    @SerializedName("universityEmail") val universityEmail: String = "",
    @SerializedName("birthDate") val birthDate: Date = Date(),
    @SerializedName("fullNameEn") val _fullNameEn: String = "",
    @SerializedName("fullNameTh") val _fullNameTh: String = "",
    @SerializedName("fullNameCn") val _fullNameCn: String = "",
    @SerializedName("scholarshipHour") val scholarshipHour: Int = 0,
    @SerializedName("studentActive") var studentActive: Date? = null,
    @SerializedName("issuedAt") var issuedAt: Date? = null,
    @SerializedName("expiredAt") var expiredAt: Date? = null,
    @SerializedName("profileImageUrl") var profileImageUrl: String = "",
    @SerializedName("cardImageUrl") val cardImageUrl: String = "",
    @SerializedName("billingAddress1") var billingAddress1: String = "00000",
    @SerializedName("billingAddress2") var billingAddress2: String = "",
    @SerializedName("billingPostalCode") var billingPostalCode: String = "",
    @SerializedName("today") var today: Date = Date(),
    @SerializedName("colorCode") var colorCode: String? = null,
    @SerializedName("hasCurriculum") val hasCurriculum: Boolean = false,
    @SerializedName("batch") var batch: String = "",
    @SerializedName("guardianStatus") val guardianStatus: String = "",
    @SerializedName("activeYear") val activeYear: Int = 0,
    @SerializedName("activeTerm") val activeTerm: Int = 0,
    @SerializedName("isWeeklyScheduleEnabled") val isWeeklyScheduleEnabled: Boolean = false,
    @SerializedName("isThai") val isThai: Boolean = false,
    @SerializedName("passportNumber") val passPortNumber: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("bankAccountNumber") val bankAccountNumber: String? = null
)