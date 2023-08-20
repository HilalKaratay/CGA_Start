import cga.exercise.components.geometry.Transformable
/*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.AMDSeamlessCubemapPerTexture
import org.lwjgl.stb.STBImage
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*


class Skybox (var vertices: FloatArray, var indices: IntArray): Transformable(){

    private var skyboxVAO = 0
    private var skyboxVBO = 0
    private var skyboxIBO = 0
    private var cubemapTextureID = -1

    init {
        skyboxVAO = glGenVertexArrays()
        skyboxVBO = glGenBuffers()
        skyboxIBO = glGenBuffers()

        glBindVertexArray(skyboxVAO)
        glBindBuffer(GL_ARRAY_BUFFER, skyboxVBO)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, skyboxIBO)

        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*4, 0)

    }

    fun loadCubemap(textures: ArrayList<String>){
        cubemapTextureID = glGenTextures()
        glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapTextureID)


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
}
*/

