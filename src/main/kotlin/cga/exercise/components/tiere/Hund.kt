package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

class Hund(val spawnX: Float) : Tier () {
    override var isIdle = true
    override var nextSpawn = Random.nextInt(0, 5).toFloat()
    override val model: Renderable?
    override var radius = 2.3f
    override var speed = 0f
    override var speedA = 2
    override var speedB = 3



    init {
        model = ModelLoader.loadModel("assets/models/Hund/hund.obj", 0f, Math.toRadians(90.0).toFloat(), 0f)
        setSpeed()

    }

    override fun move(dt: Float) {
       // model?.translate(Vector3f(-speed* dt, 0f, 0.5f))

    }

}