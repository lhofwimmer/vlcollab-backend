package commands

import models.CommandToClient
import models.Lobby
import models.User

class EventCommand(
    private val user: User,
    override val callId: String,
    private val event: String,
    private val data: List<String>
) : BaseCommand(callId) {

    override fun executeCommand(): CommandToClient = when(event) {
        "joinLobby" -> joinLobby()
        "createLobby" -> createLobby()
        "changeUsername" -> changeUsername()
        else -> throw IllegalCommandException("${user.displayName} issued command <$event> which is not a valid method.")
    }

    /**
     * adds user to a lobby:
     * expected Params:
     *  [0]lobbyId: String
     *  [1]userId: String
     *
     *  @return if successful: lobby object, else false
     */
    private fun joinLobby(): CommandToClient {
        val lobbyId = data[1]
        user.joinLobby(lobbyId)
        return toClient(user, user.currentLobby ?: "false")
    }

    /**
     * Leave the current lobby. Will not do anything if user is not in a lobby.
     * 
     * @return A message indicating the leaveLobby method has been called. Nothing else is provided as information is sufficient 
     */
    private fun leaveLobby(): CommandToClient {
        // val lobbyId = data[1]
        user.leaveLobby()
        return toClient(user, "[EVENT::leaveLobby]")
    }

    /**
     * creates a new lobby with a given name
     * expected Params:
     *  [0]displayName: String
     *
     *  example json:
     *  {
     *      "callId": "asdf",
     *      "type": "EVENT",
     *      "event": "createLobby",
     *       "data": [
     *          "cool lobby name"
     *       ]
     *   }
     *
     *  @return if successful: lobby object of created lobby
     */
    private fun createLobby(): CommandToClient {
        val displayName = data[0]
        val lobby = Lobby(displayName)
        user.joinLobby(lobby)
        println("Lobby created with name $displayName")
        return toClient(user, lobby)
    }

    /**
     * Changes displayName of given user
     * expected Params:
     *  [0]displayName: String
     *
     * restrictions: new name cannot be more than 50 characters long
     *
     * @return if successful: new name, else the previous name
     */
    private fun changeUsername(): CommandToClient {
        val displayName = data[0]
        return if (displayName.length < 50) {
            user.displayName = displayName
            toClient(user, displayName)
        } else {
            toClient(user, user.displayName)
        }

    }
}