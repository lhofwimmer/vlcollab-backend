package commands

import managers.Globals
import models.CommandToClient
import models.User

class GetCommand(
    private val user: User,
    override val callId: String,
    private val event: String,
    private val data: List<String>
) : BaseCommand(callId) {

    override fun executeCommand(): CommandToClient = when(event) {
        "getLobbies" -> getLobbies()
        else -> throw IllegalCommandException("${user.displayName} issued command <$event> which is not a valid method.")
    }

    /**
     * paginated return of lobbies
     * expected data:
     *  start: integer representing first element to return, starts at 1
     *  size: integer representing page size
     */
    fun getLobbies(): CommandToClient {
        val start = data[0].toInt()
        val size = data[1].toInt()
        val lobbies = Globals.lobbies.slice(start..size)
        return toClient(user, lobbies)
    }

}