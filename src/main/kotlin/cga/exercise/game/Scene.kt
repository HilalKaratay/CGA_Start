import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.CubemapTexture
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader.loadModel
import cga.framework.OBJLoader.loadOBJ
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30

/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {

    private val bikeColBox = Transformable()

    private var figur = loadModel("assets/models/Figur/figur.obj", Math.toRadians(0f), Math.toRadians(0f), 0f) ?: throw IllegalArgumentException("Could not load the model")

    //Shader
    private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    private val skyboxShader: ShaderProgram


    //Skybox / Cubemap
    // Define Vertices and Indices of Cubemap
    private var size: Float = -20.00f
    private var skyboxVertices: FloatArray = floatArrayOf(
        -size, -size, size,
        size, -size, size,
        size, -size, -size,
        -size, -size, -size,
        -size, size, size,
        size, size, size,
        size, size, -size,
        -size, size, -size
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


    private val ground: Renderable



    /**Modelle**/
    //private val figur: Renderable
    private val baum: Renderable
    private val baum2: Renderable
    private val blume: Renderable
    private val laterne: Renderable
    private val stein: Renderable
    private val stein2: Renderable
    private val brücke: Renderable
    private val haus: Renderable
    private val gras: Renderable
    private val gras2: Renderable

    private val groundMaterial: Material
    private val groundColor: Vector3f

    //Lights
    private val bikeSpotLight: SpotLight
    private val bikePointLight: PointLight

    private val bikePointLight2: PointLight
    private val bikePointLight3: PointLight

    //camera
    private val camera: TronCamera


    private val cameraZoom: TronCamera
    private val cameraOben: TronCamera

    private var cameraHinten : TronCamera

    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true

    var jumps = 0

    // Player movement
    var left = false


    //scene setup
    init {
        skyboxShader = ShaderProgram("assets/shaders/skybox_vert.glsl", "assets/shaders/skybox_frag.glsl")

        // Loading Cubemap faces
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



        //load an object and create a mesh
        val gres = loadOBJ("assets/models/ground.obj")



        //load textures
        val groundDiff = Texture2D("assets/textures/grass2.png", true)
        groundDiff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundSpecular = Texture2D("assets/textures/grass2.png", true)
        groundSpecular.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundEmit = Texture2D("assets/textures/grass2.png", true)
        groundEmit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        groundMaterial = Material(groundDiff, groundEmit, groundSpecular, 60f, Vector2f(64.0f, 64.0f))

        bikeColBox.parent = figur
        bikeColBox.translate(Vector3f(0.5f,0.25f,-1.5f))

        //Create the mesh
        val stride = 8 * 4
        val atr1 = VertexAttribute(3, GL_FLOAT, stride, 0)     //position attribute
        val atr2 = VertexAttribute(2, GL_FLOAT, stride, 3 * 4) //texture coordinate attribute
        val atr3 = VertexAttribute(3, GL_FLOAT, stride, 5 * 4) //normal attribute
        val vertexAttributes = arrayOf(atr1, atr2, atr3)



        //Create renderable
        ground = Renderable()
        for (m in gres.objects[0].meshes) {
            val mesh = Mesh(m.vertexData, m.indexData, vertexAttributes, groundMaterial)
            ground.meshes.add(mesh)
        }


       figur = loadModel("assets/models/Figur/figur.obj", Math.toRadians(1f), Math.toRadians(-180f), 0f) ?: throw IllegalArgumentException("Could not load the model")

        baum = loadModel("assets/models/Baum/Baum.obj", Math.toRadians(-90.0f), Math.toRadians(90.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")

        blume = loadModel("assets/models/Blume/blume.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        blume.translate(Vector3f(10f, 0f, 0f))
        baum2 = loadModel("assets/models/Baum_2/baum_2.obj" ,Math.toRadians(-0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        baum2.translate(Vector3f(-10f, 0f, 0f))
        laterne = loadModel("assets/models/Laterne/Laterne.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        laterne.translate(Vector3f(13f, 0f, 0f))
        stein = loadModel("assets/models/Stein/stein.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        stein.translate(Vector3f(6f, 0f, 0f))
        stein2 = loadModel("assets/models/Stein2/stein2.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        stein2.translate(Vector3f(-8f, 0f, 0f))
        brücke = loadModel("assets/models/Brücke/Brücke.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        brücke.translate(Vector3f(-4f, 0f, 0f))
        haus = loadModel("assets/models/Haus/Haus.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        haus.translate(Vector3f(0f, 0f, 0f))
        gras = loadModel("assets/models/Gras/Gras.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        gras.translate(Vector3f(5f, 0f, 0f))
        gras2 = loadModel("assets/models/Gras2/Gras2.obj" ,Math.toRadians(0.0f), Math.toRadians(0.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        gras2.translate(Vector3f(-3f, 0f, 0f))

        //setup camera
        // Oben Ansicht
        cameraOben = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            1000.0f
        )
        cameraOben.rotate(0f,Math.toRadians(1f),0f)
        cameraOben.rotate(Math.toRadians(-80f),0f,0f)
        cameraOben.translate(Vector3f(0f,0f,5f))

        // Zoom Ansicht
        cameraZoom = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            1000.0f
        )
        cameraZoom.rotate(0f,Math.toRadians(1f),0f)
        cameraZoom.rotate(Math.toRadians(-50f),0f,0f)
        cameraZoom.translate(Vector3f(0f,1f,0.5f))

        // Hinten Weitwinkel Ansicht
        cameraHinten = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(90.0f),
            0.1f,
            1000.0f
        )
        cameraHinten.rotate(Math.toRadians(-145.0f), 0.0f, Math.toRadians(180.0f))
        cameraHinten.translate(Vector3f(0.0f, 0.0f, 8.0f))

        // Standart Kamera
        camera = TronCamera(
            custom(window.framebufferWidth, window.framebufferHeight),
            Math.toRadians(120.0f),
            0.1f,
            1000.0f
        )
        camera.rotate(0f,Math.toRadians(1f),0f)
        camera.rotate(Math.toRadians(-30f),0f,0f)
        camera.translate(Vector3f(0f,0f,2.5f))

        // Parent
        cameraOben.parent = figur
        cameraZoom.parent = figur
        camera.parent = figur
        cameraHinten.parent = figur

        // BODEN FARBE
        groundColor = Vector3f(1.0f, 1.0f, 1.0f)

        //bike point light
        bikePointLight = PointLight("bikePointLight", Vector3f(0.0f, 2.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        bikePointLight.parent = figur
        //bike spot light
        bikeSpotLight = SpotLight("bikeSpotLight", Vector3f(3.0f, 3.0f, 3.0f), Vector3f(0.0f, 1.0f, -2.0f), Math.toRadians(20.0f), Math.toRadians(30.0f))
        bikeSpotLight.rotate(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        bikeSpotLight.parent = figur

        bikePointLight2 = PointLight("bikePointLight2", Vector3f(0.0f, 2.0f, 2.0f), Vector3f(-10.0f, 2.0f, -10.0f))
        bikePointLight3 = PointLight("bikePointLight3", Vector3f(2.0f, 0.0f, 0.0f), Vector3f(10.0f, 2.0f, 10.0f))

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        //glDisable(GL_CULL_FACE); GLError.checkThrow()
        //glEnable(GL_CULL_FACE)
        //glFrontFace(GL_CCW); GLError.checkThrow()
        //glCullFace(GL_BACK); GLError.checkThrow()
       // glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()
    }

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // Skybox render
        glDepthFunc(GL_LEQUAL)
        skyboxShader.use()

        skyboxShader.setUniform("view", camera.calculateViewMatrix(), false)
        skyboxShader.setUniform("projection", camera.calculateProjectionMatrix(), false)

        GL30.glBindVertexArray(cubeMap.skyboxVAO)
        GL30.glActiveTexture(GL30.GL_TEXTURE0)
        glBindTexture(GL30.GL_TEXTURE_CUBE_MAP, cubeMapTexture)
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0)

        GL30.glBindVertexArray(0);
        glDepthFunc(GL_LESS);



        staticShader.use()

        ground.render(staticShader)
        // bind shaders to camera
        if(window.getKeyState(GLFW_KEY_0)) {
            cameraZoom.bind(staticShader)
        }
        else if (window.getKeyState(GLFW_KEY_1)){
            cameraHinten.bind(staticShader)
        }
        else {
            camera.bind(staticShader)
        }


        val changingColor = Vector3f(1.0f, 1.0f, 1.0f)
        staticShader.setUniform("shadingColor", groundColor)
        ground.render(staticShader)
        staticShader.setUniform("shadingColor",changingColor)


        //bikePointLight.lightColor = changingColor

        bikePointLight.bind(staticShader)
        bikePointLight2.bind(staticShader)
        bikePointLight3.bind(staticShader)
        bikeSpotLight.bind(staticShader, camera.calculateViewMatrix())
        //bike.render(staticShader)
        //bike.render(skyboxShader)


        baum.render(staticShader)
        stein.render(staticShader)
        blume.render(staticShader)
        //haus.render(staticShader)
        baum2.render(staticShader)
        laterne.render(staticShader)
        blume.render(staticShader)


    }

    fun update(dt: Float, t: Float) {
        if(window.getKeyState(GLFW_KEY_0)) { }
        if(window.getKeyState(GLFW_KEY_1)) { }

        val moveMul = 5.0f
        val rotateMul = 0.5f * Math.PI.toFloat()
        if (window.getKeyState(GLFW_KEY_W)) {
            //bike.translate(Vector3f(0.0f, 0.0f, -dt * moveMul))
            figur.translate(Vector3f(0.0f, 0.0f, -dt * moveMul))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            //bike.translate(Vector3f(0.0f, 0.0f, dt * moveMul))
            figur.translate(Vector3f(0.0f, 0.0f, dt * moveMul))
        }
        if (window.getKeyState(GLFW_KEY_A) and window.getKeyState(GLFW_KEY_W)) {
            //bike.rotate(0.0f, dt * rotateMul, 0.0f)
            figur.rotate(0.0f, dt * rotateMul, 0.0f)
        }
        if (window.getKeyState(GLFW_KEY_D) and window.getKeyState(GLFW_KEY_W)) {
            //bike.rotate(0.0f, -dt * rotateMul, 0.0f)
            figur.rotate(0.0f, -dt * rotateMul, 0.0f)
        }
        if (window.getKeyState(GLFW_KEY_F)) {
            bikeSpotLight.rotate(Math.PI.toFloat() * dt, 0.0f, 0.0f)
        }

        //links
        if(window.getKeyState(GLFW_KEY_A)){

            figur.translate(Vector3f(0.0f,0.0f,0f*dt))
            figur.rotate(0.0f,2f*dt,0.0f)
        }
        //rechts
        if(window.getKeyState(GLFW_KEY_D)){
            //cycle.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
            //cycle.rotateLocal(0.0f,-1f*dt,0.0f)
            //bike.translate(Vector3f(0.0f,0.0f,0f*dt))
            //bike.rotate(0.0f,-2f*dt,0.0f)
            figur.translate(Vector3f(0.0f,0.0f,0f*dt))
            figur.rotate(0.0f,-2f*dt,0.0f)
        }

        /*
        //SPRINGEN
        if(window.getKeyState(GLFW_KEY_SPACE)&&jumps == 0){
            bike.translate(Vector3f(0.0f,1.0f,0f*dt))
            jumps = 1
        }

         */

        //STEIGEN
        if(window.getKeyState(GLFW_KEY_SPACE)){
            //bike.translate(Vector3f(0.0f,5.0f*dt,0.0f))
            figur.translate(Vector3f(0.0f,5.0f*dt,0.0f))
        }
        //SINKEN
        if(window.getKeyState(GLFW_KEY_LEFT_SHIFT)){
            //bike.translate(Vector3f(0.0f,-5.0f*dt,0.0f))
            figur.translate(Vector3f(0.0f,-5.0f*dt,0.0f))
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawAngle = (xpos - oldMouseX).toFloat() * 0.002f
            val pitchAngle = (ypos - oldMouseY).toFloat() * 0.0005f
            if (!window.getKeyState(GLFW_KEY_LEFT_ALT)) {
                //bike.rotate(0.0f, yawAngle, 0.0f)
                figur.rotate(0.0f, yawAngle, 0.0f)
            }
            else{
                camera.rotateAroundPoint(0.0f, yawAngle, 0.0f, Vector3f(0.0f, 0.0f, 0.0f))
            }
        } else firstMouseMove = false
        oldMouseX = xpos
        oldMouseY = ypos
    }

    fun cleanup() {}
}