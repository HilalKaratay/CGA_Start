
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.RenderableList
import cga.exercise.components.geometry.Transformable
import cga.framework.OBJLoader
import kotlin.math.sqrt

class Collision {

    fun checkCollision(t1: Transformable, box: ArrayList<Renderable> ): Boolean{
        return checkCollisionBikeBox(t1, box)
    }

    fun checkCollisionBikeBox(t1: Transformable, tList: ArrayList<Renderable>): Boolean {
        tList.forEach {
            if (t1.getWorldPosition().z + 2f >= it.getWorldPosition().z && it.getWorldPosition().z + 1f >= t1.getWorldPosition().z){
                if (t1.getWorldPosition().x + 1f >= it.getWorldPosition().x && it.getWorldPosition().x + 2f >= t1.getWorldPosition().x){ //hier die breite für die unsichtbare box
                    if (t1.getWorldPosition().y + 1f >= it.getWorldPosition().y && it.getWorldPosition().y + 1.5f >= t1.getWorldPosition().y) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
