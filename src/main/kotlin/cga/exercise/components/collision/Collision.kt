package cga.exercise.components.collision

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.GL11


import cga.exercise.components.geometry.Mesh

import kotlin.math.min

/*
class Collision {

    fun checkCollision(t1: Transformable, box: ArrayList<Renderable>): Boolean{
        return checkCollisionBikeBox(t1, box)
    }

    fun checkCollisionBikeBox(t1: Transformable, tList: ArrayList<Renderable>): Boolean {
        tList.forEach {
            if (t1.getWorldPosition().z + 3f >= it.getWorldPosition().z && it.getWorldPosition().z + 1f >= t1.getWorldPosition().z){
                if (t1.getWorldPosition().x + 1f >= it.getWorldPosition().x && it.getWorldPosition().x + 4.5f >= t1.getWorldPosition().x){
                    if (t1.getWorldPosition().y + 1f >= it.getWorldPosition().y && it.getWorldPosition().y + 1.5f >= t1.getWorldPosition().y) {
                        return true
                    }
                }
            }
        }
        return false
    }


    fun checkCollisionBikePowerUp(t1: Transformable, tObj: Renderable): Boolean {
        if (t1.getWorldPosition().z + 3f >= tObj.getWorldPosition().z && tObj.getWorldPosition().z + 1f >= t1.getWorldPosition().z){
            if (t1.getWorldPosition().x + 1f >= tObj.getWorldPosition().x && tObj.getWorldPosition().x + 2f >= t1.getWorldPosition().x){
                if (t1.getWorldPosition().y + 1f >= tObj.getWorldPosition().y && tObj.getWorldPosition().y + 1.5f >= t1.getWorldPosition().y) {
                    return true
                }
            }
        }
        return false
    }
}
*/

class AABB(val parent: Transformable, val boundingBox: Renderable) {

    companion object {

        fun toggleHitbox(){
            showHitbox = !showHitbox
        }

        var showHitbox = false
    }
/*
    val minExtend: Vector3f
        get() = parent.getWorldPosition().add(boundingBox.meshes[0].minVertex())

    val maxExtend: Vector3f
        get() = parent.getWorldPosition().add(boundingBox.meshes[0].maxVertex())
*/

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