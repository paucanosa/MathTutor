
package furhatos.app.spacereceptionist.flow

import furhatos.app.spacereceptionist.flow.modules.BeginExam
import furhatos.app.spacereceptionist.flow.modules.UserCheerUp
import furhatos.app.spacereceptionist.flow.modules.addFillers
import furhatos.app.spacereceptionist.flow.modules.generalQuestion
import furhatos.app.spacereceptionist.flow.modules.isSadEmotion
import furhatos.app.spacereceptionist.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.Number
import kotlin.random.Random

var easy = arrayOf(intArrayOf(4,2,2,0), intArrayOf(6,3,2,0), intArrayOf(10,5,2,0),intArrayOf(21,3,7,0))
var medium = arrayOf(intArrayOf(13,4,3,1), intArrayOf(7,4,1,3))
                            //  14/3 = 4... 2 (2 is the remainder)
var hard = arrayOf(intArrayOf(34,7,4,6), intArrayOf(29,9,3,2), intArrayOf(107,13,8,3))

val BeginExercises: State = state(Interaction) {

    onEntry {

        furhat.say(addFillers("Before we start practicing, you need to know that the exercises are classified in three levels:" +
                " easy, medium and hard. Usually, we start by the easiest ones and keep increasing the difficulty. " +
                "Once you think you have practiced enough, you can ask for an exam, in order to get proof that you have" +
                "understood the division."))
        furhat.ask("Would you like to start by easy, medium or hard exercises?")

    }
    this.onResponse<MediumExercises> {
        furhat.say("Great")
        goto(MediumExercises)
    }
    this.onResponse<EasyExercises> {
        furhat.say("Good")
        goto(EasyExercises)
    }
    this.onResponse<HardExercises> {
        furhat.say("Alright")
        goto(HardExercises)
    }
    this.onResponse {
        goto(generalQuestion(it.text,thisState));
    }

}

val EasyExercises: State = state(Interaction){
    val index = (0 .. easy.size-1).random()
    onEntry{
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("Let's practice solving an easy division exercise. " +
                "What is " + easy[index][0] + " divided by " + easy[index][1] + "?"
        )
    }
    this.onResponse<Number> {
        if(Number(it.intent.value!!)==Number(easy[index][2])) {
            furhat.gesture(Gestures.Nod)
            random({furhat.say("Good!")}, {furhat.say("Great!")}, {furhat.say("Awesome!")})
            furhat.say("That is right, the correct answer is "+ easy[index][2] + "." +
                "Now on to the medium level exercises")
            furhat.gesture(Gestures.Smile)
            goto(MediumExercises)
        }else{
            furhat.gesture(Gestures.Shake)
            furhat.say("That is incorrect. The answer is " +  easy[index][2] +".")
            goto(explainIncorrectAnswer(thisState, index, easy))
        }
    }
    this.onResponse<Confused> {
        furhat.say("Don't worry. I will explain the solution to you.")
        val oldValence = users.current.valence
        val emotion = call(inferEmotion())
        if (isSadEmotion(emotion as String) && oldValence > users.current.valence) {
            goto(UserCheerUp(explainIncorrectAnswer(thisState, index, easy)))
        }
        goto(explainIncorrectAnswer(thisState, index, easy))
    }
    this.onResponse<UnwillingToContinue> {
        goto(UserCheerUp(this.thisState))
    }
    this.onResponse {
        goto(generalQuestion(it.text,thisState));
    }
}

