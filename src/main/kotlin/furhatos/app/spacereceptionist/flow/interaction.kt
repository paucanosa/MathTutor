package furhatos.app.spacereceptionist.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start: State = state(Interaction) {

    onEntry {
        furhat.ask("Hey, I see you have order some math tutoring, am I right?")
    }

    this.onResponse<Yes> {
        furhat.say("Let's get started then.")
    }

    this.onResponse<No> {
        furhat.say("Sorry to hear that.")
    }
}
