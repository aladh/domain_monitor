import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

private val WEBHOOK_URL: String by lazy {
    System.getenv("WEBHOOK_URL").also {
        require(it != null) { "WEBHOOK_URL environment variable not set" }
    }
}

@Serializable
private data class Payload(val content: String)

object Notifier {
    private val client = HttpClient(CIO) {
        install(DefaultRequest) {
            url(WEBHOOK_URL)
            contentType(ContentType.Application.Json)
        }
        CurlUserAgent()
        install(ContentNegotiation) { json() }
        install(HttpTimeout) { requestTimeoutMillis = 5000 }
        install(HttpRequestRetry) {
            maxRetries = 3
            exponentialDelay()
        }
    }

    suspend fun send(message: String) {
        client.post { setBody(Payload(message)) }.also {
            check(it.status.value == 204) { "Failed to send notification: ${it.status}" }
        }
    }
}
