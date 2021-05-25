package me.takehara.database

import me.takehara.Configuration
import me.takehara.Configuration.Companion.config
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.csv.CsvDataSet
import org.dbunit.operation.DatabaseOperation
import org.jetbrains.exposed.sql.Database
import java.nio.file.Paths
import java.sql.DriverManager

class DatabaseDriver {
    val database: Database
    val connection: DatabaseConnection

    init {
        val url = config[Configuration.database.url]
        val schema = config[Configuration.database.schema]
        val driver = config[Configuration.database.driver]
        val user = config[Configuration.database.user]
        val password = config[Configuration.database.password]

        Class.forName(driver)
        val c = DriverManager.getConnection(url, user, password)
        database = Database.connect(url, driver, user, password)
        connection = DatabaseConnection(c, schema)
    }

    fun beforeSuite() {
        val csvDataSet = Thread.currentThread().contextClassLoader
            .getResource("before/suite/database/")!!
            .let { Paths.get(it.toURI()) }
            .let { CsvDataSet(it.toFile()) }
        DatabaseOperation.CLEAN_INSERT.execute(connection, csvDataSet)
    }
}
