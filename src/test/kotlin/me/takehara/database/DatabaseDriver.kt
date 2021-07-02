package me.takehara.database

import me.takehara.Configuration
import me.takehara.Configuration.Companion.config
import me.takehara.getResourceFile
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.csv.CsvDataSet
import org.dbunit.operation.DatabaseOperation
import org.jetbrains.exposed.sql.Database
import java.sql.DriverManager

object DatabaseDriver {
    val dbunitConnection: DatabaseConnection
    val exposedConnection: Database

    init {
        val url = config[Configuration.database.url]
        val schema = config[Configuration.database.schema]
        val driver = config[Configuration.database.driver]
        val user = config[Configuration.database.user]
        val password = config[Configuration.database.password]

        Class.forName(driver)
        val c = DriverManager.getConnection(url, user, password)
        dbunitConnection = DatabaseConnection(c, schema)

        exposedConnection = Database.connect(url, driver, user, password)
    }

    fun executeCleanInsert(csvFolderPath: String) = getResourceFile(csvFolderPath)
        .let(::CsvDataSet)
        .let { DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, it) }
}
