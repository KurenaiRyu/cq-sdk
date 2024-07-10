package moe.kurenai.cq.client

import moe.kurenai.cq.request.HttpMethod
import moe.kurenai.cq.request.Request
import moe.kurenai.cq.util.DefaultMapper.convertToByteArray
import org.apache.logging.log4j.LogManager
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.CompletableFuture

open class DefaultClient(
    val baseUrl: String,
    var token: String? = null,
    private val isDebugEnabled: Boolean = true
) {

    companion object {
        private val log = LogManager.getLogger()
        private val DEFAULT_TIMEOUT = Duration.ofSeconds(5)
        private const val DEFAULT_MIME_TYPE = "application/json"
    }


    fun <T> send(request: Request<T>, timeout: Duration? = null): CompletableFuture<T> {
        val uri = determineUri(request)
        if (isDebugEnabled) log.debug("Request to {}", uri)

//        return HttpClient.newHttpClient()
//            .sendAsync(buildRequest(uri, request, timeout), HttpResponse.BodyHandlers.ofByteArray())
//            .thenApplyAsync { response: HttpResponse<ByteArray> -> response.log() }
//            .thenApply { response: HttpResponse<ByteArray> -> response.parse(request.responseType).data }
        return CompletableFuture();
    }

    fun <T> sendSync(request: Request<T>, timeout: Duration? = null): T? {
        val uri = determineUri(request)
        if (isDebugEnabled) log.debug("Request to {}", uri)

//        return HttpClient.newHttpClient()
//            .send(buildRequest(uri, request, timeout), HttpResponse.BodyHandlers.ofByteArray())
//            .log()
//            .parse(request.responseType).data
        return null
    }

    private fun determineUri(request: Request<*>): URI {
//        return URI.create("$baseUrl/${request.method}")
        return URI.create("$baseUrl/")
    }

    private fun <T> buildRequest(uri: URI, request: Request<T>, timeout: Duration? = null): HttpRequest? {
        val httpRequest = HttpRequest.newBuilder()
        if (request.httpMethod == HttpMethod.POST) {
            httpRequest
                .header("Content-Type", DEFAULT_MIME_TYPE)
                .headers("Accept", DEFAULT_MIME_TYPE)
                .POST(HttpRequest.BodyPublishers.ofByteArray(convertToByteArray(request).also { printDebugRequestData(it) }))
        } else {
            httpRequest.GET()
        }
        token?.let { httpRequest.header("Authorization", "Bearer $token") }
        if (timeout != Duration.ZERO) httpRequest.timeout(timeout ?: DEFAULT_TIMEOUT)
        return httpRequest.uri(uri).build()
    }

    private fun printDebugRequestData(byteArray: ByteArray) {
        if (!isDebugEnabled) return
        log.debug("Request data \n{}", String(byteArray))
    }

    private fun logResponse(response: HttpResponse<ByteArray>): HttpResponse<ByteArray> {
        if (isDebugEnabled) response.body()?.let { log.debug("Response ${String(it)}") }
        return response
    }

    private fun HttpResponse<ByteArray>.log(): HttpResponse<ByteArray> {
        return logResponse(this)
    }

}