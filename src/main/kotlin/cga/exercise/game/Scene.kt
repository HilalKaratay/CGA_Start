package game


import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.texture.CubemapTexture
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader.loadModel
import cga.framework.OBJLoader
import cga.framework.OBJLoader.loadOBJ
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30
import shader.ShaderProgram
import texture.Texture2D
import java.awt.SystemColor.window

class Scene (private val WINDOW: GameWindow) {


    /**Skybox**/
    // Define Vertices and Indices of Cubemap
    private var size: Float = -20.00f
    private var skyboxVertices: FloatArray = floatArrayOf(
        -size, -size,  size,
         size, -size,  size,
         size, -size, -size,
        -size, -size, -size,
        -size,  size,  size,
         size,  size,  size,
         size,  size, -size,
        -size,  size, -size
    )

    private var skyboxIndices: IntArray = intArrayOf(
        //right
        1, 2, 6,
        6, 5, 1,
        //left
        0, 4, 7,
        7, 3, 0,
        //top
        4, 5, 6,
        6, 7, 4,
        //bottom
        0, 3, 2,
        2, 1, 0,
        //back
        0, 1, 5,
        5, 4, 0,
        //front
        3, 7, 6,
        6, 2, 3
    )

    private var cubeMap = CubemapTexture(skyboxVertices, skyboxIndices)
    private var cubeMapTexture = glGenTextures()


    /**Shader**/
    val staticShader = ShaderProgram()
    val skyboxShader = ShaderProgram()

    /**Kamera**/
    val camera = TronCamera()
    var cameraZoom = TronCamera()

    /**Boden**/
    val meshBoden : Mesh


    /**Boden Texturen**/
    var texEmitBoden : Texture2D
    var texDiffBoden : Texture2D
    var texSpecBoden : Texture2D

