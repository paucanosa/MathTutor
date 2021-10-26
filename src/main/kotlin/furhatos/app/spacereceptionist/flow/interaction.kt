package furhatos.app.spacereceptionist.flow

import furhatos.app.spacereceptionist.flow.modules.BeginExam
import furhatos.app.spacereceptionist.flow.modules.UserCheerUp
import furhatos.app.spacereceptionist.flow.modules.generalQuestion
import furhatos.app.spacereceptionist.nlu.Confused
import furhatos.app.spacereceptionist.nlu.ExamModule
import furhatos.app.spacereceptionist.nlu.PracticeModule
import furhatos.app.spacereceptionist.nlu.getSentiment
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
        furhat.gesture(LookAway, async = true)
        furhat.gesture(Gestures.Smile)
        furhat.ask("Are you ready?");
    }
    // use this to test CoreNLP classification of user's response
//    this.onResponse{
//        var sentiment = getSentiment(it.text)
//        println(sentiment+ " "+it.text)
//        reentry()
//    }
    this.onResponse<Yes> {
        furhat.gesture(LookAway, async = true)
        furhat.say("Good!")
        goto(CheckingUserFromTheBeginning)

    }

    this.onResponse<No> {
        furhat.gesture(Gestures.Thoughtful)
        furhat.gesture(LookAway, async = true)
        furhat.say("Don't worry then, take your time!")
    }

    this.onResponse<Confused> {
        furhat.gesture(LookAway, async = true)
        furhat.say("I am your math teacher, today we are going to learn the division and" +
                " I will help you in the process. ")
        furhat.gesture(Gestures.Thoughtful)
        reentry()
    }

   this.onResponse {
       goto(catchSentiment(it.text, thisState))
   }
}

val CheckingUserFromTheBeginning:State = state(parent=Interaction){
    onEntry {


        furhat.gesture(LookAway, async = true)

        furhat.ask("Would you like to start from the beginning?")

    }
    this.onResponse<Yes> {
        furhat.say("Then let's start by introducing ourselves!");
        furhat.gesture(LookAway, async = true)
        goto(InitialDataRetrieval);
    }
    this.onResponse<No> {
        furhat.gesture(LookAway, async = true)
       goto(ChoosePracticeOrExam)
    }

    this.onResponse {
        goto(catchSentiment(it.text, thisState))
    }
}

val ChoosePracticeOrExam:State = state(Interaction){
    onEntry {
        furhat.ask("Then, choose, do you want to start with the exercises or exam?")
        furhat.gesture(LookAway, async = true)
    }
    this.onResponse<PracticeModule> {
        furhat.say("Good")
        furhat.gesture(LookAway, async = true)
        goto(BeginExercises);
    }
    this.onResponse<ExamModule> {
        furhat.say("Good")
        furhat.gesture(LookAway, async = true)
        goto(BeginExam);
    }
    this.onResponse {
        goto(catchSentiment(it.text, thisState))
    }
}

val InitialDataRetrieval:State = state(Interaction){
    onEntry {
        furhat.gesture(Gestures.Thoughtful)
        furhat.ask("What's your name?");
        furhat.gesture(LookAway, async = true)

    }

    this.onReentry {
        furhat.ask("Can I know your name please?")
    }

    this.onResponse<PersonName> {
        users.current.name = it.intent.text;
        furhat.gesture(Gestures.Smile)
        furhat.gesture(LookAway, async = true)
        furhat.say("Nice to meet you ${users.current.name}")
        furhat.gesture(Gestures.Wink)
        furhat.ask("Do you like maths?")
        furhat.gesture(LookAway, async = true)
    }
    this.onResponse<Yes> {
        if(users.current.name.isEmpty())reentry();
        else {
            furhat.gesture(Gestures.Smile)
            furhat.gesture(LookAway, async = true)
            furhat.say("Good, then you are going to like this!")
            users.current.userLikesMaths = "Yes";
            goto(BeginExplanation)
        }
    }
    this.onResponse<No> {
        if(users.current.name.isEmpty())reentry()
        else {
            furhat.gesture(Gestures.Thoughtful)
            furhat.gesture(LookAway, async = true)
            furhat.say("Then don't worry, I am sure you are going to learn it fast!")
            users.current.userLikesMaths = "No";
            goto(BeginExplanation)
        }
    }
    this.onResponse {
        goto(catchSentiment(it.text, thisState))
    }
}
fun catchSentiment(txt:String, originalState: State): State = state(Interaction){
    onEntry {
        var sentiment = getSentiment(txt)
        print("$sentiment $txt")
        if (sentiment == "Positive") {
            furhat.gesture(Gestures.BigSmile)
            furhat.say("It is good to hear that.")
            goto(originalState)
        } else if (sentiment == "Negative") {
            furhat.gesture(Gestures.ExpressSad)
            goto(UserCheerUp(originalState))
        } else
            this.goto(generalQuestion(txt, originalState))
    }
}




