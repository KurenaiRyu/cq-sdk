package moe.kurenai.cq.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import moe.kurenai.cq.model.LoginInfo
import moe.kurenai.cq.model.ResponseWrapper

class GetLoginInfo : Request<LoginInfo>() {

    companion object {
        const val METHOD = "get_login_info"
        val RESPONSE_TYPE = object : TypeReference<ResponseWrapper<LoginInfo>>() {}
    }

    @JsonIgnore
    override val method = METHOD

    @JsonIgnore
    override val responseType = RESPONSE_TYPE

    @JsonIgnore
    override val httpMethod = HttpMethod.GET

}