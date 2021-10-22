package furhatos.app.spacereceptionist.flow



import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import org.apache.commons.math3.distribution.NormalDistribution

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