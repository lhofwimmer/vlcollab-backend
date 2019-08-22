package commands

import models.CommandToClient
import models.User
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

class MediaPlayerCommand(
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
        return (commandMap[event]?.call(data) ?: CommandToClient(callId, "")) as CommandToClient
    }
}