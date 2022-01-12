package moe.kurenai.cq.request.group

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.MessageId
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.model.SingleMessage
import moe.kurenai.cq.request.Request

class SendGroupMsg(
    val groupId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
) : Request<ResponseWrapper<MessageId>>() {

    @JsonIgnore
    override val method = "send_group_msg"

    @JsonIgnore
    override val responseType = object : TypeReference<ResponseWrapper<MessageId>>() {}
}