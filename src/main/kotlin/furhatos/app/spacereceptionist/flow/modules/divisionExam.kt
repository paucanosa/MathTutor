package furhatos.app.spacereceptionist.flow.modules

import furhatos.app.spacereceptionist.flow.*
import furhatos.app.spacereceptionist.nlu.Confused
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Number
import furhatos.nlu.common.Yes

var easy = arrayOf(intArrayOf(4,2,2,0), intArrayOf(6,3,2,0), intArrayOf(10,5,2,0),intArrayOf(21,3,7,0))
var medium = arrayOf(intArrayOf(4,2,2,0), intArrayOf(6,3,2,0), intArrayOf(10,5,2,0),intArrayOf(21,3,7,0))
var hard = arrayOf(intArrayOf(4,2,2,0), intArrayOf(6,3,2,0), intArrayOf(10,5,2,0),intArrayOf(21,3,7,0))


val BeginExam: State = state(Interaction) {
    onEntry {
        furhat.say("Let's begin with the division exam. The exam will consist on three different exercises: one for each difficulty.");
        delay(500)
        furhat.ask("Do you understand it?")
    }
    this.onResponse<Yes> {
        random({furhat.say("Good!")}, {furhat.say("Great!")}, {furhat.say("Awesome!")})
        users.current.nCorrectExamQuestions = 0
        goto(ExamEasyExercice)
    }
    this.onResponse<No> {
        furhat.say("Don't worry, I will repeat it for you. I will ask you three different division exercises: the first one will have an easy difficulty, the second one a medium difficulty and the last one a hard difficulty. ");
        goto(ExamEasyExercice);
    }
}

val ExamEasyExercice: State = state(Interaction){
    val index = (0 .. easy.size-1).random()
    onEntry{
        furhat.ask("Let's begin the exam with an easy exercise: " +
                "What is " + easy[index][0] + " divided by " + easy[index][1] + "?"
        )
    }
    onReentry {
        furhat.ask("What is " + easy[index][0] + " divided by " + easy[index][1] + "?")
    }
    this.onResponse<Number> {
        if(Number(it.intent.value!!) == Number(easy[index][2])) {
            users.current.nCorrectExamQuestions += 1
        }
        goto(ExamMediumExercice)
    }
    this.onResponse<Confused> {
        furhat.say("Don't worry. I will repeat the question.")
        reentry()
    }
}

val ExamMediumExercice: State = state(Interaction){
    val index = (0 .. medium.size-1).random()
    onEntry{
        furhat.ask("Let's move on to the second question. This exercise has a medium difficulty: " +
                "What is " + medium[index][0] + " divided by " + medium[index][1] + "?"
        )
    }
    onReentry {
        furhat.ask("What is " + medium[index][0] + " divided by " + medium[index][1] + "?")
    }
    this.onResponse<Number> {
        if(Number(it.intent.value!!) == Number(medium[index][2])) {
            users.current.nCorrectExamQuestions += 1
        }
        goto(ExamHardExercice)
    }
    this.onResponse<Confused> {
        furhat.say("Don't worry. I will repeat the question.")
        reentry()
    }
}

val ExamHardExercice: State = state(Interaction){
    val index = (0 .. hard.size-1).random()
    onEntry{
        furhat.ask("We are almost done. This will be the last and most difficult one: " +
                "What is " + hard[index][0] + " divided by " + hard[index][1] + "?"
        )
    }
    onReentry {
        furhat.ask("What is " + hard[index][0] + " divided by " + hard[index][1] + "?")
    }
    this.onResponse<Number> {
        if(Number(it.intent.value!!) == Number(hard[index][2])) {
            users.current.nCorrectExamQuestions += 1
        }
        goto(ExamEnd)
    }
    this.onResponse<Confused> {
        furhat.say("Don't worry. I will repeat the question.")
        reentry()
    }
}

val ExamEnd: State = state(Interaction){
    onEntry{
        furhat.say("Perfect! You have already finished the exam. Let's see how many questions you asked correctly.")
        delay(2000)
        if (users.current.nCorrectExamQuestions == 3) {
            furhat.say("Well done!! All your answers were correct!")
            furhat.say("Congratulations! You already understand how the division works.")
        }
        else if (users.current.nCorrectExamQuestions == 2) {
            furhat.say("Good job! You only made one mistake ")
            furhat.ask("You almost anwered all the exercieses correctly. Do you want to practice a little bit more so that next time you obtain a perfect score?")
        }
        else if (users.current.nCorrectExamQuestions == 1) {
            furhat.say("You have made two mistakes. You need to keep practicing more.")
            furhat.ask("Do you want to do it now?")
        }
        else {
            furhat.say("All your answers were wrong. It looks like you need to practice more. I encourage you to keep practicing as soon as possible")
            furhat.ask("Do you want to do it now?")
        }
    }
    this.onResponse<Yes> {
        random({furhat.say("Good!")}, {furhat.say("Great!")}, {furhat.say("Awesome!")})
        goto(EasyExercises)
    }
    this.onResponse<No> {
        furhat.say("No problem. See you in our next class.");
    }
}