package moe.kurenai.cq.request.group

import moe.kurenai.cq.model.GroupMemberInfo
import moe.kurenai.cq.request.Request

class GetGroupMemberInfo @JvmOverloads constructor(
    val groupId: Long,
    val userId: Long,
    val noCache: Boolean? = null,
) : Request<GroupMemberInfo>(
    path = PATH
) {
    companion object {
        val PATH = "get_group_member_info"
    }

}