package com.vps.android.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.vps.android.R

class KeypadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    var onKeypadClicked: (String) -> Unit = {}
    var onClearClicked = {}
    var isKeypadEnabled: Boolean = true
        set(value) {
            setKeypadClickable(value)
        }

    private val root = View.inflate(context, R.layout.view_keypad, this)
    private val key1: TextView = root.findViewById(R.id.tv_1)
    private val key2: TextView = root.findViewById(R.id.tv_2)
    private val key3: TextView = root.findViewById(R.id.tv_3)
    private val key4: TextView = root.findViewById(R.id.tv_4)
    private val key5: TextView = root.findViewById(R.id.tv_5)
    private val key6: TextView = root.findViewById(R.id.tv_6)
    private val key7: TextView = root.findViewById(R.id.tv_7)
    private val key8: TextView = root.findViewById(R.id.tv_8)
    private val key9: TextView = root.findViewById(R.id.tv_9)
    private val key0: TextView = root.findViewById(R.id.tv_0)
    private val btnClear: ImageView = root.findViewById(R.id.btn_clear)

    init {
        setListeners()
    }

    private fun setListeners() {
        key1.setOnClickListener {
            onKeypadClicked.invoke("1")
        }
        key2.setOnClickListener {
            onKeypadClicked.invoke("2")
        }
        key3.setOnClickListener {
            onKeypadClicked.invoke("3")
        }
        key4.setOnClickListener {
            onKeypadClicked.invoke("4")
        }
        key5.setOnClickListener {
            onKeypadClicked.invoke("5")
        }
        key6.setOnClickListener {
            onKeypadClicked.invoke("6")
        }
        key7.setOnClickListener {
            onKeypadClicked.invoke("7")
        }
        key8.setOnClickListener {
            onKeypadClicked.invoke("8")
        }
        key9.setOnClickListener {
            onKeypadClicked.invoke("9")
        }
        key0.setOnClickListener {
            onKeypadClicked.invoke("0")
        }

        btnClear.setOnClickListener {
            onClearClicked.invoke()
        }
    }

    private fun setKeypadClickable(isEnabled: Boolean) {
        key1.isEnabled = isEnabled
        key2.isEnabled = isEnabled
        key3.isEnabled = isEnabled
        key4.isEnabled = isEnabled
        key5.isEnabled = isEnabled
        key6.isEnabled = isEnabled
        key7.isEnabled = isEnabled
        key8.isEnabled = isEnabled
        key9.isEnabled = isEnabled
        key0.isEnabled = isEnabled
        btnClear.isEnabled = isEnabled
    }
}
