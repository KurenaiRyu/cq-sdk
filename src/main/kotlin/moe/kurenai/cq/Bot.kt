package moe.kurenai.cq

object Bot {

    fun newBot(block: CQBotBuilder.() -> Unit): CQBot {
        return CQBotBuilder().apply(block).build()
    }

}