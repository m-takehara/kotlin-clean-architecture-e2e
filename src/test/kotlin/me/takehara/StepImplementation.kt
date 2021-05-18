package me.takehara

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.Table
import org.amshove.kluent.shouldBe

class StepImplementation {
    @Step("Vowels in English language are <vowelString>.")
    fun setLanguageVowels(vowelString: String) {
    }

    @Step("The word <word> has <expectedCount> vowels.")
    fun verifyVowelsCountInWord(word: String, expectedCount: Int) {
        expectedCount shouldBe 3
    }

    @Step("Almost all words have vowels <wordsTable>")
    fun verifyVowelsCountInMultipleWords(wordsTable: Table) {
    }

}
