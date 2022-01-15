package com.example.inputpaneldialog


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val pattern = Pattern.compile("/[1-9]\\d*\\?")
        val matcher = pattern.matcher("https://nj-phx.yunxuetang.com.cn/m/tlive/#/tlivedetail/22385?og=d59e702e-14b8-42f0-bb72-677487d09730")
        if (matcher.find()) {
            val matched = matcher.group().run {
                substring(1, length - 1)
            }
            println("matched: $matched")
        } else {
            println("no match")
        }
        val (n,m) = A()
    }
}

data class A(
    val a:String = "",
    val b:String = ""
)