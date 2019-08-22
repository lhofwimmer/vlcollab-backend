package commands

import managers.Json
import models.CommandToClient
import models.Lobby
import models.User
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

class EventCommand(
    private val user: User,
    override val callId: String,
    private val event: String,
    private val data: List<String>
) : BaseCommand(callId) {

    init {
        if (commandMap.isEmpty()) {
            val clazz = this::class.java.classLoader.loadClass(this::class.qualifiedName)
            val cm: MutableMap<String, Method> = mutableMapOf()

            clazz.methods
                .forEach {
                    cm[it.name] = it
            }
            cm[event]?.invoke(this, *data.toTypedArray())

            this::class.functions
                .filterIsInstance<KFunction<CommandToClient>>()
                .forEach {
                    @Suppress("UNCHECKED_CAST")
                    commandMap[it.name] = it
                }
        }
    }

    override fun executeCommand(): CommandToClient {
        println(commandMap[event])
        return commandMap[event]?.call(*data.toTypedArray()) ?: CommandToClient(callId, "")
    }

    /**
     * adds user to a lobby:
     * expected Params:
     *  [0]lobbyId: String
     *  [1]userId: String
     *
     *  @return if successful: lobby object, else false
     */
    private fun joinLobby(data: List<String>): CommandToClient {
        val lobbyId = data[1]
        user.joinLobby(lobbyId)
        return toClient(user.currentLobby ?: "false")
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
    private fun createLobby(data: List<String>): CommandToClient {
        val displayName = data[0]
        val lobby = Lobby(displayName)
        user.joinLobby(lobby)
        println("Lobby created with name $displayName")
        return toClient(lobby)
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
    private fun changeUsername(data: List<String>): CommandToClient {
        val displayName = data[0]
        return if (displayName.length > 50) {
            user.displayName = displayName
            toClient(displayName)
        } else {
            toClient(user.displayName)
        }

    }
}