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

    //video position in milliseconds
    var currentTimestamp: Int = -1

    //video title shown in lobby list for example
    var videoTitle: String = ""

    //total length of current lobby video
    var totalVideoLength: Int = -1

    fun removeUser(user: User) = users.remove(user)

    fun removeUser(userId: String) = users.remove(users.find { it.userId == userId })
}