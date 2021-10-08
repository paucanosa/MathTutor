package furhatos.app.spacereceptionist.flow
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

var User.name by NullSafeUserDataDelegate { "" }
var User.userLikesMaths by NullSafeUserDataDelegate { "" }
var User.neededExplanations by NullSafeUserDataDelegate { 0 }
var User.reviewedExplanations   by NullSafeUserDataDelegate { intArrayOf()}
var User.currentFrustration by NullSafeUserDataDelegate { -1 }
var User.failedExercises   by NullSafeUserDataDelegate { intArrayOf()}
var User.passedExercises  by NullSafeUserDataDelegate { intArrayOf()}
var User.attemptedExercises by NullSafeUserDataDelegate { intArrayOf()}



