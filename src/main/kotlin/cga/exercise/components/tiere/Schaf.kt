package cga.exercise.components.tiere

import cga.exercise.components.geometry.Renderable
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

class Schaf (val spawnX: Float) : Tier () {
    override var isIdle = true
    override var nextSpawn = Random.nextInt(0, 5).toFloat()
    override val model: Renderable?
    override var radius = 0.3f
    override var speed = 0f
    override var speedA = 2
    override var speedB = 3



    init {
        model = ModelLoader.loadModel("assets/models/Sheep/13574_Marco_Polo_Sheep_v1_L3.obj", Math.toRadians(-90.0).toFloat(), 0.0f, 0f)

        model?.translate((Vector3f(6f,0f,-14f)))
        model?.scale(Vector3f(0.013f, 0.013f, 0.013f))
        setSpeed()

    }

    override fun move(dt: Float) {
        model?.translate(Vector3f(-speed * dt, 0f, 0f))
    }





}