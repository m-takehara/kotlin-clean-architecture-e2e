package me.takehara.json

import org.json.JSONObject

fun digString(json: String, path: String): String {
    val (j, s) = dig(json, path)
    return j.getString(s)
}

fun digInt(json: String, path: String): Int {
    val (j, s) = dig(json, path)
    return j.getInt(s)
}

private fun dig(json: String, path: String): Pair<JSONObject, String> {
    val split = path.split(".")
    val jsonObject = try {
        JSONObject(json)
    } catch (e: Exception) {
        System.err.println("Invalid JSON format.\nJSON: $json\npath: $path")
        throw e
    }
    if (split.size == 1) {
        return Pair(jsonObject, split[0])
    }
    return dig(jsonObject.get(split[0]).toString(), split.subList(1, split.size).joinToString(separator = "."))
}
