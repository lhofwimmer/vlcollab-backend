import commands.CommandFactory
import managers.Globals.clients
import models.User
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start()
}

fun Application.module() {
    log.info("Starting server")

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

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
                            val req = handleRequest(client, text)
                            send(req.toJsonFrame())
                        }
                    }
                }
            }


        }
    }
}

fun handleRequest(user: User, json: String) = CommandFactory(json, user).produceCommand()