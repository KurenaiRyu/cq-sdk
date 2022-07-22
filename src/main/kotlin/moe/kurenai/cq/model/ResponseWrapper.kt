package moe.kurenai.cq.model

data class ResponseWrapper<T>(
    val data: T? = null,
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