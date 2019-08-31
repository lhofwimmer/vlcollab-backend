package managers

import models.Lobby
import models.User
import java.util.*
import kotlin.collections.LinkedHashSet

object Globals {
    val clients: MutableSet<User> = Collections.synchronizedSet(LinkedHashSet<User>())
    val lobbies: MutableList<Lobby> = Collections.synchronizedList(mutableListOf<Lobby>())
}