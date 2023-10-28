package org.example.database

import org.example.telegram.TelegramUserId
import org.example.users.UserId
import org.example.users.UserProvider
import org.example.users.UserSession
import org.example.users.UserState
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserSessions : IntIdTable() {
    val state = enumeration("state", UserState::class).default(UserState.NEW_BEE)
    val name = varchar("name", 32).default("")
    val profile = varchar("profile", 256).default("")
}

object TelegramUsers : LongIdTable() {
    //val id = long("id")
    val userId = reference("userId", UserSessions)
    //override val primaryKey = PrimaryKey(id, name = "PK_Cities_ID")
}

class DatabaseUserSession(id: EntityID<Int>) : IntEntity(id), UserSession {
    companion object : IntEntityClass<DatabaseUserSession>(UserSessions)

    private var _state by UserSessions.state
    private var _name by UserSessions.name
    private var _profile by UserSessions.profile
    override fun getState() = transaction {  _state}
    override fun setState(state: UserState) = transaction { _state = state }
    override fun getName() =transaction {  _name}
    override fun setName(name: String) = transaction { _name = name }
    override fun getProfile() = transaction { _profile }
    override fun setProfile(profile: String) = transaction { _profile = profile }
}

object DatabaseUserProvider:UserProvider {
    init {
        Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UserSessions, TelegramUsers)
        }
    }
    override fun findUserById(userId: UserId): UserSession {
        if (userId is TelegramUserId) {
            return transaction {
                addLogger(StdOutSqlLogger)
                val id = TelegramUsers
                    .select { TelegramUsers.id.eq(userId.id) }
                    .firstOrNull()?.get(TelegramUsers.userId)
                if (id != null) {
                    DatabaseUserSession.findById(id) ?: throw IllegalStateException("UserSession not found")
                } else {
                    val newSession = DatabaseUserSession.new {}
                    TelegramUsers.insert {
                        it[TelegramUsers.id] = userId.id
                        it[TelegramUsers.userId] = newSession.id.value
                    }
                    newSession
                }
            }
        }
        throw IllegalStateException("Only Telegram support now")
    }

}