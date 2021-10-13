package furhatos.app.spacereceptionist.nlu

import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.Intent
import furhatos.util.Language
import furhatos.nlu.common.Number


// CheckIn Intent
class CheckIn : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I would like to check in", "I would like to check-in",
                "check-in",
                "checkin"
        )
    }
}

// Wtf is going on intent
class Confused : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Who are you?", "Where am I?", "What?", "What is this?", "What the hell is this?")
    }
}

class DivisionAnswer(var answer : DivisionAnswerInfo? = null) : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@answer")
    }
}

class DivisionAnswerInfo(var quotient : Number = Number(0),
               var remainder : Number = Number(0)) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf(
            "The quotient is @quotient.",
            "@quotient",
            "It is @quotient",
            "left with @remainder",
            "The remainder is @remainder.",
            "The answer is @quotient.",
            "remainders @remainder")
    }

    override fun toText(): String {
        return generate("$quotient $remainder")
    }
}