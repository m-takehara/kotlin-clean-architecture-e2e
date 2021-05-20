package me.takehara.step

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import me.takehara.Configuration.Companion.config
import me.takehara.Configuration.application
import me.takehara.database.UserDbDriver
import me.takehara.http.doPost
import org.json.JSONObject
import java.time.LocalDateTime

class UserSteps {
    private val userDbDriver = UserDbDriver()
    private val endpoint = config[application.endpoint]

    @Step("もしメールアドレス<mailAddress>を含むユーザが存在する場合は削除する")
    fun deleteUserIfExists(mailAddress: String) {
        userDbDriver.deleteUserIfExistsBy(mailAddress)
    }

    @Step("ユーザ名<name>・パスワード<password>・メールアドレス<mailAddress>でユーザ新規作成のリクエストを送る")
    fun createUser(name: String, password: String, mailAddress: String) {
        ScenarioDataStore.put("timeBeforeRequest", LocalDateTime.now())

        val url = "$endpoint/users"
        val json = """ { "name": "$name", "password": "$password", "mailAddress": "$mailAddress" } """
        val (response, status) = doPost(url, json)

        ScenarioDataStore.put("timeAfterRequest", LocalDateTime.now())
        ScenarioDataStore.put("id", JSONObject(response).get("id").toString())
        ScenarioDataStore.put("name", name)
        ScenarioDataStore.put("password", password)
        ScenarioDataStore.put("mailAddress", mailAddress)
        ScenarioDataStore.put("statusCode", status)
        ScenarioDataStore.put("responseBody", response)
    }

    @Step("ユーザの登録日をDBに登録できた")
    fun verifyUsersTable() {
        val id = ScenarioDataStore.get("id").toString()
        userDbDriver.assertUserTable(id)
    }

    @Step("メールアドレスをユーザIDの初期値として認証情報をDBに登録できた")
    fun verifyUserAuthsTable() {
        val id = ScenarioDataStore.get("id").toString()
        val mailAddress = ScenarioDataStore.get("mailAddress").toString()
        val password = ScenarioDataStore.get("password").toString()
        userDbDriver.assertUserAuthTable(id, mailAddress, password)
    }

    @Step("ユーザ名とメールアドレスをDBに登録できた")
    fun verifyUserProfilesTable() {
        val id = ScenarioDataStore.get("id").toString()
        val name = ScenarioDataStore.get("name").toString()
        val mailAddress = ScenarioDataStore.get("mailAddress").toString()
        userDbDriver.assertUserProfileTable(id, name, mailAddress)
    }
}
