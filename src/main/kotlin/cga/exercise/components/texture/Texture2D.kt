package texture

import cga.exercise.components.texture.ITexture
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.EXTTextureFilterAnisotropic
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

class Texture2D (imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean): ITexture {
    private var texID: Int = -1
        private set

    init {
        try {
            processTexture(imageData, width, height, genMipMaps)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
    companion object {
        //create texture from file
        //don't support compressed textures for now
        //instead stick to pngs
        operator fun invoke(path: String, genMipMaps: Boolean): Texture2D {
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val readChannels = BufferUtils.createIntBuffer(1)
            //flip y coordinate to make OpenGL happy
            STBImage.stbi_set_flip_vertically_on_load(false)
            val imageData = STBImage.stbi_load(path, x, y, readChannels, 4)
                ?: throw Exception("Image file \"" + path + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

            try {
                return Texture2D(imageData, x.get(), y.get(), genMipMaps)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                throw ex
            } finally {
                STBImage.stbi_image_free(imageData)
            }
        }
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) {
        texID= GL13.glGenTextures()
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, texID)
        GL13.glTexImage2D( GL13.GL_TEXTURE_2D, 0, GL13.GL_RGBA, width, height, 0, GL13.GL_RGBA, GL13.GL_UNSIGNED_BYTE, imageData)
        if(genMipMaps) {
            GL30.glGenerateMipmap(GL13.GL_TEXTURE_2D)
        }
        unbind()
    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int) {
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, texID)
        GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_WRAP_S, wrapS)
        GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_WRAP_T, wrapT)
        GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MAG_FILTER, magFilter)
        GL13.glTexParameteri(GL13.GL_TEXTURE_2D, GL13.GL_TEXTURE_MIN_FILTER, minFilter)

        GL11.glTexParameterf(GL13.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,16.0f)
        unbind()
    }

    override fun bind(textureUnit: Int) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, texID)
    }

    override fun unbind() {
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        unbind()
        if (texID != 0) {
            GL11.glDeleteTextures(texID)
            texID = 0
        }
    }
}
