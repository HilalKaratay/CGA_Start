package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f
import shader.ShaderProgram

open class PointLight(private val name: String, var lightColor: Vector3f, position: Vector3f) : Transformable(), IPointLight {

    init {
        translate(position)
    }

    override fun bind(shaderProgram: ShaderProgram) {
        TODO("Not yet implemented")
    }

    /*
        override fun bind(shaderProgram: ShaderProgram) {
            shaderProgram.setUniform("$name.Color", lightColor)
            shaderProgram.setUniform("$name.Position", getWorldPosition())
        }*/
}