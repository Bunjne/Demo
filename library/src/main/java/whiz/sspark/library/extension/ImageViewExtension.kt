package whiz.sspark.library.extension

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import whiz.sspark.library.R
import whiz.sspark.library.data.enum.Gender
import java.io.File

fun ImageView.show(resId: Int) {
    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(resId)
        .into(this)
}

fun ImageView.show(url: String) {
    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(url)
        .into(this)
}

fun ImageView.show(file: File) {
    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(file)
        .into(this)
}

fun ImageView.show(bitmap: Bitmap) {
    Glide.with(this.context)
        .setDefaultRequestOptions(RequestOptions
            .diskCacheStrategyOf(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
        )
        .load(bitmap)
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

    Glide.with(this)
        .load(profileImageURL)
        .apply(requestOptions)
        .into(this)
}