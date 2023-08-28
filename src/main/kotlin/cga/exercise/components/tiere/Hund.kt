package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

class Hund(val spawnX: Float) : Tier () {

    override val model: Renderable?
    override var radius = 2.3f
    override var speed = 0f

    init {
        model = ModelLoader.loadModel("assets/models/Hund/hund.obj", 0f, Math.toRadians(180.0).toFloat(), 0f)
        model?.translate((Vector3f(9f,0f,-5f)))
    }

    override fun move(dt: Float) {
       model?.translate(Vector3f(0f, 0f, speed* dt))

    }

}