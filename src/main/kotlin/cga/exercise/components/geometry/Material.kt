package cga.exercise.components.geometry

import Texture2D
import cga.exercise.components.shader.ShaderProgram

import org.joml.Vector2f

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var specular: Texture2D,
               var shininess: Float = 50.0f,
               var tcMultiplier : Vector2f = Vector2f(1.0f)){

    fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("materialDiff", diff)
        shaderProgram.setUniform("materialSpec", specular)
        shaderProgram.setUniform("materialEmit", emit)
        shaderProgram.setUniform("materialShininess", shininess)
        shaderProgram.setUniform("tcMultiplier", tcMultiplier)
    }
}