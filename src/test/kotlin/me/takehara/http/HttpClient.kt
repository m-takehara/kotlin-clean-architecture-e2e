package me.takehara.http

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost

fun doPost(url: String, jsonBody: String): Pair<String, Int> {
    val (request, response, result) = url.httpPost().jsonBody(jsonBody).responseString()
    return Pair(result.get(), response.statusCode)
}
