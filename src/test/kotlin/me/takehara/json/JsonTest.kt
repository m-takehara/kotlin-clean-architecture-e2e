package me.takehara.json

import org.amshove.kluent.AnyException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow

// me.takehara.json.JsonKt の動作をテストするための簡易テストケース
internal fun main() {
    val json = """{
            "foo1": {
                "foo2": 100,
                "foo3": {
                    "foo4": "foo5",
                    "foo6": "2021-05-23T04:30:00Z"
                }
            },
            "bar": "baz"
        }""".trimIndent()

    digInt(json, "foo1.foo2") shouldBeEqualTo 100
    digString(json, "foo1.foo3.foo4") shouldBeEqualTo "foo5"
    digString(json, "foo1.foo3.foo6") shouldBeEqualTo "2021-05-23T04:30:00Z"
    digString(json, "bar") shouldBeEqualTo "baz"

    invoking { digString(json, "foo1") } shouldThrow AnyException
    invoking { digString(json, "fooXXXX") } shouldThrow AnyException

    println("Test succeed!")
}
