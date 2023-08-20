package cga.exercise.components.light


import org.joml.Matrix4f
import shader.ShaderProgram

interface ISpotLight {
    fun bind(shaderProgram: ShaderProgram, viewMatrix: Matrix4f)
}