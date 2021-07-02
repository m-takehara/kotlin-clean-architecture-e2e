package me.takehara.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime

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
