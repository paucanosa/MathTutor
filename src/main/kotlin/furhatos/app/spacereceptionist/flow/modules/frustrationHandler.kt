package furhatos.app.spacereceptionist.flow.modules

import furhatos.app.spacereceptionist.flow.*
import furhatos.app.spacereceptionist.nlu.Confused
import furhatos.app.spacereceptionist.nlu.UnwillingToContinue
import furhatos.flow.kotlin.*
import furhatos.nlu.common.No
import furhatos.nlu.common.Number
import furhatos.nlu.common.Yes

val explanationStates = arrayOf("BeginExplanation","ExplanationUnderstood","AdditionalExplanation","TakeDivisionQuestion")
val practiceStates = arrayOf("EasyExercises","MediumExercises","HardExercises","explainIncorrectAnswer")
val examStates = arrayOf("BeginExam","ExamEasyExercice","ExamMediumExercice","ExamHardExercice","ExamEnd")

fun UserCheerUp(originState: State): State = state(Interaction) {
    onEntry {
       if(explanationStates.contains(originState.name))goto(UserCheerUpExplanation(originState))
       else if(practiceStates.contains(originState.name))goto(UserCheerUpPractice(originState))
        else if(examStates.contains(originState.name))goto(UserCheerUpExam(originState))
        else {
           furhat.say("Come on, let's continue, you'll see this session is going to be worthed!")
           goto(originState)
       }
    }
}


fun UserCheerUpExplanation(originState: State): State = state(Interaction) {
    onEntry {
        random({furhat.say("Hey, I know this is not easy but it will really help you on the following courses")},
            {furhat.say("Hey, it's only the beginning, I am sure you are going to have fun when practicing, trust me!")},
            {furhat.say("Do you understand it now?")})
        furhat.ask("Let's continue, alright?")
    }
    this.onResponse<Yes> {
        random({furhat.say("Okay, let me continue then" )},
            {furhat.say("Great, I am going to repeat myself" )},
            { furhat.say("Good choice, let me refresh your memory with where we were.")})
        goto(originState);
    }
    this.onNoResponse {
        random({furhat.say("Let me start again" )},
            {furhat.say("I am going to repeat it" )},
            { furhat.say("Let me refresh your memory with the last comment")})
        goto(originState);
    }
    this.onResponse<No> {goto(frustratedUserFarewell)}
    this.onResponse<UnwillingToContinue> {goto(frustratedUserFarewell)}
}

fun UserCheerUpPractice(originState: State): State = state(Interaction) {
    onEntry {
        if(originState.name==="EasyExercises")random({furhat.say("You are just in the beginning, don't give up now!")},
            {furhat.say("The beginning of practice is the most difficult step, don't worry, trust me!")},
            {furhat.say("Don't worry, it is normal to be wrong at the beginning.")})
        else if(originState.name==="MediumExercises")random({furhat.say("Things are getting harder but you have already overcome the easy exercises, don't give up!")},
            {furhat.say("Medium exercises are more difficult, I know, but trust me, you will rapidly know how to solve them.")},
            {furhat.say("Don't worry, we are just practicing, it's normal to get frustrated.")})
        else if(originState.name==="HardExercises")random({furhat.say("You are almost ready to master the division operation, don't give up now!")},
            {furhat.say("This kind of exercise are challenging, it is normal to be wrong sometimes.")},
            {furhat.say("You already know a lot about division, let's take this final step, you are almost there.")})
        else{
            random({furhat.say("I understand your frustation, but I am here to help you, let's make this work together!")},
                {furhat.say("It doesn't matter if you fail a thousand times, I am going to be here to explain it to you, don't worry.")},
                {furhat.say("Hey, it's normal to get frustrated, but it's more important to keep going!")},
                {furhat.say("You are doing great, don't worry.")})
        }
        furhat.ask("Let's continue, alright?")
    }
    this.onResponse<Yes> {
        furhat.say("Great, let's keep practicing then" )
        goto(originState);
    }
    this.onNoResponse {
        furhat.say("Let's continue with the practice.'" )
        goto(originState);
    }
    this.onResponse<No> {goto(frustratedUserFarewell)}
    this.onResponse<UnwillingToContinue> {goto(frustratedUserFarewell)}
}

fun UserCheerUpExam(originState: State): State = state(Interaction) {
    onEntry {
        if(originState.name==="BeginExam"){
            random({furhat.say("I know you are tired, but this is the final step, come on.")},
                {furhat.say("You already master the exercises, let's just continue with this exam and we are done.")},
                {furhat.say("One last effort, you are doing great.")})
            furhat.ask("Let's continue, alright?")
        }
        else if(originState.name==="ExamEnd")
            furhat.ask("It's important to understand the division completely. I am going to ask you again, do you want to continue?" )
        else{
            random({furhat.say("You are in the middle of the exam, let's just finish it.")},
                {furhat.say("I know you are tired, come one we are almost at the end.")},
                {furhat.say("One last effort, come on, you are almost finished.")})
            furhat.ask("Let's continue, alright?")
        }

    }
    this.onResponse<Yes> {
        furhat.say("Great, let's continue then" )
        goto(originState);
    }
    this.onNoResponse {
        furhat.say("Let's continue with the session" )
        goto(originState);
    }
    this.onResponse<No> {goto(frustratedUserFarewell)}
    this.onResponse<UnwillingToContinue> {goto(frustratedUserFarewell)}
}

val frustratedUserFarewell: State = state(Interaction) {
    onEntry {
        random({
            furhat.say("If you don't want to continue, I am not going to force you. Goodbye!")
        },{
            furhat.say("Well, if you don't want to learn it's okay, we will continue another day. Goodbye!")
        })
        terminate()
    }
}
