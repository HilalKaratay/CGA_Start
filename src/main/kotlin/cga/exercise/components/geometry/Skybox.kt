import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix3f
import org.joml.Matrix4f

import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*

class Skybox {

    private var skyboxVAO = 0
    private var skyboxVBO = 0
    private var skyboxIBO = 0
    private var cubemapTexture = -1

    private var skyboxVertices: FloatArray = floatArrayOf(
        -1f, -1f, 1f,
        1f, -1f, 1f,
        1f, -1f, -1f,
        -1f, -1f, -1f,
        -1f, 1f, 1f,
        1f, 1f, 1f,
        1f, 1f, -1f,
        -1f, 1f, -1f
    )

    private var skyboxIndices: IntArray = intArrayOf(
        //Rechts
        1, 2, 6,
        6, 5, 1,
        //Links
        0, 4, 7,
        7, 3, 0,
        //Oben
        4, 5, 6,
        6, 7, 4,
        //Unten
        0, 3, 2,
        2, 1, 0,
        //Hinten
        0, 1, 5,
        5, 4, 0,
        //Vorne
        3, 7, 6,
        6, 2, 3
    )



    init {
        skyboxVAO = glGenVertexArrays()
        skyboxVBO = glGenBuffers()
        skyboxIBO = glGenBuffers()

        glBindVertexArray(skyboxVAO)
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, skyboxIBO)

        glBufferData(GL_ARRAY_BUFFER, skyboxVertices, GL_STATIC_DRAW)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, skyboxIndices, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*4, 0)



    }

    fun loadCubemap(textures: ArrayList<String>){
        cubemapTexture = glGenTextures()
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTexture)


        for (i in 0 until 6){
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val nrChannels = BufferUtils.createIntBuffer(1)
            STBImage.stbi_set_flip_vertically_on_load(false)
            val data = STBImage.stbi_load(textures[i], x, y, nrChannels, 0)
                ?: throw Exception("Image file \"" + textures[i] + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

            val width = x.get()
            val height = y.get()
            GL11.glTexImage2D(
                GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                0,
                GL_RGB,
                width,
                height,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                data
            )
            STBImage.stbi_image_free(data)

        }


        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE)
        glBindTexture(GL_TEXTURE_CUBE_MAP,0)
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun render(shader : ShaderProgram, view : Matrix4f, projection: Matrix4f){
        glActiveTexture(GL_TEXTURE0)
        glBindVertexArray(skyboxVAO)
        GL11.glDepthMask(false)
        shader.use()
        var newView = Matrix4f(Matrix3f(view))
        shader.setUniform("view", newView , false)
        shader.setUniform("projection", projection, false)

        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTexture)
        glDrawElements(GL_TRIANGLES, skyboxIndices.size, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)

        glBindVertexArray(0)
        GL11.glDepthMask(true)
    }


}