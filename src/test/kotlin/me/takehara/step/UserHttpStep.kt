package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.http.DemoApplicationDriver
import java.util.*

class UserHttpStep {
    @Step("URL<urlPath>に対してPOSTリクエスト<jsonString>を実行する")
    fun registerUser(urlPath: String, jsonString: String) {
        val (response, statusCode) = DemoApplicationDriver.doPost(urlPath, jsonString)
        ValueStore.putHttpResponse(response)
        ValueStore.putHttpStatusCode(statusCode)
        println(response)
    }

    @Step("既存のユーザがメールアドレス<mailAddress>を使っているときに、同じメールアドレスを使ってユーザ新規作成のリクエストを送る")
    fun registerUserWithConflictedMailAddress(mailAddress: String) {
        val random = UUID.randomUUID().toString().substring(0..8)
        val json = """ { "name": "$random", "password": "$random", "mailAddress": "$mailAddress" } """
        val (response, statusCode) = DemoApplicationDriver.doPost("/users", json)

        ValueStore.putHttpResponse(response)
        ValueStore.putHttpStatusCode(statusCode)
    }
}
