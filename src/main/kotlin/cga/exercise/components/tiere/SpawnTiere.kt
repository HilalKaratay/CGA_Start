package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import org.joml.Vector3f

class SpawnTiere() {
    val listeTiere= mutableListOf<Tier>()

    val hund1 = Hund(-50f)
    val hund2 =Hund(-50f)
    val hund3 =Hund(-50f)
    val hund4 =Hund(-50f)
    val hund5 =Hund(-50f)

    val katze1 = Katze (-70f)
    val sheep = Schaf (-70f)
    val sheep1 = Schaf(-70f)


    fun spawn(t :Float){
        if (hund1.spawn(t)) listeTiere.add(hund1)
     //   if (hund2.spawn(t)) listeTiere.add(hund2)
       // if (hund3.spawn(t)) listeTiere.add(hund3)
       // if (hund4.spawn(t)) listeTiere.add(hund4)
        //if (hund5.spawn(t)) listeTiere.add(hund5)
        if(katze1.spawn(t)) listeTiere.add(katze1)
        if(sheep.spawn(t)) listeTiere.add(sheep)
        if(sheep1.spawn(t)) listeTiere.add(sheep1)
    }

    fun move(dt:Float,t:Float){
        hund1.move(dt)
       // hund2.move(dt)
       // hund3.move(dt)
        //hund4.move(dt)
       // hund5.move(dt)
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