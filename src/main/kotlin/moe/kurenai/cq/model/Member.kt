package moe.kurenai.cq.model

import kotlinx.serialization.Serializable

@Serializable
class Member(
    override val userId: Long,
    val nickname: String,
    val card: String,
    val sex: String,
    val age: Int,
    val area: String,
    val level: String,
    val role: String,
    val title: String
) : User()

object Role {
    const val FIELD_NAME = "role"
    const val OWNER = "owner"
    const val ADMIN = "admin"
    const val MEMBER = "member"
}