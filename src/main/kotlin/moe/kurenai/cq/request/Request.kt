package moe.kurenai.cq.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference

abstract class Request<T> {

    abstract val method: String
    abstract val responseType: TypeReference<T>

    @JsonIgnore
    open val httpMethod: HttpMethod = HttpMethod.POST

    @JsonIgnore
    open val needToken: Boolean = true

}

enum class HttpMethod {
    GET, POST
}