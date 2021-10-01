package com.vps.android.core.ext.glide

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

fun LoadImageRequest.remoteImage(url: String): LoadImageRequest =
    apply {
        imageSource = ImageSource.RemoteImage(url)
    }

fun LoadImageRequest.fileImage(path: String): LoadImageRequest =
    apply {
        imageSource = ImageSource.FileImage(path)
    }

fun LoadImageRequest.bitmapImage(bitmap: Bitmap): LoadImageRequest =
    apply {
        imageSource = ImageSource.BitmapImage(bitmap)
    }

fun LoadImageRequest.resourceImage(@DrawableRes image: Int): LoadImageRequest =
    apply {
        imageSource = ImageSource.ResourceImage(image)
    }

fun LoadImageRequest.withRoundedCorners(radius: Int): LoadImageRequest =
    apply {
        roundedCorners = RoundedCornersType.EqualCorners(radius)
    }

fun LoadImageRequest.asCircle(): LoadImageRequest =
    apply {
        roundedCorners = RoundedCornersType.Circle
    }

fun LoadImageRequest.fileUri(uri: String): LoadImageRequest =
    apply {
        imageSource = ImageSource.UriImage(uri)
    }

fun LoadImageRequest.resize(width: Int, height: Int): LoadImageRequest =
    apply {
        resizeType = ResizeType.Resize(width, height)
    }

fun LoadImageRequest.withPlaceholder(
    @DrawableRes placeholder: Int,
): LoadImageRequest = apply {
    this.placeholder = placeholder
}
