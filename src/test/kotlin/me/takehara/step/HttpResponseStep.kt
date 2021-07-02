package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import org.amshove.kluent.shouldBeEqualTo

class HttpResponseStep {
    @Step("HTTPステータスコード<code>が返された")
    fun verifyHttpStatusCode(code: Int) {
        ValueStore.getHttpStatusCode() shouldBeEqualTo code
    }
}
