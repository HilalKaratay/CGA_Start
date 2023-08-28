package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import org.joml.Vector3f

class SpawnTiere() {
    val listeTiere= mutableListOf<Tier>()

    val hund1 = Hund(-50f)
    val katze1 = Katze (-70f)
    val sheep = Schaf (-70f)
    val sheep1 = Schaf(-70f)


    fun spawn(){
        if (hund1.spawn()) listeTiere.add(hund1)
        if(katze1.spawn()) listeTiere.add(katze1)
        if(sheep.spawn()) listeTiere.add(sheep)
        if(sheep1.spawn()) listeTiere.add(sheep1)
    }

    fun move(dt:Float){
        hund1.move(dt)
        katze1.move(dt)
        sheep.move(dt)

    }

    fun getRenderList():MutableList<Renderable?>{
        val renderlist=mutableListOf<Renderable?>()
        for (i in listeTiere){
            renderlist.add(i.model)
        }
        return renderlist
    }

}