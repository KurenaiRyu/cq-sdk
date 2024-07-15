package moe.kurenai.cq.request

import kotlinx.serialization.Serializable

/**
 * @author Kurenai
 * @since 7/20/2022 11:23:21
 */

@Serializable
data class RequestWrapper<T : Request<*>>(
    val action: String,
    val params: T,
    val echo: String
)

fun <T> Request<T>.wrap(): RequestWrapper<Request<T>> {
    return RequestWrapper(this.path, this, this.echo)
//    return RequestWrapper(this.method, this, this.echo)
}