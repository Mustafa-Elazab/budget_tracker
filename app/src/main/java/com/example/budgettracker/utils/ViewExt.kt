package com.example.budgettracker.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import okio.IOException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

//fun Fragment.changeMode(mSharedPref: SharedPreferences) {
//    val state = mSharedPref.getBoolean(NIGHT_MODE, true)
//    val mEditor = mSharedPref.edit()
//    mEditor.putBoolean(NIGHT_MODE, !state)
//    mEditor.apply()
//}



//fun Activity.getLastThemeMode(): Boolean {
//    val mSharedPref = getPreferences(Context.MODE_PRIVATE)
//    return mSharedPref?.getBoolean(NIGHT_MODE, true) ?: true
//}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Activity.transparentStatusBar() {
    val decor = window.decorView
    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    window.statusBarColor = Color.TRANSPARENT
}

inline fun View.snack(
    @StringRes string: Int,
    length: Int = Snackbar.LENGTH_LONG,
    action: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, resources.getString(string), length)
    action.invoke(snack)
    snack.show()
}
fun Fragment.getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}
fun Snackbar.action(
    @StringRes text: Int,
    color: Int? = null,
    listener: (View) -> Unit
) {
    setAction(text, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun TextInputEditText.transformIntoDatePicker(
    context: Context,
    format: String,
    maxDate: Date? = null
) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false

    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format, Locale.UK)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context,
            datePickerOnDataSetListener,
            myCalendar
                .get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            maxDate?.time?.also { datePicker.maxDate = it }
            show()
        }
    }
}

// currency converter
fun egyPound(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("EGP")
    return format.format(amount)
}

val String.cleanTextContent: String
    get() {
        // strips off all non-ASCII characters
        var text = this
        text = text.replace("[^\\x00-\\x7F]".toRegex(), "")

        // erases all the ASCII control characters
        text = text.replace("[\\p{Cntrl}&&[^\r\n\t]]".toRegex(), "")

        // removes non-printable characters from Unicode
        text = text.replace("\\p{C}".toRegex(), "")
        text = text.replace(",".toRegex(), "")
        return text.trim()
    }

// parse string to double
fun parseDouble(value: String?): Double {
    return if (value == null || value.isEmpty()) Double.NaN else value.toDouble()


}

