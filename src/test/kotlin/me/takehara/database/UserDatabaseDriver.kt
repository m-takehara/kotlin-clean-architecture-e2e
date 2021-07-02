package me.takehara.database

import me.takehara.getResourceFile
import org.dbunit.Assertion
import org.dbunit.dataset.csv.CsvDataSet

object UserDbDriver {
    fun assertTableEquals(tableNames: Collection<String>, csvFolderPath: String) {
        val actual = DatabaseDriver.dbunitConnection.createDataSet()
        val expected = getResourceFile(csvFolderPath).let(::CsvDataSet)

        tableNames.forEach {
            Assertion.assertEquals(expected.getTable(it), actual.getTable(it))
        }
    }
}
