package dev.fummicc1.lit.snacker.services

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class RemoteImageLoader(val picasso: Picasso) {

    fun loadImageIntoImageView(url: String, imageView: ImageView, onSuccess: () -> Unit, onError: (Exception?) -> Unit) {
        try {
            picasso.load(url).into(imageView, object: Callback {
                override fun onSuccess() {
                    onSuccess()
                }

                override fun onError(e: Exception?) {
                    onError(e)
                }
            })
        } catch (e: Exception) {
            onError(e)
        }
    }
}