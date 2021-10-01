package com.vps.android.core.utils

sealed class Notify {

    abstract val message: String

    data class Text(override val message: String) : Notify()

    data class Action(
        override val message: String,
        val actionLabel: String,
        val actionHandler: (() -> Unit)
    ) : Notify()

    data class Error(
        override val message: String,
        val errLabel: String? = null,
        val errHandler: (() -> Unit)? = null
    ) : Notify()

    data class ErrorHide(
        override val message: String = ""
    ) : Notify()

    data class DialogAction(
        val title: String? = null,
        override val message: String,
        val positiveActionLabel: String,
        val positiveActionHandler: (() -> Unit),
        val negativeActionLabel: String? = null,
        val negativeActionHandler: (() -> Unit)? = null,
    ) : Notify()
}
