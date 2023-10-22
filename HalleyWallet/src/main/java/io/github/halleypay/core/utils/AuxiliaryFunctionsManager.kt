package io.github.halleypay.core.utils

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.multidex.MultiDexApplication
import io.github.halleypay.data.dataSource.local.sharedPrefrences.SharedPreferencesManager
import io.github.halleypay.domain.entity.TypeLanguage

class AuxiliaryFunctionsManager(private val sharedPreferencesManager: SharedPreferencesManager) {

    fun getTypefaceBold(context: Context?): Typeface {
        return if (sharedPreferencesManager.getLanguageApp() == TypeLanguage.PERSIAN.typeLanguage) {
            return Typeface.createFromAsset(context!!.assets, "fonts/DanaFaNumBold.ttf")
        } else {
            return Typeface.createFromAsset(context!!.assets, "fonts/DanaEnNumBold.ttf")
        }
    }

    fun getTypefaceNormal(context: Context?): Typeface {
        return if (sharedPreferencesManager.getLanguageApp() == TypeLanguage.PERSIAN.typeLanguage) {
            Typeface.createFromAsset(context!!.assets, "fonts/DanaFaNumRegular.ttf")
        } else {
            Typeface.createFromAsset(context!!.assets, "fonts/DanaEnNumRegular.ttf")
        }
    }


    fun getVersionAndroid(): String {
        val builder = StringBuilder()
        builder.append("Android ").append(Build.VERSION.RELEASE)
        val fields = VERSION_CODES::class.java.fields
        for (field in fields) {
            val fieldName = field.name
            var fieldValue = -1
            try {
                fieldValue = field.getInt(Any())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ")
                builder.append("sdk=").append(fieldValue)
            }
        }
        return builder.toString()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(MultiDexApplication.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}