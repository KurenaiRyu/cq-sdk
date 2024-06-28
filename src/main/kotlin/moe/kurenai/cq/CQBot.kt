package moe.kurenai.cq

class CQBot (
    val host: String,
    val port: Int,
    val token: String?
) {
}

class CQBotBuilder {
    private var port: Int = 6800
    private var host: String = "localhost"
    private var token: String? = null

    fun build() = CQBot(host = host, port = port, token = token)
}