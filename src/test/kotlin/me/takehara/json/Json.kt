package me.takehara.json

import com.jayway.jsonpath.JsonPath
import me.takehara.ResponseJson

object Json {
    fun getString(json: String, jsonPath: String): String {
        return JsonPath.read(json, jsonPath)
    }
}

fun ResponseJson.getString(jsonPath: String): String {
    return Json.getString(this, jsonPath)
}
