data class Domain(val name: String) {
    private val tld: String
        get() = name.split(".").last()

    fun expirationDate(): String? = RdapRegistry.lookup(name, tld)?.expirationDate
}
