import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.stb.STBImage
import java.util.*

class Skybox {
    private var texID: Int = -1
        private set
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {

        var skyboxVertices = floatArrayOf(
            // positions
            -1f, -1f, -1f,
            1f, -1f, -1f,
            1f, 1f, -1f,
            -1f, 1f, -1f,
            -1f, -1f, 1f,
            1f, -1f, 1f,
            1f, 1f, 1f,
            -1f, 1f, 1f
        )

        var skyboxIndices = intArrayOf(
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

        indexcount = skyboxIndices.size

        // todo: generate IDs
        vao = GL30.glGenVertexArrays()
        vbo = GL15.glGenBuffers()
        ibo = GL15.glGenBuffers()
        // todo: bind your objects
        GL30.glBindVertexArray(vao)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo)
        // todo: upload your mesh data
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, skyboxVertices, GL15.GL_STATIC_DRAW)
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 12, 0)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, skyboxIndices, GL15.GL_STATIC_DRAW)
        GL30.glBindVertexArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun loadCubemap(faces : ArrayList<String>){
        texID = GL11.glGenTextures()
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID)

        var width = BufferUtils.createIntBuffer(1)
        var height = BufferUtils.createIntBuffer(1)
        var nrChannels = BufferUtils.createIntBuffer(1)

        for(i in 0 until faces.size)
        {
            STBImage.stbi_set_flip_vertically_on_load(false)
            val imageData = STBImage.stbi_load(faces[i], width, height, nrChannels, 4)
                ?: throw Exception("Image file \"" + faces[i] + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL13.GL_RGBA, width.get(), height.get(), 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE, imageData)
            width.clear()
            height.clear()
            nrChannels.clear()
            STBImage.stbi_image_free(imageData)
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_MIN_FILTER, GL13.GL_LINEAR)
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_MAG_FILTER, GL13.GL_LINEAR)
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL13.GL_TEXTURE_WRAP_R, GL13.GL_CLAMP_TO_EDGE)
    }

    fun render(shader : ShaderProgram, view : Matrix4f, projection: Matrix4f){
        GL11.glDepthMask(false)
        shader.use()
        var newView = Matrix4f(Matrix3f(view))
        shader.setUniform("view", newView , false)
        shader.setUniform("projection", projection, false)
        GL30.glBindVertexArray(vao)
        GL30.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID)
        GL11.glDrawElements(GL11.GL_TRIANGLES, indexcount, GL11.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
        GL11.glDepthMask(true)
    }
}