package moe.kurenai.cq.handler

interface EventHandler<T> {

    suspend fun handle(event: T)

}