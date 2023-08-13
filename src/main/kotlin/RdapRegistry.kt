import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames
import java.io.FileNotFoundException
import java.net.URL

@Serializable
private data class RdapServiceList(val services: List<List<List<String>>>)

private const val BOOTSTRAP_URL = "https://data.iana.org/rdap/dns.json"
private val json = Json { ignoreUnknownKeys = true }

object RdapRegistry {
    @Serializable
    data class RdapResponse(val events: List<RdapEvent>) {
        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        data class RdapEvent(@JsonNames("eventAction") val action: String, @JsonNames("eventDate") val date: String) {
            companion object {
                const val EXPIRATION_ACTION = "expiration"
            }
        }

        val expirationDate: String? = events.find { it.action == RdapEvent.EXPIRATION_ACTION }?.date
    }

    private val serviceList: RdapServiceList by lazy {
        URL(BOOTSTRAP_URL)
            .readText()
            .let { json.decodeFromString<RdapServiceList>(it) }
    }

    fun lookup(name: String, tld: String): RdapResponse? = try {
        URL("${serviceForTLD(tld)}domain/$name")
            .readText()
            .let { json.decodeFromString<RdapResponse>(it) }
    } catch (e: FileNotFoundException) {
        null
    }

    private fun serviceForTLD(tld: String): String? {
        return serviceList.services.find { it[0][0] == tld }?.get(1)?.get(0)
    }
}
