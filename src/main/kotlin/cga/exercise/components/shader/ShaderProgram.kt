package shader

import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

class ShaderProgram {

    private var vertexID : Int = 0
    private var fragmentID : Int = 0
    private var programID : Int = 0

    private val m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)

    fun use() {
        val curprog = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
        if (curprog != programID) GL20.glUseProgram(programID)
    }

    fun shader(vertexPath: String, fragmentPath: String) {
        val vPath = Paths.get(vertexPath)
        val fPath = Paths.get(fragmentPath)
        val vSource = String(Files.readAllBytes(vPath))
        val fSource = String(Files.readAllBytes(fPath))

        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        if (vertexID == 0) throw Exception ("Vertex shader object couldn't be created.")
        GL20.glShaderSource(vertexID, vSource)
        GL20.glCompileShader(vertexID)
        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID))
        }

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        if (fragmentID == 0) {
            GL20.glDeleteShader(vertexID)
            throw Exception ("Fragment shader object couldn't be created.")
        }
        GL20.glShaderSource(fragmentID, fSource)
        GL20.glCompileShader(fragmentID)
        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID))
        }

        programID = GL20.glCreateProgram()

        GL20.glAttachShader(programID, vertexID)
        GL20.glAttachShader(programID, fragmentID)

        GL20.glLinkProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID))
        }

        GL20.glDetachShader(programID, vertexID)
        GL20.glDetachShader(programID, fragmentID)

        GL20.glDeleteShader(vertexID)
        GL20.glDeleteShader(fragmentID)
    }

    fun setUniform(name: String, value: Matrix4f, transpose: Boolean): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniformMatrix4fv(loc, transpose, value.get(m4x4buf))
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Int):Boolean{
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1i(loc, value)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Float): Boolean {
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1f(loc, value)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Vector2f):Boolean{
        if (programID == 0) return false
        val loc = GL20.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform2f(loc, value.x, value.y)
            return true
        }
        return false
    }

}