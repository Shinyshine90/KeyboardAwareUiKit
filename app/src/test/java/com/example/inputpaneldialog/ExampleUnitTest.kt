package com.example.inputpaneldialog

import org.junit.Test

import org.junit.Assert.*
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
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

    }
}