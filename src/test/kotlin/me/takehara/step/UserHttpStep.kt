package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.http.DemoApplicationDriver
import me.takehara.json.digString
import java.time.LocalDateTime
import java.util.*

class UserHttpStep {
    private val demoApplicationDriver = DemoApplicationDriver()

    @Step("ユーザ名<name>・パスワード<password>・メールアドレス<mailAddress>でユーザ新規作成のリクエストを送る")
    fun registerUser(name: String, password: String, mailAddress: String) {
        ValueStore.putUserName(name)
        ValueStore.putMailAddress(mailAddress)
        ValueStore.putLoginPassword(password)
        ValueStore.putTimeBeforeRequest(LocalDateTime.now())

        val url = "${DemoApplicationDriver.baseUrl}/users"
        val json = """ { "name": "$name", "password": "$password", "mailAddress": "$mailAddress" } """
        val (response, statusCode) = demoApplicationDriver.doPost(url, json)
        val userId = digString(response, "id")

        ValueStore.putTimeAfterRequest(LocalDateTime.now())
        ValueStore.putUserId(userId)
        ValueStore.putHttpStatusCode(statusCode)
    }

    @Step("既存のユーザがメールアドレス<mailAddress>を使っているときに、同じメールアドレスを使ってユーザ新規作成のリクエストを送る")
    fun registerUserWithConflictedMailAddress(mailAddress: String) {
        val random = UUID.randomUUID().toString().substring(0..8)
        val url = "${DemoApplicationDriver.baseUrl}/users"
        val json = """ { "name": "$random", "password": "$random", "mailAddress": "$mailAddress" } """
        val (response, statusCode) = demoApplicationDriver.doPost(url, json)

        ValueStore.putMailAddress(mailAddress)
        ValueStore.putHttpResponse(response)
        ValueStore.putHttpStatusCode(statusCode)
    }
}
