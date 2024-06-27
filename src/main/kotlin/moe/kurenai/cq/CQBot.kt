package moe.kurenai.cq

import moe.kurenai.cq.handler.EventHandler

object CQBot {

    public fun builder() {

    }

}

class CQBotBuilder {
    private var port: Int = 6800
    private var host: String = "localhost"
    private var token: String? = null
}