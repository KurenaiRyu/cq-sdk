package io.github.kurenairyu.kurenaibot.entity

class Member(
    override val userId: Long,
    val nickname: String,
    val card: String,
    val sex: String,
    val age: Int,
    val area: String,
    val level: String,
    val role: Role,
    val title: String
) : User()

enum class Role {
    owner, admin, member;
}