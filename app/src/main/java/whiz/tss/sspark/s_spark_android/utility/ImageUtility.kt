package whiz.tss.sspark.s_spark_android.utility

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.presentation.image.ImageActivity

fun showImage(activity: Activity, ivPost: ImageView, url: String) {
    val intent = Intent(activity, ImageActivity::class.java)
    intent.putExtra("image", url)
    val transitionName = activity.resources.getString(R.string.image_transition_name)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, ivPost, transitionName)
    activity.startActivity(intent, options.toBundle())

}