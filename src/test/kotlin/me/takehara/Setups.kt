package me.takehara

import com.thoughtworks.gauge.BeforeScenario
import com.thoughtworks.gauge.BeforeSuite
import me.takehara.database.DatabaseDriver

class DatabaseSetup {
    @BeforeSuite
    fun beforeSuite() {
        DatabaseDriver.executeCleanInsert("before/suite/database/")
    }

    @BeforeScenario(tags = ["cleanInsertBeforeScenario"])
    fun cleanInsertBeforeScenario() {
        DatabaseDriver.executeCleanInsert("before/scenario/database/")
    }
}
