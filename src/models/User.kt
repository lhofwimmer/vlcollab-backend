package models

import io.ktor.http.cio.websocket.DefaultWebSocketSession
import managers.Globals
import managers.LobbyManager
import java.util.*

data class User(
    @Transient val session: DefaultWebSocketSession
) {
    init {
        Globals.clients.add(this)
    }

    val userId: String = "user${UUID.randomUUID()}"
    var displayName: String = "User"
    @Transient var currentLobby: Lobby? = null

    fun broadcastTargets(): List<User> {
        return currentLobby?.users ?: listOf(this)
    }

    fun joinLobby(lobbyId: String): Boolean = LobbyManager.addUserToLobby(userId, lobbyId)

    fun joinLobby(lobby: Lobby): Boolean = LobbyManager.addUserToLobby(this, lobby)

    fun leaveLobby(): Boolean = if (currentLobby != null) {
        currentLobby?.let { LobbyManager.removeUserFromLobby(this, it) }
        true
    } else {
        false
    }
}