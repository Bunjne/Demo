package whiz.sspark.library.utility

import whiz.sspark.library.data.enum.ItemPosition


fun getItemPositionType(isNextItemHeader: Boolean, isPreviousItemHeader: Boolean) : String {
    return if (isPreviousItemHeader && isNextItemHeader ) {
        ItemPosition.SINGLE.position
    } else if (isPreviousItemHeader && !isNextItemHeader) {
        ItemPosition.FIRST.position
    } else if (!isPreviousItemHeader && isNextItemHeader) {
        ItemPosition.LAST.position
    } else {
        ItemPosition.MIDDLE.position
    }
}