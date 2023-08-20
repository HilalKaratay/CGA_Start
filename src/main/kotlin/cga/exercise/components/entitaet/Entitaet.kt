package cga.exercise.components.entitaet

import cga.exercise.components.collision.AABB
import cga.exercise.components.geometry.IRenderable
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.movementController.Movement
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f
import shader.ShaderProgram

open class Entity(var models: List<Renderable>, collisionBoxPath: String? = null) : Transformable(),
    IRenderable{
    constructor(model: Renderable, collisionBoxPath: String? = null) : this(
        listOf(model), collisionBoxPath
    )

    companion object {
        val allEntities = mutableListOf<Entity>()
    }

    open val movementSpeed: Float = 5f
    open val jumpSpeed = 8f
    open val weight = 1f
    open val height = 0f


    open val movementController: Movement? = Movement(this)
    val collisionBox: AABB? = collisionBoxPath?.let { AABB(this, ModelLoader.loadModel(it)) }

    init {
        models.forEach { it.parent = this }
        allEntities.add(this)

    }

    override fun render(shaderProgram: ShaderProgram) {
            collisionBox?.render(shaderProgram)
            for (model in models) {
                model.render(shaderProgram)
            }
    }

    open fun update(dt: Float, time: Float) {
            movementController?.update(dt, time)
            collisionBox?.setPosition(getPosition())
    }

    open fun onMouseMove(xDiff: Double, yDiff: Double) {
        rotate(0f, Math.toRadians((xDiff * 0.1f).toFloat()), 0f)
    }

    fun cleanUp() {
        models.forEach { it.cleanUp() }
    }

    fun movementCollision(): Boolean {
        if (this.collisionBox != null) {
        }

        return false
    }

    fun largestValueInVector(vec: Vector3f): Float {
        var largest = vec[0]
        if (vec[1] > largest) largest = vec[1]
        if (vec[2] > largest) largest = vec[2]
        return largest
    }
    fun checkForEntityCollision(): Boolean {
        allEntities.forEach {
            if (it.collisionBox != null && this != it) {
                val distance1 = it.collisionBox.minExtend.sub(this.collisionBox!!.maxExtend)
                val distance2 = this.collisionBox!!.minExtend.sub(it.collisionBox.maxExtend)
                val maxDistance = distance1.max(distance2)
                val maxValue = largestValueInVector(maxDistance)
                if (maxValue < 0) {
                    println("${this.javaClass} entity collision with ${it.javaClass}")
                    println("distance 1 $distance1")
                    println("distance 2 $distance2")
                    println("maxdistance $maxDistance")
                    println("maxvalue $maxValue")
                    return true
                }
            }
        }
        return false
    }



    fun moveForward(dt: Float): Boolean {
        return movementController?.moveForward(dt) ?: false
    }

    fun moveLeft(dt: Float): Boolean {
        return movementController?.moveLeft(dt) ?: false
    }

    fun moveRight(dt: Float): Boolean {
        return movementController?.moveRight(dt) ?: false
    }

    fun moveBack(dt: Float): Boolean {
        return movementController?.moveBack(dt) ?: false
    }

    fun moveDown(dt: Float): Boolean {
        return movementController?.moveDown(dt) ?: false
    }

    fun moveUp(dt: Float): Boolean {
        return movementController?.moveUp(dt) ?: false
    }


}