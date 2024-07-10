package moe.kurenai.cq.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Kurenai
 * @since 6/30/2022 17:02:32
 */

@Serializable
abstract class RequestEvent : Event() {
    override val postType = PostType.REQUEST
    abstract val requestType: String
}

@Serializable
enum class RequestType {
    @SerialName("request_type")
    FIELD
}