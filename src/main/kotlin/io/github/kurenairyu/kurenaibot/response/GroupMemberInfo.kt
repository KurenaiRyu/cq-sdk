package io.github.kurenairyu.kurenaibot.response

import io.github.kurenairyu.kurenaibot.entity.Role

class GroupMemberInfo(
    val groupId: Long,
    val userId: Long,
    val nickname: String,
    val card: String,
    val sex: String,
    val age: Int,
    val area: String?,
    val joinTime: Long,
    val lastSentTime: Long,
    val level: String,
    val role: Role,
    val unfriendly: Boolean,
    val title: String?,
    val titleExpireTime: Long,
    val cardChangeable: Boolean
)