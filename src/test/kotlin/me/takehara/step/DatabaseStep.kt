package me.takehara.step

import com.thoughtworks.gauge.BeforeSuite
import me.takehara.database.DatabaseDriver

class DatabaseStep {
    private val databaseDriver = DatabaseDriver()

    @BeforeSuite
    fun beforeSuite() {
        databaseDriver.beforeSuite()
    }
}
