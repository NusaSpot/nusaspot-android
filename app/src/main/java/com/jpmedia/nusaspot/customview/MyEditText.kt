package com.jpmedia.nusaspot.customview



import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.jpmedia.nusaspot.R

class MyEditText : AppCompatEditText {

    enum class ValidationType {
        EMAIL, PASSWORD
    }

    private var validationType: ValidationType = ValidationType.EMAIL

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditText)
        val validationTypeValue = typedArray.getString(R.styleable.MyEditText_validationType)
        if (validationTypeValue == "password") {
            validationType = ValidationType.PASSWORD
        }
        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (validationType == ValidationType.EMAIL) {
                    val email = s.toString()
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        error = "Format email tidak valid"
                    } else {
                        error = null
                    }
                } else if (validationType == ValidationType.PASSWORD) {
                    if (text.length < 8) {
                        error = "Password tidak boleh kurang dari 8 karakter"
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}

