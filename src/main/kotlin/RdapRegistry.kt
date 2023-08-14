import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
private data class ServiceList(private val services: List<List<List<String>>>) {
    fun serviceForTLD(tld: String): String? =
        services.find { it[0].contains(tld) }?.get(1)?.get(0)
}

private const val BOOTSTRAP_URL = "https://data.iana.org/rdap/dns.json"

object RdapRegistry {
    @Serializable
    data class Response(private val events: List<Event>) {
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        data class Event(@JsonNames("eventAction") val action: String, @JsonNames("eventDate") val date: String) {
            companion object {
                const val EXPIRATION_ACTION = "expiration"
            }
        }

        val expirationDate: String?
            get() = events.find { it.action == Event.EXPIRATION_ACTION }?.date
    }

    private lateinit var serviceList: ServiceList

    suspend fun lookup(name: String, tld: String): Response? =
        client.get("${serviceList.serviceForTLD(tld)}domain/$name").let {
            if (it.status.value == HttpStatusCode.OK.value) it.body() else null
        }

    suspend fun loadServiceList() {
        serviceList = client.get(BOOTSTRAP_URL) {
            expectSuccess = true
        }.body()
    }
}
