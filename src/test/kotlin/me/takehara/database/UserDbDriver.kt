package me.takehara.database

import com.thoughtworks.gauge.datastore.ScenarioDataStore
import me.takehara.Configuration.Companion.config
import me.takehara.Configuration.database
import org.amshove.kluent.shouldBeAfter
import org.amshove.kluent.shouldBeBefore
import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UserDbDriver(
    private val db: Database = Database.connect(
        url = config[database.url],
        driver = config[database.driver],
        user = config[database.user],
        password = config[database.password]
    )
) {
    fun assertUserTable(id: String) = transaction(db) {
        val user = Users.select { Users.id eq id }.single()
        val registeredAt = user[Users.registeredAt]

        registeredAt shouldBeAfter (ScenarioDataStore.get("timeBeforeRequest") as LocalDateTime)
        registeredAt shouldBeBefore (ScenarioDataStore.get("timeAfterRequest") as LocalDateTime)
    }

    fun assertUserAuthTable(id: String, mailAddress: String, password: String) = transaction(db) {
        val auth = UserAuths.select { UserAuths.userId eq id }.single()
        val loginId = auth[UserAuths.loginId]
        val loginPassword = auth[UserAuths.loginPassword]

        loginId shouldBeEqualTo mailAddress
        loginPassword shouldBeEqualTo password
    }

    fun assertUserProfileTable(id: String, name: String, mailAddress: String) = transaction(db) {
        val profile = UserProfiles.select { UserProfiles.userId eq id }.single()
        val userName = profile[UserProfiles.name]
        val userMailAddress = profile[UserProfiles.mailAddress]

        userName shouldBeEqualTo name
        userMailAddress shouldBeEqualTo mailAddress
    }

    fun deleteUserIfExistsBy(mailAddress: String) = transaction(db) {
        val profile = UserProfiles.select { UserProfiles.mailAddress eq mailAddress }
        if (profile.any()) {
            val id = profile.single()[UserProfiles.userId]
            UserAuths.deleteWhere { UserAuths.userId eq id }
            UserProfiles.deleteWhere { UserProfiles.userId eq id }
            Users.deleteWhere { Users.id eq id }
        }
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
