package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f
import shader.ShaderProgram
import java.awt.Color

open class PointLight(private val name: String, var lightColor: Vector3f, position: Vector3f) : Transformable(), IPointLight {

    private  var attenuation= Vector3f(1.0f,0f,0f)

    public fun Light(position: Vector3f,color: Color){
        this.getPosition()
    }

    init {
        translate(position)
    }



    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("$name.Color", lightColor)
        shaderProgram.setUniform("$name.Position", getWorldPosition())
    }
}