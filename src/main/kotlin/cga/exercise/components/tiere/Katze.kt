package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

class Katze (val spawnX: Float) : Tier (){
    override var isIdle = true
    override var createSpawn = Random.nextInt(0, 5).toFloat()
    override val model: Renderable?
    override var radius = 2.3f
    override var speed = 0.4f
    override var speedA = 1
    override var speedB = 2


    init {
        model = ModelLoader.loadModel("assets/models/katze/katze.obj", 0f, Math.toRadians(90.0).toFloat(), 0f)
        model?.translate((Vector3f(-3f,0f,-1f)))

    }

    override fun move(dt: Float) {
        model?.translate(Vector3f(-speed * dt, 0f, 0f))
    }


}