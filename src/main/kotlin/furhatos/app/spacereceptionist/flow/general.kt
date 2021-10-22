package furhatos.app.spacereceptionist.flow

import furhatos.app.spacereceptionist.flow.modules.BeginExam
import furhatos.flow.kotlin.*
import furhatos.util.*

val Idle: State = state {

    init {
        furhat.param.interruptableOnAsk = true
        furhat.param.interruptableOnSay = true
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)


        if (users.count > 0) {
            furhat.attend(users.random)
            goto(InitState)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(InitState)
    }
}

val Interaction: State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(InitState)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }
    onResponseFailed() {
        furhat.say("Sorry, my speech recognizer is not working")
        delay(500)
        reentry()
    }

}