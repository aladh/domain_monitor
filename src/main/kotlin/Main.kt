import kotlinx.coroutines.delay
import java.util.*
import kotlin.time.Duration.Companion.days

private const val DOMAIN_DELIMITER = ","

suspend fun main(args: Array<String>) {
  RdapRegistry.loadServiceList()

  args[0]
    .split(DOMAIN_DELIMITER)
    .map { Domain(it) }
    .forEach { domain ->
      domain.expirationDate()
        ?.let { date ->
          if (date.before(Date())) {
            printAndNotify("${domain.name} expired on $date.")
          } else if (date.before(Date(System.currentTimeMillis() + 30.days.inWholeMilliseconds))) {
            printAndNotify("${domain.name} expires in less than 30 days on $date.")
          } else {
            println("${domain.name} expires on $date.")
          }
        } ?: printAndNotify("${domain.name} might be available for registration.")

      delay(1000)
    }
}

private suspend fun printAndNotify(message: String) {
  println(message)
  WebhookNotifier.send(message)
}
