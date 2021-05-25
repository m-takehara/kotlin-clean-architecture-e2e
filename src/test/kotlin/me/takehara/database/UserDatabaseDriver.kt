package me.takehara.database

import me.takehara.ValueStore
import org.amshove.kluent.shouldBeAfter
import org.amshove.kluent.shouldBeBefore
import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UserDbDriver {
    private val driver = DatabaseDriver()

    fun assertUserRecordEquals(id: String, timeBeforeRequest: LocalDateTime, timeAfterRequest: LocalDateTime) =
        transaction(driver.database) {
            val user = Users.select { Users.id eq id }.single()
            val registeredAt = user[Users.registeredAt]

            registeredAt shouldBeAfter timeBeforeRequest
            registeredAt shouldBeBefore timeAfterRequest
        }

    fun assertUserRecordEquals(id: String, registeredAt: LocalDateTime) =
        transaction(driver.database) {
            val user = Users.select { Users.id eq id }.single()
            user[Users.registeredAt] shouldBeEqualTo registeredAt
        }

    fun assertUserAuthRecordEquals(id: String, loginId: String, loginPassword: String) = transaction(driver.database) {
        val auth = UserAuths.select { UserAuths.userId eq id }.single()
        val actualId = auth[UserAuths.loginId]
        val actualPassword = auth[UserAuths.loginPassword]

        actualId shouldBeEqualTo loginId
        actualPassword shouldBeEqualTo loginPassword
    }

    fun assertUserProfileRecordEquals(id: String, name: String, mailAddress: String) = transaction(driver.database) {
        val profile = UserProfiles.select { UserProfiles.userId eq id }.single()
        val userName = profile[UserProfiles.name]
        val userMailAddress = profile[UserProfiles.mailAddress]

        userName shouldBeEqualTo name
        userMailAddress shouldBeEqualTo mailAddress
    }

    fun getUserIdByMailAddress(mailAddress: String): String = transaction(driver.database) {
        UserProfiles.select { UserProfiles.mailAddress eq mailAddress }.single()[UserProfiles.userId]
    }

    fun putUserRecordToValueStore(id: String) = transaction(driver.database) {
        val user = Users.select { Users.id eq id }.single()
        ValueStore.putUserId(id)
        ValueStore.putRegisteredAt(user[Users.registeredAt])
    }

    fun putUserAuthRecordToValueStore(id: String) = transaction(driver.database) {
        val auth = UserAuths.select { UserAuths.userId eq id }.single()
        ValueStore.putLoginId(auth[UserAuths.loginId])
        ValueStore.putLoginPassword(auth[UserAuths.loginPassword])
    }

    fun putUserProfileRecordToValueStore(id: String) = transaction(driver.database) {
        val profile = UserProfiles.select { UserProfiles.userId eq id }.single()
        ValueStore.putUserName(profile[UserProfiles.name])
        ValueStore.putMailAddress(profile[UserProfiles.mailAddress])
    }
}

object Users : Table("demo_schema.users") {
    val id = varchar("id", 36)
    val registeredAt = datetime("registered_at")

    override val primaryKey = PrimaryKey(id)
}

object UserProfiles : Table("demo_schema.user_profiles") {
    val userId = varchar("user_id", 36) references Users.id
    val name = varchar("name", 256)
    val mailAddress = varchar("mail_address", 256)

    override val primaryKey = PrimaryKey(userId)
}

object UserAuths : Table("demo_schema.user_auths") {
    val userId = varchar("user_id", 36) references Users.id
    val loginId = varchar("login_id", 256)
    val loginPassword = varchar("login_password", 256)

    override val primaryKey = PrimaryKey(userId)
}
