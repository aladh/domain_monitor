private const val DOMAIN_DELIMITER = ","

fun main(args: Array<String>) =
    args[0]
        .split(DOMAIN_DELIMITER)
        .map { Domain(it) }
        .forEach { domain ->
            domain.expirationDate()
                ?.let { date -> println("Expiration date for ${domain.name} is $date") }
                ?: println("No expiration date found for ${domain.name}")

            Thread.sleep(1000)
        }
