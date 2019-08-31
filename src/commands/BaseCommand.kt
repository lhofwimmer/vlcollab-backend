package commands

import managers.Json
import models.CommandToClient
import models.User

abstract class BaseCommand(open val callId: String) {
    abstract fun executeCommand(): CommandToClient

    inline fun <reified T>toClient(user: User, obj: T) =
        CommandToClient(user, callId, Json.toJson(obj))
}