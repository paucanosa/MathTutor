package furhatos.app.spacereceptionist.flow

import furhatos.app.spacereceptionist.nlu.Confused
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.*
import furhatos.nlu.NLUUtils


val InitState: State = state(Interaction) {

    onEntry {
        furhat.glance(users.current);
        furhat.gesture(Gestures.BigSmile)
        furhat.ask("Hello, welcome to today's math class! Today we are going to learn the division! Are you ready?");
    }
    onReentry {
        furhat.gesture(Gestures.Smile)
        furhat.ask("Are you ready?");
    }

    this.onResponse<Yes> {
        furhat.say("Then let's start by introducing ourselves!")
        goto(InitialDataRetrieval)
    }

    this.onResponse<No> {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("Don't worry then, take your time!")
    }

    this.onResponse<Confused> {

        furhat.say("I am your math teacher, today we are going to learn the division and" +
                " I will help you in the process. ")
        furhat.gesture(Gestures.Thoughtful)
        reentry()
    }
}

val InitialDataRetrieval:State = state(Interaction){
    onEntry {
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("What's your name?");

    }
    this.onResponse<PersonName> {
        users.current.name = it.intent.text;
        furhat.gesture(Gestures.Smile)
        furhat.say("Nice to meet you ${users.current.name}")
        furhat.gesture(Gestures.Wink)
        furhat.ask("Do you like maths?")
    }
    this.onResponse<Yes> {
        if(users.current.name.isEmpty())reentry();
        else {
            furhat.gesture(Gestures.Smile)
            furhat.say("Good, then you are going to like this!")
            users.current.userLikesMaths = "Yes";
            goto(BeginExplanation)
        }
    }
    this.onResponse<No> {
        if(users.current.name.isEmpty())reentry()
        else {
            furhat.gesture(Gestures.Thoughtful)
            furhat.say("Then don't worry, I am sure you are going to learn it fast!")
            users.current.userLikesMaths = "No";
            goto(BeginExplanation)
        }
    }
}

