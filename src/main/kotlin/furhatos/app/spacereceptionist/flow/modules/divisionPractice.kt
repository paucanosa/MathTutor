
package furhatos.app.spacereceptionist.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.Number
import kotlin.random.Random

var easy = arrayOf(intArrayOf(4,2,2,0), intArrayOf(6,3,2,0), intArrayOf(10,5,2,0),intArrayOf(21,3,7,0))
var medium = arrayOf(intArrayOf(14,3,4,2), intArrayOf(9,2,4,1))
                            //  14/3 = 4... 2 (2 is the remainder)


val BeginExercises: State = state(Interaction) {

    onEntry {
        furhat.say("Before we start practicing, you need to know that the exercises are classified in three levels:" +
                " easy, medium and hard. We will start by the easiest ones and keep increasing the difficulty. " +
                "Once you think you have practiced enough, you can ask for an exam, in order to get proof that you have" +
                "understood the division.")
        goto(EasyExercises)
    }
}
val EasyExercises: State = state(Interaction){
    val index = (0..easy.size-1).random()
    onEntry{
        furhat.ask("Let's practice solving an easy division exercises. " +
                "What is " + easy[index][0] + " divided by " + easy[index][1] + "?"
        )
    }
    this.onResponse<Number> {
        if(Number(it.intent.value!!)==Number(easy[index][3])) {
            random(furhat.say("Good!"), furhat.say("Great!"), furhat.say("Awesome!"))
            furhat.say("That is right, the correct answer is "+ easy[index][2] + "." +
                "Now on to the medium level exercises")
        }else{
            furhat.say("That is incorrect. The answer is " +  easy[index][2] +
                        ".")
            goto(explainIncorrectAnswer(thisState, index, easy))
        }
    }
}
val MediumExercises: State = state(Interaction){
    val index = (0..medium.size-1).random()
    onEntry{
        furhat.ask("Try to solve this medium difficulty division problem." +
                "What is " + medium[index][0] + " divided by " + medium[index][1] + "?" +
                "And what is the remainder?"
        )
    }
    this.onResponse<Number> { // TODO: captured remainder as well for full answer and flow to be continued..
        if(Number(it.intent.value!!)==Number(medium[index][3])) {
            random(furhat.say("Good!"), furhat.say("Great!"), furhat.say("Awesome!"))
            furhat.say("That is right, the correct answer is "+ easy[index][2] + "." +
                    "Now on to the hard level exercises")
        }else{
            furhat.say("That is incorrect. The answer is " +  medium[index][2] +
                    ".")
            goto(explainIncorrectAnswer(thisState, index, medium))
        }
    }
}
fun explainIncorrectAnswer(stateOrigin: State, index: Int, exerciseList: Array<IntArray>): State = state(Interaction){
    onEntry{
        furhat.say("When you divide " + exerciseList[index][0] + " by "+ exerciseList[index][1]+
                ", you can think of it like distributing "+ exerciseList[index][1]+ " apples for "+
                exerciseList[index][0] + "people evenly. In the end, everyone will get "+ exerciseList[index][2]
        + " apples.")
        furhat.ask("Now, would you like to try another exercise again?")
    }
    this.onResponse<Yes> {
        furhat.say("Great!")
        goto(stateOrigin)
    }
    this.onResponse<No> {
        furhat.say("I can explain more on division and afterwards you can jump back to the exercise when you feel ready.")
        goto(BeginExplanation)
    }
}
