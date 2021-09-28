package furhatos.app.spacereceptionist

import furhatos.app.spacereceptionist.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class SpacereceptionistSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}
class Starship  {
    val availablePlaces = 5
}

fun main(args: Array<String>) {
    Skill.main(args)
}
