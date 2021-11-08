package whiz.sspark.library.extension

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.Gender
import whiz.sspark.library.utility.isValidContextForGlide
import java.io.File

fun ImageView.show(resId: Int) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(resId)
        .into(this)
}

fun ImageView.show(url: String) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .load(url)
        .into(this)
}

fun ImageView.show(drawable: Drawable) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(drawable)
        .into(this)
}

fun ImageView.show(url: String, defaultImage: Int) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    val requestOptions = RequestOptions()
        .placeholder(defaultImage)
        .error(defaultImage)

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}

fun ImageView.show(file: File) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(file)
        .into(this)
}

fun ImageView.show(bitmap: Bitmap) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(bitmap)
        .into(this)
}

fun ImageView.showBlurImage(url: String, blurRadius: Int) {
    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(url)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(blurRadius, 3)))
        .into(this)
}

fun ImageView.showUserProfileCircle(profileImageURL: String, gender: Long) {
    val defaultImage = when (gender) {
        Gender.MALE.type -> R.drawable.ic_male_circular
        Gender.FEMALE.type -> R.drawable.ic_female_circular
        else -> R.drawable.ic_male_circular
    }

    val requestOptions = RequestOptions
        .diskCacheStrategyOf(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .placeholder(defaultImage)
        .error(defaultImage)
        .circleCrop()

    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .load(profileImageURL)
        .apply(requestOptions)
        .into(this)
}

fun ImageView.showProfile(imageUrl: String, gender: Long) {
    val defaultImage = when (gender) {
        Gender.MALE.type -> R.drawable.ic_male_circular
        Gender.FEMALE.type -> R.drawable.ic_female_circular
        else -> R.drawable.ic_male_circular
    }

    val requestOptions = RequestOptions
        .diskCacheStrategyOf(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .placeholder(defaultImage)
        .error(defaultImage)

    if (!isValidContextForGlide(this.context)) {
        return
    }

    Glide.with(this.context)
        .load(imageUrl)
        .apply(requestOptions)
        .into(this)
}