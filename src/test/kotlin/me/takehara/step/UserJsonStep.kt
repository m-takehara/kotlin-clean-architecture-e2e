package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.json.Json
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeLessThan
import org.amshove.kluent.shouldMatch
import org.intellij.lang.annotations.Language
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserJsonStep {
    @Step("レスポンスのJSONのパス<jsonPath>の値がUUID形式である")
    fun assertJsonContainsUuid(jsonPath: String) {
        @Language("RegExp")
        val expectedPattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

        val actual = Json.getString(ValueStore.getHttpResponse(), jsonPath)
        ValueStore.putUserId(actual)
        actual shouldMatch expectedPattern
    }

    @Step("レスポンスのJSONのパス<jsonPath>の値が過去<time>秒以内の日時データである")
    fun assertJsonContainsDateTime(jsonPath: String, time: Int) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val expected = LocalDateTime.now().plusSeconds(time.toLong())

        val actual = Json.getString(ValueStore.getHttpResponse(), jsonPath).let { LocalDateTime.parse(it) }
        ValueStore.putRegisteredAt(actual)
        actual shouldBeLessThan expected
    }

    @Step("レスポンスのJSONのパス<jsonPath>の値が<expected>である")
    fun assertJsonContainsValue(jsonPath: String, expected: String) {
        val actual = Json.getString(ValueStore.getHttpResponse(), jsonPath)
        actual shouldBeEqualTo expected
    }
}
