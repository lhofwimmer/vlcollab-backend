import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.http.cio.websocket.*
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import commands.CommandFactory
import managers.Globals.clients
import managers.Json
import models.CommandToServer
import models.User
import java.time.Duration
import kotlin.run

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start()
}

@Suppress("unused")
fun Application.module() {
    log.info("Starting server")

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/vlcollab") {
            val client = User(this)
            clients.add(client)
            log.info("Client connected with id <${client.userId}>")

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    when (val text = frame.readText()) {
                        "disconnect" -> {
                            close(
                                CloseReason(
                                    CloseReason.Codes.NORMAL,
                                    "Session closed; id: <${client.userId}>; displayName: <${client.displayName}>"
                                )
                            )
                            log.info("removing client with id: <${client.userId}>")
                        }
                        else -> {
                            try {
                                val command = Json.fromJson<CommandToServer>(text)
                                if (command != null) {
                                    val req = handleRequest(client, command)

                                    req.user.broadcastTargets().forEach { user ->
                                        run {
                                            user.session.send(req.toJsonFrame())
                                        }
                                    }
                                }
                            } catch (ex: Exception) {
                                log.error(ex.printStackTrace().toString())
                                client.session.send("Unable to process message")
                            }
                        }//else
                    }//when
                }//if
            }//for
        }//webSocket
    }
}

fun handleRequest(user: User, command: CommandToServer) = CommandFactory(command, user).produceCommand()
