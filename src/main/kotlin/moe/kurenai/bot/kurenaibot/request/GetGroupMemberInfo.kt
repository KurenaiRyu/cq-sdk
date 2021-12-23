package moe.kurenai.bot.kurenaibot.request

class GetGroupMemberInfo(
    val groupId: Long,
    val userId: Long,
    val noCache: Boolean? = null,
) {

}