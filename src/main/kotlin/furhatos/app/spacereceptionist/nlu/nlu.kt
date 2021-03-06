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
        return listOf("I don't know", "I don't understand","Where am I?",
            "What do you mean", "How?", "Why?",
            "What?", "What is this?", "What the hell is this?")
    }
}

class PracticeModule : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Exercises", "The exercises","I want the exercises",
            "I want to start with the exercises", "With exercises", "I want to practice some exercises")
    }
}

class ExamModule : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Exam", "The exam","I want the exam","I want to take the exam",
            "I want to start with the exam", "With the exam", "I want to practice with the exam")
    }
}
class ExplanationModule : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Explanation", "The explanation","I want the explanation",
            "I want to start with the explanation", "With explanation")
    }
}

class HardExercises : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Hard", "Hard exercises","I want the hard ones",
            "I want to start with the hard exercises", "I want to practice with hard exercises")
    }
}

class MediumExercises : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Medium", "Medium exercises","I want the medium ones",
            "I want to start with the medium exercises", "I want to practice with medium exercises")
    }
}
class EasyExercises : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Easy", "Easy exercises","I want the easy ones",
            "I want to start with the easy exercises", "I want to practice with easy exercises")
    }
}

class  UnwillingToContinue: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to quit", "I am tired","I am tired of practicing","I'm not doing it","Please, go away","Go away",
            "Leave me alone", "Can we stop practicing?", "I don't want to practice anymore","I don't want to",
            "I don't want to continue", "Can I quit?", "No more practicing","I don't want to", "I don't like you")
    }
}
class  Confirmation: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to quit", "I am tired","I am tired of practicing","I'm not doing it","Please, go away","Go away",
            "Leave me alone", "Can we stop practicing?", "I don't want to practice anymore","I don't want to",
            "I don't want to continue", "Can I quit?", "No more practicing","I don't want to", "I don't like you")
    }
}
class Frustrated : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I am bad at this", "I am bad","I can't do this", "I can't do it",
            "I feel dumb", "I am dumb",
            "too hard",  "too difficult")
    }
}

class ToExplanation : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Can you explain division to me?", "explain division")
    }
}

class ToExam : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I'm ready for the exam", "I want to take the exam")
    }
}

class DivisionQuestion(var question : DivisionQuestionInfo? = null) : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@question")
    }
}

class DivisionQuestionInfo(var dividend : Number = Number(0),
                         var divisor : Number = Number(0)) : ComplexEnumEntity() {

    override fun getEnum(lang: Language): List<String> {
        return listOf(
            "What is @dividend divided by @divisor",
            "@dividend divided by @divisor",
            "What is @dividend / @divisor",
            "@dividend / @divisor",
            "I have a math question")
    }

    override fun toText(): String {
        return generate("$dividend $divisor")
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
            "@quotient with remainder @remainder",
            "@quotient with reminder @remainder",
            "The quotient is @quotient and the remainder is @remainder",
            "The quotient is @quotient and the reminder is @remainder")
    }

    override fun toText(): String {
        return generate("$quotient $remainder")
    }
}

