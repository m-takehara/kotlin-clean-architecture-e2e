package me.takehara

import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

fun getResourceAsString(path: String): String {
    return getResourceFile(path)
        .readLines(StandardCharsets.UTF_8)
        .joinToString()
}

fun getResourceFile(path: String): File {
    return Thread.currentThread()
        .contextClassLoader
        .getResource(path)
        ?.toURI()
        ?.let(::File)
        ?: throw FileNotFoundException("Resource not found: $path")
}
