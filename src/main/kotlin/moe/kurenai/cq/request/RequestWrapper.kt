package moe.kurenai.cq.request

/**
 * @author Kurenai
 * @since 7/20/2022 11:23:21
 */

data class RequestWrapper<T : Request<*>>(
    val action: String,
    val params: T,
    val echo: String
)

fun <T> Request<T>.wrap(): RequestWrapper<Request<T>> {
    return RequestWrapper("this.method", this, this.echo)
//    return RequestWrapper(this.method, this, this.echo)
}