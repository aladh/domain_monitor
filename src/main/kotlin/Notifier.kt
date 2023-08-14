import io.ktor.client.plugins.*
import kotlinx.serialization.Serializable
import io.ktor.client.request.*

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
    require(it != null) { "WEBHOOK_URL environment variable not set" }
}

@Serializable
private data class Payload(val content: String)

object Notifier {
    suspend fun send(message: String) {
        client.post(WEBHOOK_URL) {
            expectSuccess = true
            setBody(Payload(message))
        }
    }
}
