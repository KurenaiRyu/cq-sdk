package moe.kurenai.cq.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.uritl.IdGenerator

abstract class Request<T> {

    abstract val method: String
    abstract val responseType: TypeReference<ResponseWrapper<T>>

    @JsonIgnore
    open val httpMethod: HttpMethod = HttpMethod.POST

    @JsonIgnore
    open val needToken: Boolean = true

    open val echo: String by lazy {
        IdGenerator.nextStr()
    }
}

enum class HttpMethod {
    GET, POST
}