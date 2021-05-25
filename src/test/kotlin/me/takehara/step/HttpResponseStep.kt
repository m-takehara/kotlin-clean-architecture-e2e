package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.json.digString
import org.amshove.kluent.shouldBeEqualTo

class HttpResponseStep {
    @Step("HTTPステータスコード<code>が返された")
    fun verifyHttpStatusCode(code: Int) {
        ValueStore.getHttpStatusCode() shouldBeEqualTo code
    }

    @Step("エラーメッセージ<message>が返された")
    fun verifyErrorMessage(message: String) {
        val actualMessage = ValueStore.getHttpResponse().let { digString(it, "error") }
        actualMessage shouldBeEqualTo message
    }
}