val MediumExercises: State = state(Interaction){
    val index = (0 .. medium.size-1).random()
    onEntry{
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("Try to solve this medium difficulty division problem. " +
                "What is " + medium[index][0] + " divided by " + medium[index][1] + "? " +
                "And what is the remainder?"
        )
    }
    this.onResponse<DivisionAnswer> {
        val answerInfo = it.intent.answer
        if(answerInfo!!.quotient==Number(medium[index][2])) {
            if(answerInfo!!.remainder==Number(medium[index][3])){
                furhat.gesture(Gestures.Nod)
            random({furhat.say("Good!")}, {furhat.say("Great!")}, {furhat.say("Awesome!")})

            furhat.say("That is right, the correct answer is "+ medium[index][2] + ". With remainder of " +
                   medium[index][3]+ ". Now on to the hard level exercises")
                furhat.gesture(Gestures.Smile)
                goto(HardExercises)
            }else{
                furhat.gesture(Gestures.Shake)
                furhat.say("You got the quotient right, which is "+ medium[index][2] + ". " +
                        "But the remainder should be " + medium[index][3] + "." )
                reentry()
            }
        }
        else{
            furhat.gesture(Gestures.Shake)
            furhat.say("That is incorrect. The answer is " +  medium[index][2] +
                    " with remainder of " + medium[index][3] + ".")
            val oldValence = users.current.valence
            val emotion = call(inferEmotion())
            if (isSadEmotion(emotion as String) && oldValence > users.current.valence) {
                goto(UserCheerUp(explainIncorrectAnswer(thisState, index, medium)))
            }
            goto(explainIncorrectAnswer(thisState, index, medium))
        }
    }
    this.onResponse<Confused> {

        furhat.say("Don't worry. I will explain the solution to you.")
        goto(explainIncorrectAnswer(thisState, index, medium))
    }
    this.onResponse<UnwillingToContinue> {
        furhat.gesture(Gestures.ExpressSad)
        goto(UserCheerUp(this.thisState))
    }
    this.onResponse {
        goto(generalQuestion(it.text,thisState));
    }
}

val HardExercises: State = state(Interaction){
    val index = (0 until hard.size-1).random()
    onEntry{
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("Try to solve this hard difficulty division problem. " +
                "What is " + hard[index][0] + " divided by " + hard[index][1] + "?" +
                "And what is the remainder?"
        )
    }
    this.onResponse<DivisionAnswer> {
        val answerInfo = it.intent.answer
        if(answerInfo!!.quotient==Number(hard[index][2])) {
            if(answerInfo!!.remainder==Number(hard[index][3])){
                furhat.gesture(Gestures.Nod)
                random({furhat.say("Good!")}, {furhat.say("Great!")}, {furhat.say("Awesome!")})
                furhat.say("That is right, the correct answer is "+ hard[index][2] + ". With remainder of " +
                        hard[index][3]+ ". Now you can try taking the exam!")
                furhat.gesture(Gestures.Smile)
                goto(BeginExam)
            }else{
                furhat.gesture(Gestures.Shake)
                furhat.say("You got the quotient right, which is "+ hard[index][2] + ". " +
                        "But the remainder should be " + hard[index][3] + ".")
                reentry()
            }
        }
        else{
            furhat.gesture(Gestures.Shake)
            furhat.say("That is incorrect. The answer is " +  hard[index][2] +
                    " with remainder of " + hard[index][3] + ".")
            val oldValence = users.current.valence
            val emotion = call(inferEmotion())
            if (isSadEmotion(emotion as String) && oldValence > users.current.valence) {
                UserCheerUp(explainIncorrectAnswer(thisState, index, hard))
            }
            goto(explainIncorrectAnswer(thisState, index, hard))
        }
    }
    this.onResponse<Confused> {
        furhat.say("Don't worry. I will explain the solution to you.")
        goto(explainIncorrectAnswer(thisState, index, hard))
    }
    this.onResponse<UnwillingToContinue> {
        furhat.gesture(Gestures.ExpressSad)
        goto(UserCheerUp(this.thisState))
    }
    this.onResponse {
        goto(generalQuestion(it.text,thisState));
    }
}

fun explainIncorrectAnswer(stateOrigin: State, index: Int, exerciseList: Array<IntArray>): State = state(Interaction){
    onEntry{
        furhat.say("When you divide " + exerciseList[index][0] + " by "+ exerciseList[index][1]+
                ", you can think of it like distributing "+ exerciseList[index][1]+ " apples for "+
                exerciseList[index][0] + "people evenly. In the end, everyone will get "+ exerciseList[index][2]
        + " apples. And the remaining number of apple is " + exerciseList[index][3] +".")
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("Now, would you like to try another exercise again?")
    }
    this.onResponse<Yes> {
        furhat.gesture(Gestures.Smile)
        furhat.say("Great!")
        goto(stateOrigin)
    }
    this.onResponse<No> {
        furhat.gesture(Gestures.Nod)
        furhat.say("I can explain more on division and afterwards you can jump back to the exercise when you feel ready.")
        goto(BeginExplanation)
    }
    this.onResponse<UnwillingToContinue> {

        goto(UserCheerUp(this.thisState))
    }
    this.onResponse {
        goto(generalQuestion(it.text,thisState));
    }
}
