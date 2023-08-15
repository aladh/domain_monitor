import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
  require(it != null) { "WEBHOOK_URL environment variable not set" }
}

@Serializable
private data class Payload(val content: String)

object WebhookNotifier {
  suspend fun send(message: String) {
    client.post(WEBHOOK_URL) {
      expectSuccess = true
      setBody(Payload(message))
    }
  }
}
