package com.core.binding

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.telemed.R
import com.telemed.interfaces.OnTextChangeListener
import com.telemed.utils.Constants
import com.telemed.utils.DateTimeUtils
import com.telemed.utils.StringUtility


class DataBindingAdapter {
    companion object {

        /*To set font on text view*/
        @JvmStatic
        @BindingAdapter("bind:font")
        fun setFont(view: TextView, fontName: String) {
            //view.typeface = Typeface.createFromAsset(view.context.assets, "fonts/" + fontName)
            view.typeface = getFontFamily(view, fontName)
        }

        /*To set font on edit text*/
        @JvmStatic
        @BindingAdapter("bind:font")
        fun setFont(view: EditText, fontName: String) {
            // view.typeface = Typeface.createFromAsset(view.context.assets, "fonts/" + fontName)
            view.typeface = getFontFamily(view, fontName)
        }

        /*To set font on TextInputLayout*/
        @JvmStatic
        @BindingAdapter("bind:font")
        fun setFont(view: TextInputLayout, fontName: String) {
            // view.typeface = Typeface.createFromAsset(view.context.assets, "fonts/" + fontName)
            view.typeface = getFontFamily(view, fontName)
        }


        fun getFontFamily(view: View, fontName: String): Typeface? {
            var typeface: Typeface? = null

            if (fontName == view.resources.getString(R.string.font_regular)) {
                typeface = ResourcesCompat.getFont(view.context, R.font.optima_regular)
            } else if (fontName == view.resources.getString(R.string.font_italic)) {
                typeface = ResourcesCompat.getFont(view.context, R.font.optima_italic)
            } else if (fontName == view.resources.getString(R.string.font_bold)) {
                typeface = ResourcesCompat.getFont(view.context, R.font.optima_bold)
            }

            typeface = ResourcesCompat.getFont(
                view.context, view.context.applicationContext.resources
                    .getIdentifier(fontName.replace(".ttf", ""), "font", view.context.packageName)
            )
            return typeface
        }


        private fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity: Activity = context
                if (activity.isDestroyed || activity.isFinishing) {
                    return false
                }
            }
            return true
        }

        @JvmStatic
        @BindingAdapter("bind:loadSdCardImage")
        fun loadSdCardImage(view: AppCompatImageView, url: String?) {
            if (url != null && StringUtility.validateString(url) && isValidContextForGlide(view.context)) {
                Glide
                    .with(view.context)
                    .load("file://" + url)
                    .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("bind:loadSdCardImage")
        fun loadSdCardImage(view: ImageView, url: String?) {
            if (url != null && StringUtility.validateString(url) && isValidContextForGlide(view.context)) {
                Glide
                    .with(view.context)
                    .load("file://" + url)
                    .into(view)
            }
        }


        @JvmStatic
        @BindingAdapter("bind:url")
        fun url(view: ImageView, url: String?) {
            if (isValidContextForGlide(view.context)) {
                if (url != null && StringUtility.validateString(url)) {
                    Glide
                        .with(view.context)
                        .load(url)
                        .placeholder(view.context.resources.getDrawable(R.drawable.ic_launcher_background))
                        .into(view)

                } else {
                    view.setImageDrawable(view.context.resources.getDrawable(R.drawable.ic_launcher_background))
                }
            }
        }

        @JvmStatic
        @BindingAdapter("bind:url", "bind:chatType")
        fun urlWithDifferentPlaceHolder(view: ImageView, url: String?, chatType: String?) {
            url(
                view,
                url,
                ResourcesCompat.getDrawable(
                    view.getResources(),
                    R.drawable.ic_launcher_background,
                    null
                )!!
            )

        }

        @JvmStatic
        @BindingAdapter("bind:url", "bind:placeholder")
        fun url(view: ImageView, url: String?, drawable: Drawable) {
            if (isValidContextForGlide(view.context)) {
                if (url != null && StringUtility.validateString(url)) {
                    Glide
                        .with(view.context)
                        .load(url)
                        .placeholder(drawable)
                        .into(view)

                } else {
                    view.setImageDrawable(drawable)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("bind:urlWithoutPlaceholder")
        fun urlWithoutPlaceholder(view: ImageView, url: String?) {
            if (url != null && StringUtility.validateString(url) && isValidContextForGlide(view.context)) {
                var completeUrl = Constants.BASE_URL + url
                Glide
                    .with(view.context)
                    .load(completeUrl)
                    .into(view)


            } else {
            }
        }


        @JvmStatic
        @BindingAdapter("bind:fade")
        fun fade(view: AppCompatImageView, fade: Boolean) {
            if (fade != null) {
                if (fade == true) {
                    view.setAlpha(0.30f)
                } else {
                    view.setAlpha(1.0f)
                }
            }
        }


        /*To show date with differenct formats*/
        @JvmStatic
        @BindingAdapter("bind:date", "bind:date_format")
        fun setDate(view: TextView, timeStamp: Long, dateFormat: String) {
            if (timeStamp > 0) {
                view.setText(DateTimeUtils.getDate(timeStamp, dateFormat))
            } else {
                view.text = ""
            }
        }

        /*To show date with different formats*/
        @JvmStatic
        @BindingAdapter("bind:date", "bind:date_format")
        fun setDate(view: TextView, timeStamp: String?, dateFormat: String) {
            if (timeStamp != null) {
                val tmStmp = java.lang.Long.parseLong(timeStamp)
                view.setText(DateTimeUtils.getDate(tmStmp, dateFormat))
            } else {
                view.text = ""
            }
        }

        @JvmStatic
        @BindingAdapter("bind:date", "bind:date_format", "bind:date_type")
        fun setDate(view: TextView, timeStamp: Long, dateFormat: String, dateType: String) {
            if (timeStamp > 0) {
                view.setText(DateTimeUtils.getDate(timeStamp, dateFormat))
            } else {
                view.text = dateType
            }
        }

        @JvmStatic
        @BindingAdapter("bind:fromDate", "bind:toDate", "bind:date_format", "bind:date_type")
        fun setDate(
            view: TextView,
            fromTimeStamp: Long,
            toTimeStamp: Long,
            dateFormat: String,
            dateType: String
        ) {
            if (fromTimeStamp > 0 && toTimeStamp > 0) {
                view.setText(
                    DateTimeUtils.getDate(
                        fromTimeStamp,
                        dateFormat
                    ) + " - " + DateTimeUtils.getDate(toTimeStamp, dateFormat)
                )
            } else {
                view.text = dateType
            }
        }

        /*To show date with different formats*/
        @JvmStatic
        @BindingAdapter("bind:date")
        fun setChatListDate(view: TextView, timeStmp: Long) {
            if (timeStmp != null && timeStmp > 0) {
                view.setText(
                    DateTimeUtils.getChatListDate(
                        view.context,
                        timeStmp,
                        Constants.DATE_FORMAT_dd_MM_yy
                    )
                )
            } else {
                view.text = ""
            }
        }

        @JvmStatic
        @BindingAdapter("bind:textChangeListener")
        fun textChangeListener(view: EditText, textChangeListener: OnTextChangeListener?) {
            view.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    if (textChangeListener != null) {
                        textChangeListener!!.onTextChange(view, editable.toString())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }


    }
}