package io.github.kurenairyu.kurenaibot.config

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CommonHeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
        headers.forEach { (name: String?, value: String?) -> builder.addHeader(name, value) }
        return chain.proceed(builder.build())
    }
}