package moe.kurenai.cq.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Kurenai
 * @since 6/30/2022 17:02:32
 */
@Serializable
data class MetaEvent(
    override val time: Long,
    override val selfId: Long,
    val metaEventType: MetaEventType
) : Event() {
    override val postType = PostType.META
}

@Serializable
enum class MetaEventType(
    val type: String
) {
    @SerialName("lifecycle")
    LIFECYCLE ("lifecycle"),
}