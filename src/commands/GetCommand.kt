package commands

import managers.Globals
import models.CommandToClient
import models.User
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

class GetCommand(
    private val user: User,
    override val callId: String,
    private val event: String,
    private val data: List<String>
) : BaseCommand(callId) {
    init {
        if (commandMap.isEmpty()) {
            this::class.functions
                .filterIsInstance<KFunction<CommandToClient>>()
                .forEach {
                    @Suppress("UNCHECKED_CAST")
                    commandMap[it.name] = it
                }
        }
    }

    override fun executeCommand(): CommandToClient {
        return commandMap[event]?.call(*data.toTypedArray()) ?: CommandToClient(callId, "")
    }

    /**
     * paginated return of lobbies
     * expected data:
     *  start: integer representing first element to return, starts at 1
     *  size: integer representing page size
     */
    fun getLobbies(data: List<String>): CommandToClient {
        val start = data[0].toInt()
        val size = data[1].toInt()
        val lobbies = Globals.lobbies.slice(start..size)
        return toClient(lobbies)
    }

}