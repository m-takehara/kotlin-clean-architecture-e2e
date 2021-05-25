package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.database.UserDbDriver
import java.security.MessageDigest

class UserDatabaseStep {
    private val userDbDriver = UserDbDriver()

    @Step("ユーザの登録日をDBに登録できた")
    fun verifyUsersTable() {
        val id = ValueStore.getUserId()
        val timeBeforeRequest = ValueStore.getTimeBeforeRequest()
        val timeAfterRequest = ValueStore.getTimeAfterRequest()
        userDbDriver.assertUserRecordEquals(id, timeBeforeRequest, timeAfterRequest)
    }

    @Step("ログインIDはメールアドレスが初期値となり、パスワードはハッシュ化され、DBに登録できた")
    fun verifyUserAuthsTableWithEncryptedPassword() {
        val id = ValueStore.getUserId()
        val mailAddress = ValueStore.getMailAddress()
        val password = ValueStore.getLoginPassword().let(::encryptPassword)
        userDbDriver.assertUserAuthRecordEquals(id, mailAddress, password)
    }

    @Step(
        "ユーザ名とメールアドレスをDBに登録できた",
        "既存のユーザのユーザ名とメールアドレスは変更されていない"
    )
    fun verifyUserProfilesTable() {
        val id = ValueStore.getUserId()
        val name = ValueStore.getUserName()
        val mailAddress = ValueStore.getMailAddress()
        userDbDriver.assertUserProfileRecordEquals(id, name, mailAddress)
    }

    @Step("メールアドレス<mailAddress>を使用中のユーザの情報を保存しておく")
    fun registerUserWithConflictedMailAddress(mailAddress: String) {
        val id = userDbDriver.getUserIdByMailAddress(mailAddress)
        userDbDriver.putUserRecordToValueStore(id)
        userDbDriver.putUserAuthRecordToValueStore(id)
        userDbDriver.putUserProfileRecordToValueStore(id)
    }

    @Step("既存のユーザの登録日時は変更されていない")
    fun verifyUserUnchanged() {
        val id = ValueStore.getUserId()
        userDbDriver.assertUserRecordEquals(id, ValueStore.getRegisteredAt())
    }

    @Step("既存のユーザのログインIDとログインパスワードは変更されていない")
    fun verifyUserAuthsTable() {
        val id = ValueStore.getUserId()
        val loginId = ValueStore.getLoginId()
        val password = ValueStore.getLoginPassword()
        userDbDriver.assertUserAuthRecordEquals(id, loginId, password)
    }

    private fun encryptPassword(password: String): String {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}
