import kotlinx.serialization.Serializable
import io.ktor.client.request.*
import io.ktor.http.*

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
    require(it != null) { "WEBHOOK_URL environment variable not set" }
}

@Serializable
private data class Payload(val content: String)

object Notifier {
    suspend fun send(message: String) {
        client.post(WEBHOOK_URL) {
            setBody(Payload(message))
        }.also {
            check(it.status.value == HttpStatusCode.NoContent.value) { "Failed to send notification: ${it.status}" }
        }
    }
}
