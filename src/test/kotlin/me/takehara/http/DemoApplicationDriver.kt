package me.takehara.http

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import me.takehara.Configuration

object DemoApplicationDriver {
    fun doPost(path: String, jsonBody: String): Pair<ResponseBody, StatusCode> {
        val url = Configuration.config[Configuration.application.endpoint] + path
        val (_, response, result) = url.httpPost().jsonBody(jsonBody).responseString()
        return when (result) {
            is Result.Success -> Pair(result.get(), response.statusCode)
            is Result.Failure -> String(result.getException().response.body().toByteArray()) to response.statusCode
        }
    }
}

typealias ResponseBody = String
typealias StatusCode = Int
