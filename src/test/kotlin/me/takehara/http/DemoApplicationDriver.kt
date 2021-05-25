package me.takehara.http

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import me.takehara.Configuration

class DemoApplicationDriver {
    companion object {
        val baseUrl = Configuration.config[Configuration.application.endpoint]
    }

    fun doPost(url: String, jsonBody: String): Pair<String, Int> {
        val (_, response, result) = url.httpPost().jsonBody(jsonBody).responseString()
        return when (result) {
            is Result.Success -> Pair(result.get(), response.statusCode)
            is Result.Failure -> String(result.getException().response.body().toByteArray()) to response.statusCode
        }
    }
}
