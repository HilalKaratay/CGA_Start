package cga.exercise.components.collision


import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import shader.ShaderProgram

class AABB(val parent: Transformable, val boundingBox: Renderable) {

    companion object {

        fun toggleHitbox(){
            showHitbox = !showHitbox
        }

        var showHitbox = false
    }

    val minExtend: Vector3f
        get() = parent.getWorldPosition().add(boundingBox.meshes[0].minVertex())

    val maxExtend: Vector3f
        get() = parent.getWorldPosition().add(boundingBox.meshes[0].maxVertex())


    fun setPosition(pos: Vector3f) {
        boundingBox.setPosition(pos)
    }

    fun render(shaderProgram: ShaderProgram) {

        if (showHitbox) {
            val prev = GL11.glGetInteger(GL11.GL_POLYGON_MODE)
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
            boundingBox.render(shaderProgram)
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, prev)
        }
    }

}