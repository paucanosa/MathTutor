
package furhatos.app.spacereceptionist.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val BeginExplanation: State = state(Interaction) {

    onEntry {
        furhat.ask("The division is a method used to distribute a group of things into equal parts. For example," +
                "if you have 4 candies and you want to distribute them to 4 friends, if you want to make it equal for everyone," +
                "you will give one candy to each friend. ");
    }
}
