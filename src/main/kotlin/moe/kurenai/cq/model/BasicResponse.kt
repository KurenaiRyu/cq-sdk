package moe.kurenai.cq.model

import kotlinx.serialization.Serializable

@Serializable
data class BasicResponse(
    val retcode: Int,
    val status: String,
    val msg: String? = null,
    val wording: String? = null,
    val echo: String? = null
) {

    fun isSuccess(): Boolean {
        return status.equals("OK", true)
    }
}