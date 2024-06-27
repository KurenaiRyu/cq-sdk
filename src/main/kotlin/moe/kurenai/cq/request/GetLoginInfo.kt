package moe.kurenai.cq.request

import moe.kurenai.cq.model.LoginInfo

class GetLoginInfo : Request<LoginInfo>(
    path = PATH,
    httpMethod = HttpMethod.GET
) {

    companion object {
        const val PATH = "get_login_info"
    }

}