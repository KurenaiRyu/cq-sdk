package moe.kurenai.cq.event

/**
 * @author Kurenai
 * @since 6/30/2022 17:02:32
 */

abstract class RequestEvent : Event() {
    override val postType: String get() = PostType.REQUEST
    abstract val requestType: String
}

object RequestType {
    const val FIELD = "request_type"
}