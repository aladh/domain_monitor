import java.text.SimpleDateFormat
import java.util.*

private val dateFormat by lazy { SimpleDateFormat("yyyy-MM-dd") }

data class Domain(val name: String) {
  private val tld: String
    get() = name.split(".").last()

  suspend fun expirationDate(): Date? =
    RdapRegistry.lookup(name, tld)
      ?.expirationDate
      ?.let { dateFormat.parse(it) }
}
