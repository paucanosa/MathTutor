package furhatos.app.spacereceptionist.flow.modules

import furhatos.app.spacereceptionist.flow.Interaction
import furhatos.app.spacereceptionist.flow.valence
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import org.apache.commons.math3.distribution.NormalDistribution

val FAILED_RESPONSES = listOf("Error", "No spoken result available", "Wolfram Alpha did not understand your input")
const val TIMEOUT = 4000 // 4 seconds
const val APP_ID = "V9AKQ2-J9UWTJWR97"

// State to conduct the query to the Emotion Inference API
fun inferEmotion() = state {
    onEntry {
        val query = "http://127.0.0.1:5000/api/v1/getEmotion"

        val response = call {
            khttp.get(query).text
        } as String

        // Reply to user depending on the returned response
        var reply = when {
            FAILED_RESPONSES.contains(response) -> {
                "Error"
            }
            else -> "$response"
        }

        users.current.valence = reply.split("~").toTypedArray()[1].toDouble()
        reply = reply.split("~").toTypedArray()[0]

        // Return the response
        terminate(reply)
    }

    onTime(TIMEOUT) {
        println("Issues connecting to Inference Server")
        terminate("Error")
    }
}

// State to conduct the query to the Wolframalpha API
fun generalQuestion(inputQuest: String,stateOrigin: State): State = state(Interaction){
    val BASE_URL = "https://api.wolframalpha.com/v1/spoken"
    var question = inputQuest.replace("+", " plus ").replace(" ", "+")
    var query = "$BASE_URL?i=$question&appid=$APP_ID"

    onEntry {
        furhat.ask("Do you want me to find the answer to that?")
    }

    this.onResponse<Yes> {

        // Filler speech and gesture
        furhat.say(async = true) {
            + "Okay let me think"
            + Gestures.GazeAway
        }

        val response = call {
            khttp.get(query).text
        } as String

        // Reply to user depending on the returned response
        val reply = when {
            FAILED_RESPONSES.contains(response) -> {
                "That's an interesting question"
            }
            else -> "$response"
        }
        furhat.say(response)
        furhat.say("Come on, let's continue with the lesson.")
        goto(stateOrigin)
    }

    this.onResponse<No> {
        furhat.say("Come on, let's continue with the lesson then.")
        goto(stateOrigin)
    }

    this.onResponse {

        // Filler speech and gesture
        furhat.say(async = true) {
            + "Okay let me think"
            + Gestures.GazeAway
        }

        val response = call {
            khttp.get(query).text
        } as String

        // Reply to user depending on the returned response
        val reply = when {
            FAILED_RESPONSES.contains(response) -> {
                "That's an interesting question"
            }
            else -> "$response"
        }
        furhat.say(response)
        furhat.say("Come on, let's continue with the lesson.")
        goto(stateOrigin)
    }


    //onTime(TIMEOUT) {
      //  println("Issues connecting to Inference Server")
        //terminate("Having Trouble Connecting")
    //}
}

// Custom Gesture
fun isHappyEmotion(emotion: String): Boolean {
    return emotion in arrayOf("Affection", "Confidence", "Engagement", "Esteem", "Excitement", "Happiness", "Peace", "Pleasure", "Sympathy")
}

fun isSadEmotion(emotion: String): Boolean {
    return !isHappyEmotion(emotion)
}

val LookAway = defineGesture("LookAway") {
    val nd : NormalDistribution = NormalDistribution(85.97, 120.24)

    var dur = nd.sample(1)[0]/1000
    var startTime = duration * 0.25
    var retainDur = duration * 0.75

    println("Duration of gesture is " + dur.toString())

    frame(startTime, retainDur) {
        BasicParams.NECK_PAN to -5
        BasicParams.NECK_TILT to 5
        BasicParams.GAZE_PAN to -5
        BasicParams.GAZE_TILT to -5
    }
    reset(dur)
}

// Random Filler Generator
fun addFillers(sentence:String, percent:Double = 0.1): String{

    val sentEndings = listOf<String>(".","?","!")

    val parts = sentence.split(" ").toMutableList()
    val numFPs = (0..(parts.size * percent).toInt()).random()

    val pos = mutableListOf<Int>()
    for (idx in (0 until numFPs)){
        pos.add(idx, (0 until parts.size).random())
    }

    for (idx in pos){
        if (idx == 0 || sentEndings.contains(parts[idx - 1].last().toString())){
            parts.add(idx, "um")
        } else {
            parts.add(idx, "uh")
        }
    }

    return parts.joinToString(" ")
}