package me.takehara

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

class Configuration {
    companion object {
        val config = ConfigurationProperties.fromResource("uat.properties")
    }

    object application : PropertyGroup() {
        val endpoint by stringType
    }

    object database : PropertyGroup() {
        val url by stringType
        val driver by stringType
        val user by stringType
        val password by stringType
    }
}
