
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.tiere.SpawnTiere

class Collision {

    fun checkCollision(t1: Transformable, box: ArrayList<Renderable> ): Boolean{
        return checkCollisionBox(t1, box)

    }

    fun checkCollisionBox(t1: Transformable, tList: ArrayList<Renderable>): Boolean {
        tList.forEach {
            if (t1.getWorldPosition().z + 1f >= it.getWorldPosition().z && it.getWorldPosition().z + 1f >= t1.getWorldPosition().z){
                if (t1.getWorldPosition().x + 1f >= it.getWorldPosition().x && it.getWorldPosition().x + 1f >= t1.getWorldPosition().x){
                    if (t1.getWorldPosition().y + 1f >= it.getWorldPosition().y && it.getWorldPosition().y + 1f >= t1.getWorldPosition().y) {
                        return true
                    }
                }
            }

        }
        return false
    }


}
