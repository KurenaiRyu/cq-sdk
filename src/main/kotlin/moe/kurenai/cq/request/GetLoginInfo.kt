package moe.kurenai.cq.request

import kotlinx.serialization.Serializable
import moe.kurenai.cq.model.LoginInfo

@Serializable
class GetLoginInfo : Request<LoginInfo>(
    path = PATH,
    httpMethod = HttpMethod.GET
) {

    companion object {
        const val PATH = "get_login_info"
    }

}