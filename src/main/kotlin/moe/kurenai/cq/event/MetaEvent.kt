package moe.kurenai.cq.event

/**
 * @author Kurenai
 * @since 6/30/2022 17:02:32
 */

data class MetaEvent(
    override val time: Long,
    override val selfId: Long,
    val metaEventType: String
) : Event() {
    override val postType: String = PostType.META
}

object MetaEventType {
    const val FIELD = "request_type"
    const val LIFECYCLE = "lifecycle"
}