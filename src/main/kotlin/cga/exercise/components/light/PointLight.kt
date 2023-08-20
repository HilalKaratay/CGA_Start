package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f
import shader.ShaderProgram

open class PointLight(var pos : Vector3f, var col : Vector3f, var attenuation : Vector3f) : IPointLight, Transformable() {
    init {
        translateGlobal(pos)
    }
    override fun bind(shaderProgram: ShaderProgram, name: String) {
        val position = getWorldPosition()
        shaderProgram.setUniform(name + "Position", position.x, position.y, position.z)
        shaderProgram.setUniform(name + "Color", col.x, col.y, col.z)
        shaderProgram.setUniform(name + "AttenuationFactors", attenuation.x, attenuation.y, attenuation.z)
    }


}