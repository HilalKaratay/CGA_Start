package cga.exercise.components.light


import shader.ShaderProgram


interface IPointLight {
    fun bind(shaderProgram: ShaderProgram, name:String)
}