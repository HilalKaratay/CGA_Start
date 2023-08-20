package cga.exercise.components.geometry


import org.joml.Vector2f
import shader.ShaderProgram
import texture.Texture2D

class Material (var diff: Texture2D,
                var emit: Texture2D,
                var specular: Texture2D,
                var shininess: Float = 50.0f,
                var tcMultiplier : Vector2f = Vector2f(1.0f)){

    fun bind(shaderProgram: ShaderProgram) {
        emit.bind(0)
        diff.bind(1)
        specular.bind(2)
        shaderProgram.setUniform("emit",0)
        shaderProgram.setUniform("diff",1)
        shaderProgram.setUniform("spec",2)
        shaderProgram.setUniform("shininess",shininess)
        shaderProgram.setUniform("tcMultiplier",tcMultiplier)
    }
}