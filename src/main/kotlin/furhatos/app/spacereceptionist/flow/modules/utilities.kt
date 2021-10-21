package furhatos.app.spacereceptionist.flow.modules

import furhatos.app.spacereceptionist.flow.Interaction
import furhatos.app.spacereceptionist.flow.valence
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import furhatos.nlu.common.No
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
    var generalQuestion = inputQuest
    onEntry {
        val BASE_URL = "https://api.wolframalpha.com/v1/spoken"
        val question = generalQuestion.replace("+", " plus ").replace(" ", "+")
        val query = "$BASE_URL?i=$question&appid=$APP_ID"

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
        // Return the response
        //terminate(reply)
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
fun addFillers(sentence:String, percent:Double = 0.2): String{

    val sentEndings = listOf<String>(".","?","!")

    val parts = sentence.split(" ").toMutableList()
    val numFPs = (0..(parts.size * percent).toInt()).random()

    val pos = mutableListOf<Int>()
    for (idx in (0 until numFPs)){
        pos.add(idx, (0 until parts.size).random())
    }

    for (idx in pos){
        if (idx == 0 || sentEndings.contains(parts[idx - 1].last().toString())){
            parts.add(idx, "u:m")
        } else {
            parts.add(idx, "u:h")
        }
    }

    return parts.joinToString(" ")
}