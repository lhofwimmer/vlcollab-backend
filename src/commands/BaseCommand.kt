package commands

import com.google.gson.Gson
import managers.Json
import models.CommandToClient
import kotlin.reflect.KFunction

abstract class BaseCommand(open val callId: String) {
    companion object {
        //functions get stored in companion object as they don't need to be calculated with every command creation.
        var commandMap: MutableMap<String, KFunction<CommandToClient>> = mutableMapOf()
    }

    fun toJson(): String {
        return Json.toJson(this)
    }

    override fun toString() = toJson()

    abstract fun executeCommand(): CommandToClient

    fun toClient(obj: Any) = CommandToClient(callId, Json.toJson(obj))
}