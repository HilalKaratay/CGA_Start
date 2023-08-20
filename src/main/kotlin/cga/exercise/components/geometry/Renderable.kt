package cga.exercise.components.geometry



import shader.ShaderProgram

/**
 * Extends Transformable such that the object can render Mesh objects transformed by Transformable
 */


class Renderable(val meshes: MutableList<Mesh> = mutableListOf()) : Transformable(parent = null), IRenderable {

    override fun render(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (m in meshes) {
            m.render(shaderProgram)
        }
    }

    fun cleanUp(){
        for(mesh in meshes){
            mesh.cleanup()
        }
    }
}