package models

import managers.Globals
import java.util.*

data class Lobby(
    var displayName: String
) {
    init {
        Globals.lobbies.add(this)
    }

    val lobbyId: String = "lobby${UUID.randomUUID()}"
    val users: MutableList<User> = Collections.synchronizedList(mutableListOf())

    fun iterateOverLobbyMembers(id: String, callback: (User) -> Unit) {
        users.forEach{
            callback(it)
        }
    }

    fun removeUser(user: User) = users.remove(user)

    fun removeUser(userId: String) = users.remove(users.find { it.userId == userId })
}