package me.takehara

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import java.time.LocalDateTime

// DataStore だと Gauge のクラスと命名が重複して入力補完が面倒なのであえて "Value" とした
// このままだと無限にメソッドが増えていくので UserValueStore 等に分割することも検討する
object ValueStore {
    fun getUserId() = ScenarioDataStore.get("userId") as String
    fun putUserId(value: String) = ScenarioDataStore.put("userId", value)

    fun getRegisteredAt() = ScenarioDataStore.get("registeredAt") as LocalDateTime
    fun putRegisteredAt(value: LocalDateTime) = ScenarioDataStore.put("registeredAt", value)

    fun getUserName() = ScenarioDataStore.get("userName") as String
    fun putUserName(value: String) = ScenarioDataStore.put("userName", value)

    fun getMailAddress() = ScenarioDataStore.get("mailAddress") as String
    fun putMailAddress(value: String) = ScenarioDataStore.put("mailAddress", value)

    fun getLoginId() = ScenarioDataStore.get("loginId") as String
    fun putLoginId(value: String) = ScenarioDataStore.put("loginId", value)

    fun getLoginPassword() = ScenarioDataStore.get("loginPassword") as String
    fun putLoginPassword(value: String) = ScenarioDataStore.put("loginPassword", value)

    fun getTimeBeforeRequest() = ScenarioDataStore.get("timeBeforeRequest") as LocalDateTime
    fun putTimeBeforeRequest(value: LocalDateTime) = ScenarioDataStore.put("timeBeforeRequest", value)

    fun getTimeAfterRequest() = ScenarioDataStore.get("timeAfterRequest") as LocalDateTime
    fun putTimeAfterRequest(value: LocalDateTime) = ScenarioDataStore.put("timeAfterRequest", value)

    fun getHttpStatusCode() = ScenarioDataStore.get("statusCode") as Int
    fun putHttpStatusCode(value: Int) = ScenarioDataStore.put("statusCode", value)

    fun getHttpResponse() = ScenarioDataStore.get("httpResponse") as String
    fun putHttpResponse(value: String) = ScenarioDataStore.put("httpResponse", value)
}
