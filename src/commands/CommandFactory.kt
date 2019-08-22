package commands

import managers.Json
import models.CommandToClient
import models.CommandToServer
import models.User
import org.slf4j.LoggerFactory
import java.lang.Exception

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
    private val json: String,
    private val user: User
) {
    private val commandToServer: CommandToServer
        get() {
            return try {
                Json.fromJson(json, CommandToServer::class.java)
            } catch (ex: Exception) {
                logger.error(ex.stackTrace.toString())
                throw IllegalCommandException(ex.stackTrace.toString())
            }
        }

    private val logger = LoggerFactory.getLogger("CommandFactory")

    fun produceCommand(): CommandToClient {
        return with(commandToServer) {
            when (type) {
                //route user-sent data to correct class for execution
                "EVENT" -> EventCommand(user, callId, event, data).executeCommand()
                "EXECUTE" -> MediaPlayerCommand(user, callId, event, data).executeCommand()
                "GET" -> GetCommand(user, callId, event, data).executeCommand()
                else -> throw IllegalCommandException("<$type> is not a valid type")
            }
        }
    }
}