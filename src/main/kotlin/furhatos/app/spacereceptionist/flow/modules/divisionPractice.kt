
package furhatos.app.spacereceptionist.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import kotlin.random.Random


val BeginExercises: State = state(Interaction) {

    onEntry {
        furhat.say("Before we start practicing, you need to know that the exercises are classified in three levels:" +
                " easy, medium and hard. We will start by the easiest ones and keep increasing the difficulty. " +
                "Once you think you have practiced enough, you can ask for an exam, in order to get proof that you have" +
                "understood the division.")
    }
    //TODO: Continue implementing this module.
}
