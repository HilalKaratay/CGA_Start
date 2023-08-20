package cga.exercise.components.entitaet

import cga.exercise.movementController.Movement
import cga.framework.ModelLoader
import game.Scene
import texture.Texture2D

class Hund(scene: Scene): Entity(ModelLoader.loadModel(filepath)) {

    override val movementController: Movement = Movement(this)

    override val movementSpeed: Float = 3f
    override val weight = 1.5f
    override val jumpSpeed = 4f

    companion object {

        val filepath = "\"project/assets/models/Hund/hund.obj"
        private val hitbox = "project/assets/animals/hund.mtl"
        val image = Texture2D.invoke("project/assets/models/Hund/Texture_1.png", true)

    }

    override fun update(dt: Float, time: Float) {
        super.update(dt, time)
        movementController.update(dt, time)
    }
}