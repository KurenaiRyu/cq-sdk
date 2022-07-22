package moe.kurenai.cq.request.private

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.MessageId
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.model.SingleMessage
import moe.kurenai.cq.request.Request

class SendPrivateMsg @JvmOverloads constructor(
    val userId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
) : Request<MessageId>() {

    @JsonIgnore
    override val method = "send_private_msg"

    @JsonIgnore
    override val responseType = object : TypeReference<ResponseWrapper<MessageId>>() {}
}