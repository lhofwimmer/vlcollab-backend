package managers

import models.Lobby
import models.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LobbyManager {

    private val logger: Logger = LoggerFactory.getLogger("LobbyManager")

    private fun getLobby(lobbyId: String) = Globals.lobbies.first { it.lobbyId == lobbyId }
    private fun getUser(userId: String) = Globals.clients.first { it.userId == userId }

    fun addUserToLobby(userId: String, lobbyId: String): Boolean {
        return try {
            val user = getUser(userId)
            val lobby = getLobby(lobbyId)

            lobby.users.add(user)
            user.currentLobby = lobby
            true
        } catch (ex: Exception) {
            logger.error(ex.stackTrace.toString())
            false
        }
    }

    fun addUserToLobby(user: User, lobby: Lobby): Boolean = addUserToLobby(user.userId, lobby.lobbyId)

    fun removeUserFromLobby(userId: String, lobbyId: String): Boolean {
        return try {
            val user = getUser(userId)
            val lobby = getLobby(lobbyId)

            lobby.users.remove(user)
            user.currentLobby = null
            true
        } catch (ex: Exception) {
            logger.error(ex.stackTrace.toString())
            false
        }
    }

    fun removeUserFromLobby(user: User, lobby: Lobby): Boolean = removeUserFromLobby(user.userId, lobby.lobbyId)
}