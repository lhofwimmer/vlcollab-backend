package models

import io.ktor.http.cio.websocket.DefaultWebSocketSession
import managers.Globals
import managers.LobbyManager
import java.util.*

data class User(
    val session: DefaultWebSocketSession
) {
    init {
        Globals.clients.add(this)
    }

    val userId: String = "user${UUID.randomUUID()}"
    var displayName: String = "User"
    var currentLobby: Lobby? = null

    fun broadcastToOtherLobbyMembers(callback: (User) -> Unit) {
        currentLobby?.users
            ?.filter { it.userId != userId }
            ?.forEach(callback)
    }

    fun joinLobby(lobbyId: String): Boolean = LobbyManager.addUserToLobby(userId, lobbyId)

    fun joinLobby(lobby: Lobby): Boolean = LobbyManager.addUserToLobby(this, lobby)

    fun leaveLobby(): Boolean  = if(currentLobby != null) {
        currentLobby?.let { LobbyManager.removeUserFromLobby(this, it) }
        true
    } else {
        false
    }
}