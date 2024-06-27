package moe.kurenai.cq.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.util.Snowflake
import kotlin.reflect.typeOf

abstract class Request<T> (
    @Transient
    val path: String,
    @Transient
    val httpMethod: HttpMethod = HttpMethod.POST,
    @Transient
    val needToken: Boolean = true,
) {

    @Transient
    val type = typeOf<ResponseWrapper<T>>()

    val echo: String by lazy {
        Snowflake.INSTANT.nextIdStr()
    }
}

enum class HttpMethod {
    GET, POST
}