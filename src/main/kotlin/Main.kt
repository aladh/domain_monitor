import java.util.Date

private const val DOMAIN_DELIMITER = ","

fun main(args: Array<String>) =
    args[0]
        .split(DOMAIN_DELIMITER)
        .map { Domain(it) }
        .forEach { domain ->
            domain.expirationDate()?.let { date ->
                if (date.before(Date())) {
                    println("${domain.name} has expired")
                } else {
                    println("${domain.name} expires on $date")
                }
            } ?: println("${domain.name} might be available for registration")

            Thread.sleep(1000)
        }
