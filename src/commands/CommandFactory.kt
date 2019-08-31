package commands

import models.CommandToClient
import models.CommandToServer
import models.User

/**
 *example json:
 *  {
 *      "callId": "asdf",
 *      "type": "EVENT",
 *      "event": "createLobby",
 *       "data": [
 *          "cool lobby name"
 *       ]
 *   }
 */
class CommandFactory(
    private val commandToServer: CommandToServer,
    private val user: User
) {
    fun produceCommand(): CommandToClient {
        return with(commandToServer) {
            when (type) {
                //route user-sent data to correct class for execution
                "EVENT" -> EventCommand(user, callId, event, data).executeCommand()
                "EXECUTE" -> MediaPlayerCommand(user, callId, event, data).executeCommand()
                "GET" -> GetCommand(user, callId, event, data).executeCommand()
                else -> throw IllegalCommandException("${user.displayName} issued command <$event> which is not a valid method.")
            }
        }
    }
}