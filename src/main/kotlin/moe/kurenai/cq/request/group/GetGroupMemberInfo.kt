package moe.kurenai.cq.request.group

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.GroupMemberInfo
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.request.Request

class GetGroupMemberInfo @JvmOverloads constructor(
    val groupId: Long,
    val userId: Long,
    val noCache: Boolean? = null,
) : Request<GroupMemberInfo>() {

    @JsonIgnore
    override val method = "get_group_member_info"

    @JsonIgnore
    override val responseType = object : TypeReference<ResponseWrapper<GroupMemberInfo>>() {}

}