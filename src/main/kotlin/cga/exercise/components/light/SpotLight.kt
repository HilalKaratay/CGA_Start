package cga.exercise.components.light

import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f

import shader.ShaderProgram

class SpotLight(pos : Vector3f, col : Vector3f, attenuation : Vector3f, private val innerAngle : Float, private val outerAngle : Float) : PointLight(pos, col,attenuation), ISpotLight {


    override fun bind(shaderProgram: ShaderProgram, viewMatrix: Matrix4f) {
        TODO("Not yet implemented")
    }
}