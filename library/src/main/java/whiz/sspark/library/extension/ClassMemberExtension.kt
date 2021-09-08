package whiz.sspark.library.extension

import whiz.sspark.library.data.entity.ClassMember

fun ClassMember.toDefaultDisplayName() = "${this._firstNameEn.toFirstCharacter()}${this._lastNameEn.toFirstCharacter()}"

fun ClassMember.toInstructorFullName() = if (this.position.isBlank()) {
    "${this.firstName} ${this.lastName}"
} else {
    "${this.position} ${this.firstName} ${this.lastName}"
}

fun ClassMember.toCommentDisplayName() = "${this.firstName} ${this.lastName}"