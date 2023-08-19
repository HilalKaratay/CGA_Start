package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.lwjgl.opengl.GL30.*

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(
    var vertexdata: FloatArray,
    var indexdata: IntArray,
    attributes: Array<VertexAttribute>,
    private val material: Material
    ) {
    //private data
    private var vaoId = 0
    private var vboId = 0
    private var iboId = 0
    private var indexcount = 0


    private var minVertexPositions: Vector3f? = null
    private var maxVertexPositions: Vector3f? = null

    init {
        vaoId = glGenVertexArrays()
        if (vaoId == 0) {
            throw Exception("Vertex array object creation failed.")
        }
        vboId = glGenBuffers()
        if (vboId == 0) {
            glDeleteVertexArrays(vaoId)
            throw Exception("Vertex buffer creation failed.")
        }
        iboId = glGenBuffers()
        if (iboId == 0) {
            glDeleteVertexArrays(vaoId)
            glDeleteBuffers(vboId)
            throw Exception("Index buffer creation failed.")
        }
        glBindVertexArray(vaoId)
        //---------------------- VAO state setup start ----------------------
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId)
        //buffer data
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW)

        for (i in attributes.indices) {
            glEnableVertexAttribArray(i)
            glVertexAttribPointer(
                i,
                attributes[i].n,
                attributes[i].type,
                false,
                attributes[i].stride,
                attributes[i].offset
            )
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        //--------------------- VAO state setup end --------------------
        glBindVertexArray(0)
        indexcount = indexdata.size
    }

    //Only send the geometry to the gpu
    /**
     * renders the mesh
     */
    private fun render() {
        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, indexcount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.saveTU()
        material.bind(shaderProgram)
        render()
        shaderProgram.resetTU()
    }


//NEU
    fun minVertex(): Vector3f {

        if(minVertexPositions == null){
            minVertexPositions = Vector3f(minX(), minY(), minZ())
        }
        return minVertexPositions!!
    }

    private fun minX(): Float {
        var minValue = vertexdata[0]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 0 && value < minValue) minValue = value }
        return minValue
    }

    private fun minY(): Float {
        var minValue = vertexdata[1]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 1 && value < minValue) minValue = value }
        return minValue
    }

    private fun minZ(): Float {
        var minValue = vertexdata[2]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 2 && value < minValue) minValue = value }
        return minValue
    }

    fun maxVertex(): Vector3f {
        if(maxVertexPositions == null){
            maxVertexPositions = Vector3f(maxX(), maxY(),maxZ())
        }
        return maxVertexPositions!!
    }

    private fun maxX(): Float {
        var maxValue = vertexdata[0]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 0 && value > maxValue) maxValue = value }
        return maxValue
    }

    private fun maxY(): Float {
        var maxValue = vertexdata[1]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 1 && value > maxValue) maxValue = value }
        return maxValue
    }

    private fun maxZ(): Float {
        var maxValue = vertexdata[2]
        vertexdata.forEachIndexed { index, value -> if (index % 8 == 2 && value > maxValue) maxValue = value }
        return maxValue
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (iboId != 0) glDeleteBuffers(iboId)
        if (vboId != 0) glDeleteBuffers(vboId)
        if (vaoId != 0) glDeleteVertexArrays(vaoId)
    }
}