    /**Modelle**/
    private val figur = loadModel("assets/models/Figur/figur.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val baum = loadModel("assets/models/Baum/Baum.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)
    private val baum2 = loadModel("assets/models/Baum_2/baum_2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val baum3 = loadModel("assets/models/Baum/Baum.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(0f), 0f)
    private val laterne = loadModel("assets/models/Laterne/Laterne.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val laterne2 = loadModel("assets/models/Laterne/Laterne.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val stein = loadModel("assets/models/Stein/stein.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val stein2 = loadModel("assets/models/Stein2/stein2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)

    private val blume = loadModel("assets/models/Blume/Blume.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val haus = loadModel("assets/models/Haus/Haus.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val hund = loadModel("assets/models/Hund/hund.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val katze = loadModel("assets/models/katze/katze.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val bank = loadModel("assets/models/Bank/bank.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val bank2 = loadModel("assets/models/Bank_2/bench2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val gras = loadModel("assets/models/Gras2/Gras2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)


    var lastX: Double = WINDOW.mousePos.xpos


    init {

        /**Shader**/
        staticShader.shader("assets/shaders/third_vert.glsl", "assets/shaders/third_frag.glsl")
        staticShader.use()

        skyboxShader.shader("assets/shaders/skybox_vert.glsl", "assets/shaders/skybox_frag.glsl")
        skyboxShader.use()

        /**Loading Skybox textur**/
        val facesCubeMap: ArrayList<String> = arrayListOf()
        facesCubeMap.addAll(
            listOf(
                "assets/textures/skybox/right.jpg",
                "assets/textures/skybox/left.jpg",
                "assets/textures/skybox/top.jpg",
                "assets/textures/skybox/bottom.jpg",
                "assets/textures/skybox/front.jpg",
                "assets/textures/skybox/back.jpg"
            )
        )

        cubeMapTexture = cubeMap.loadCubeMap(facesCubeMap)

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow() //Cull-Facing wurde aktiviert
        glFrontFace(GL_CCW); GLError.checkThrow() // Alle Dreiecke, die zur Kamera gerichtet sind, sind entgegen des Uhrzeigersinns definiert.
        glCullFace(GL_BACK); GLError.checkThrow() // Es werden alle Dreiecke verworfen, die nach hinten zeigen
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LEQUAL); GLError.checkThrow()


        /**load OBJ Object**/
        val attrPos = VertexAttribute(3, GL_FLOAT, 32, 0)
        val attrTC = VertexAttribute(2, GL_FLOAT, 32, 12)
        val attrNorm = VertexAttribute(3, GL_FLOAT, 32, 20)
        val vertexAttributes = arrayOf(attrPos, attrTC, attrNorm)

        /**Boden**/
        val resBoden = loadOBJ("assets/models/ground.obj")
        val objMeshBoden: OBJLoader.OBJMesh = resBoden.objects[0].meshes[0]

        texEmitBoden = Texture2D("assets/textures/boden_textur.png", true)
        texEmitBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR_MIPMAP_LINEAR)
        texDiffBoden = Texture2D("assets/textures/boden_textur.png", false)
        texDiffBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR, GL_LINEAR)
        texSpecBoden = Texture2D("assets/textures/boden_textur.png", false)
        texSpecBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR, GL_LINEAR)

        val matBoden = Material(texDiffBoden, texEmitBoden, texSpecBoden, 60.0f, Vector2f(64F, 64F))
        meshBoden = Mesh(objMeshBoden.vertexData, objMeshBoden.indexData, vertexAttributes, matBoden)

        /**Modelle unterschiedlich plazieren**/
        baum?.translate(Vector3f(-6f,0f,0f))
        baum2?.translate(Vector3f(5f,0f,-7f))
        baum3?.translate(Vector3f(10f,0f,-3f))
        laterne?.translate(Vector3f(4f,0f,3f))
        laterne2?.translate(Vector3f(-7f,0f,-4f))
        blume?.translate(Vector3f(-6f,0f,-5f))
        stein?.translate(Vector3f(-3f,0f,2f))
        stein2?.translate(Vector3f(7f,0f,2f))
        haus?.translate(Vector3f(-0f,0f,-5f))
        katze?.translate(Vector3f(-3f,0f,-3f))
        gras?.translate(Vector3f(-5f,0f,-3f))
        hund?.translate(Vector3f(4f,0f,-3f))
        bank?.translate(Vector3f(-6f,0f,-2f))
        bank2?.translate(Vector3f(6f,0f,-1f))

        /**Kamera**/
        camera.rotate(Math.toRadians((-20f).toDouble()).toFloat(), 0.0, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))
        camera.parent = figur

    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        /**Modell render**/
        figur?.render(staticShader)
        baum?.render(staticShader)
        baum2?.render(staticShader)
        baum3?.render(staticShader)
        blume?.render(staticShader)
        haus?.render(staticShader)
        katze?.render(staticShader)
        hund?.render(staticShader)
        stein?.render(staticShader)
        stein2?.render(staticShader)
        laterne?.render(staticShader)
        laterne2?.render(staticShader)
        bank?.render(staticShader)
        bank2?.render(staticShader)
        gras?.render(staticShader)
        meshBoden.render(staticShader)

        /**Skybox render**/
        glDepthFunc(GL_LEQUAL)
        skyboxShader.use()

        skyboxShader.setUniform("view", camera.getCalculateViewMatrix(), false)
        skyboxShader.setUniform("projection", camera.getCalculateProjectionMatrix(), false)

        glBindVertexArray(cubeMap.skyboxVAO)
        glActiveTexture(GL30.GL_TEXTURE0)
        glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, cubeMapTexture)
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0)

        glBindVertexArray(0);
        glDepthFunc(GL_LESS);

        camera.bind(staticShader)

    }

    fun update(dt: Float, t: Float) {


        if (WINDOW.getKeyState(GLFW_KEY_W)) {
            figur?.translate(Vector3f(0.0f, 0.0f, -100.0f*dt))
        }
        if (WINDOW.getKeyState(GLFW_KEY_S)) {
            figur?.translate(Vector3f(0.0f, 0.0f, 100.0f*dt))
        }

        if (WINDOW.getKeyState(GLFW_KEY_A)) {
            figur?.translate(Vector3f(-50.0f*dt, 0.0f, 0.0f))
        }
        if (WINDOW.getKeyState(GLFW_KEY_D)) {
            figur?.translate(Vector3f(50.0f*dt, 0.0f, 0.0f))
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        camera.rotateAroundPoint(0f, (lastX - xpos).toFloat() * 0.002f, 0f, Vector3f(0f, 0f, 0f))
        lastX = xpos
    }

    fun cleanup() {}

}