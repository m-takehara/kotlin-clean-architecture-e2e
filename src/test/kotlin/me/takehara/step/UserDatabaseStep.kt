package me.takehara.step

import com.thoughtworks.gauge.Step
import me.takehara.ValueStore
import me.takehara.database.UserAuths
import me.takehara.database.UserDbDriver
import me.takehara.database.UserProfiles
import me.takehara.database.Users
import me.takehara.json.Json
import me.takehara.json.getString
import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UserDatabaseStep {
    @Step("レスポンスのJSONのパス<jsonPath>の値が主キーであるレコードがusersテーブルに格納された")
    fun assertUsersTableContains(jsonPath: String) {
        val id = ValueStore.getHttpResponse().let { Json.getString(it, jsonPath) }
        transaction {
            val select = Users.select { Users.id eq id }
            select.any() shouldBeEqualTo true
        }
    }

    @Step("レスポンスのJSONのパス<jsonPath>の値が主キーであるレコードがuser_authsテーブルに格納された")
    fun assertUserAuthsTableContains(jsonPath: String) {
        val id = ValueStore.getHttpResponse().getString(jsonPath)
        transaction {
            val select = UserAuths.select { UserAuths.userId eq id }
            select.any() shouldBeEqualTo true
        }
    }

    @Step("レスポンスのJSONのパス<jsonPath>の値が主キーであるレコードがuser_profilesテーブルに格納された")
    fun assertUserProfilesContains(jsonPath: String) {
        val id = ValueStore.getHttpResponse().let { Json.getString(it, jsonPath) }
        transaction {
            val select = UserProfiles.select { UserProfiles.userId eq id }
            select.any() shouldBeEqualTo true
        }
    }

    @Step("DBのusersテーブルの列registered_atに登録日時を登録できた")
    fun assertUsersContainsCurrentDateTime() {
        val id = ValueStore.getHttpResponse().getString("$.id")
        val registeredAt = ValueStore.getHttpResponse().getString("$.registeredAt")
            .let { LocalDateTime.parse(it) }

        transaction {
            val user = Users.select { Users.id eq id }
            user.any() shouldBeEqualTo true
            user.single()[Users.registeredAt] shouldBeEqualTo registeredAt
        }
    }

    @Step("DBのuser_authsテーブルの列<column>に値<value>を登録できた")
    fun assertUserAuthsContainsValue(column: String, value: String) {
        TODO()
    }

    @Step("DBのuser_authsテーブルの列<column>に値<value>をハッシュ化したものを登録できた")
    fun assertUserAuthsContainsHashedValue(column: String, value: String) {
        TODO()
    }

    @Step("DBのuser_profilesテーブルの列<column>に値<value>を登録できた")
    fun assertUserProfilesContainsValue(column: String, value: String) {
        TODO()
    }

    @Step("DBのテーブル<tableNames>が初期状態<csvFolderPath>から変化していない")
    fun assertDatabaseUnchanged(tableNames: String, csvFolderPath: String) {
        val names = tableNames.split(",")
        UserDbDriver.assertTableEquals(names, csvFolderPath)
    }
}
