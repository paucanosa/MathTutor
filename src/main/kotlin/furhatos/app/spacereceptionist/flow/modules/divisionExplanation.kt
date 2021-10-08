
package furhatos.app.spacereceptionist.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import kotlin.random.Random

//TODO: I feel I am repeating a little bit. We can add another ways to explain the division...
val explanations = arrayOf(
    "The division can be interpreted as a method of grouping objects equally in groups, " +
            "for example, when arranging students in rows. When this happens, you are dividing, because you are " +
            "splitting the students into smaller and equal groups",

    "Ask yourself: How would you distribute a pizza for 4 people? Exactly, you would cut it in 4 equal slices. Thats what division" +
            "is about, distribute something into equal parts. ",

    "Division is basically splitting into equal parts. It is the result of sharing in a fair way.Imagine there " +
            "are 12 chocolates, and 3 friends want to share them, how would they divide the chocolates? They should" +
            " get 4 each. You have just divided 12 into 4."
    )

val BeginExplanation: State = state(Interaction) {

    onEntry {
        furhat.say("The division consist of distributing a group of things into equal parts. For example," +
                "if you have 4 candies and you want to distribute them to 4 friends, if you want to make it equal for everyone," +
                "you will give one candy to each friend. ");
        delay(500)
        users.current.neededExplanations = 1
        furhat.ask("Do you understand it?")
    }
    this.onResponse<Yes> {
        random(furhat.say("Good!"), furhat.say("Great!"), furhat.say("Awesome!"))
        goto(ExplanationUnderstood)
    }
    this.onResponse<No> {
        furhat.say("Don't worry, I will use another example!");
        goto(AdditionalExplanation);
    }
}

var ExplanationUnderstood: State = state(Interaction){
    onEntry {
        furhat.ask("Do you want to try some exercises to practice?")
    }

    this.onResponse<Yes> {
        furhat.say("Let's get started then!")
        goto(BeginExercises)
    }

    this.onResponse<No> {
        furhat.say("I am afraid you need to practice a little bit before taking the exam. Let's get started!")
        goto(BeginExercises)
    }
}

var AdditionalExplanation: State = state(Interaction){
    onEntry {
        users.current.neededExplanations +=1
        val nextExplanation:Int = Random.nextInt(0,3)
        //TODO: We should not repeat the explanations, just read them all if necessary and skip to the exercise part.
        furhat.say(explanations[nextExplanation])
        random(
            { furhat.ask("Do you understand it now?") },
            { furhat.ask("Do you comprehend how division works now?") },
            { furhat.ask("Do you think the concept of division is clear now?") }
        )
    }
    this.onResponse<Yes> {
        random(furhat.say("Good!"), furhat.say("Great!"), furhat.say("Awesome!"))
    }
    this.onResponse<No> {
        if(users.current.neededExplanations==4)
            furhat.say("Don't worry, let's start practicing and you will see it's not that difficult!")
        else{
            random({furhat.say("Don't worry, we are going to try another way!")},
                {furhat.say("It's okay, sometimes new things are not easy to understand! Let's try with other examples!")},
                    {furhat.say("Then let's try with another way to explain it!")})
            reentry()
        }

    }

}
