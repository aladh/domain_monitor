import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private val serializer = Json { ignoreUnknownKeys = true }

val client = HttpClient(CIO) {
  install(DefaultRequest) { contentType(ContentType.Application.Json) }
  CurlUserAgent()
  install(ContentNegotiation) { json(serializer) }
  install(HttpTimeout) { requestTimeoutMillis = 5000 }
  install(HttpRequestRetry)
}
