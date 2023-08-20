package cga.exercise.components.camera

import org.joml.Matrix4f
import shader.ShaderProgram

interface ITron {

    fun getCalculateViewMatrix(): Matrix4f

    fun getCalculateProjectionMatrix(): Matrix4f

    fun bind(shader: ShaderProgram)

}