package commands

import models.CommandToClient
import models.User

class MediaPlayerCommand(
    private val user: User,
    override val callId: String,
    private val event: String,
    private val data: List<String>
) : BaseCommand(callId) {
    override fun executeCommand(): CommandToClient = when(event) {
        "stopPlayer" -> stopPlayer()
        "startPlayer" -> startPlayer()
        "moveTimestamp" -> moveTimestamp()
        else -> throw IllegalCommandException("${user.displayName} issued command <$event> which is not a valid method.")
    }

    fun stopPlayer(): CommandToClient {

        return toClient(user, "")
    }

    fun startPlayer(): CommandToClient {
        return toClient(user, "")
    }

    fun moveTimestamp(): CommandToClient {
        return toClient(user, "")
    }
}