package com.vps.android.core.ext.glide

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File

fun ImageView.loadImage(request: LoadImageRequest) = post {
    val imageSource: ImageSource = request.imageSource

    if (imageSource is ImageSource.None) {
        return@post
    }

    var options = RequestOptions()

    options = applyRoundedCorners(options, request.roundedCorners)
    options = applyResize(options, request.resizeType, this)

    if (request.placeholder != NO_RESOURCE) {
        options = options.placeholder(request.placeholder)
    }
    if (request.errorImage != NO_RESOURCE) {
        options = options.error(request.errorImage)
    }

    options = options.diskCacheStrategy(request.cacheStrategy)
    options = options.skipMemoryCache(request.skipMemoryCache)

    var requestBuilder =
        Glide.with(this)
            .asBitmap()
            .apply {
                applyImageSource(request.imageSource, this)
            }
            .listener(
                object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        request.onLoadFailedListener()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        request.onResourceReadyListener()
                        return false
                    }
                }
            )
            .apply(options)

    requestBuilder = when {
        request.transformations.size > 1 ->
            requestBuilder.transform(MultiTransformation(request.transformations))
        request.transformations.isNotEmpty() -> requestBuilder.transform(request.transformations.first())
        else -> requestBuilder
    }
    requestBuilder
        .into(this)
}

fun ImageView.loadImage(builder: LoadImageRequest.() -> Unit) {
    val request = LoadImageRequest()
    request.builder()
    loadImage(request)
}

fun ImageView.loadRemote(url: String, builder: LoadImageRequest.() -> Unit = {}) {
    val request = LoadImageRequest()
    request.builder()
    request.remoteImage(url)
    loadImage(request)
}

fun ImageView.loadFile(filePath: String, builder: LoadImageRequest.() -> Unit = {}) {
    val request = LoadImageRequest()
    request.builder()
    request.fileImage(filePath)
    loadImage(request)
}

fun ImageView.loadBitmap(bitmap: Bitmap, builder: LoadImageRequest.() -> Unit = {}) {
    val request = LoadImageRequest()
    request.builder()
    request.bitmapImage(bitmap)
    loadImage(request)
}

fun ImageView.loadAvatar(url: String, builder: LoadImageRequest.() -> Unit = {}) {
    val request = LoadImageRequest()
    request.builder()
    request.remoteImage(url)
    request.asCircle()
    loadImage(request)
}

fun ImageView.loadResource(@DrawableRes drawable: Int, builder: LoadImageRequest.() -> Unit = {}) {
    val request = LoadImageRequest()
    request.builder()
    request.resourceImage(drawable)
    loadImage(request)
}

fun ImageView.loadUri(uri: String, builder: LoadImageRequest.() -> Unit) {
    val request = LoadImageRequest()
    request.builder()
    request.fileUri(uri)
    loadImage(request)
}

private fun applyResize(
    options: RequestOptions,
    resize: ResizeType,
    source: ImageView,
): RequestOptions = when (resize) {
    is ResizeType.Resize -> options.override(resize.width, resize.height)
    is ResizeType.Auto -> {
        if (source.width != 0 && source.height != 0) {
            options.override(source.width, source.height)
        } else {
            options
        }
    }
    else -> options
}

private fun applyRoundedCorners(
    options: RequestOptions,
    corners: RoundedCornersType,
): RequestOptions = when (corners) {
    is RoundedCornersType.Circle -> options.circleCrop()
    is RoundedCornersType.EqualCorners -> options.transform(RoundedCorners(corners.radius))
    else -> options
}

private fun applyImageSource(
    imageSource: ImageSource,
    requestBuilder: RequestBuilder<Bitmap>,
) {
    when (imageSource) {
        is ImageSource.RemoteImage -> requestBuilder.load(imageSource.url)
        is ImageSource.ResourceImage -> requestBuilder.load(imageSource.resourceId)
        is ImageSource.FileImage -> requestBuilder.load(File(imageSource.filePath))
        is ImageSource.UriImage -> requestBuilder.load(Uri.parse(imageSource.uri))
        else -> requestBuilder.load("")
    }
}
