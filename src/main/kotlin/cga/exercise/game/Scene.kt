
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.texture.CubemapTexture
import cga.exercise.components.tiere.SpawnTiere
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
import org.lwjgl.opengl.GL13
import shader.ShaderProgram
import texture.Texture2D


class Scene (private val WINDOW: GameWindow) {

    /**Skybox / Cubemap**/
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
    private var cubeMapNight =CubemapTexture(skyboxVertices,skyboxIndices)
    private var cubeMapTexture = glGenTextures()
    private var cubeMapNightTexture = glGenTextures()

    private var fogColour = 0

    /**Shader**/
    var tronShader =ShaderProgram()
    var staticShader = tronShader
    val toonShader = ShaderProgram()
    val secondShader = ShaderProgram()
    val skyboxShader = ShaderProgram()

    private var time = 6f

    /**Kamera**/
    val camera = TronCamera()
    val camerafirstperson = TronCamera()

    /**Collision**/
    val spawnTiere =SpawnTiere()
    val listOfFixObjects = arrayListOf<Renderable>()
    private val fixObjectList = RenderableList(listOfFixObjects)
    private var collision = false
    private val collisionChecker = Collision()
    var playerSpeed = 5f

    /**Licht**/
    private val pointLight: PointLight
    private val pointLightList = mutableListOf<PointLight>()

    private val bikeSpotLight :SpotLight
    private val spotLightList = mutableListOf<SpotLight>()

    /**Boden**/
    val meshBoden: Mesh

    /**Boden Texturen**/
    var texEmitBoden: Texture2D
    var texDiffBoden: Texture2D
    var texSpecBoden: Texture2D

    /**Modelle**/
    private val figur =
        loadModel("assets/models/Figur/figur.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val baum =
        loadModel("assets/models/Baum/Baum.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)
    private val baum2 =
        loadModel("assets/models/Baum_2/baum_2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val baum3 =
        loadModel("assets/models/Baum/Baum.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(0f), 0f)
    private val baum4 =
        loadModel("assets/models/Baum_2/baum_2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val laterne =
        loadModel("assets/models/Laterne/Laterne.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val laterne2 =
        loadModel("assets/models/Laterne/Laterne.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val laterne3 =
        loadModel("assets/models/Laterne/Laterne.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val stein =
        loadModel("assets/models/Stein/stein.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val stein2 =
        loadModel("assets/models/Stein2/stein2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(90f), 0f)
    private val blume =
        loadModel("assets/models/Blume/Blume.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val haus =
        loadModel("assets/models/Haus/Haus.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val hund =
        loadModel("assets/models/Hund/hund.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val katze =
        loadModel("assets/models/katze/katze.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val bank =
        loadModel("assets/models/Bank/bank.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(280f), 0f)
    private val bank2 =
        loadModel("assets/models/Bank2/bench2.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val brunnen =
        loadModel("assets/models/Brunnen/Brunnen.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(180f), 0f)
    private val busch =
        loadModel("assets/models/Busch/Busch.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val busch2 =
        loadModel("assets/models/Busch/Busch.obj", org.joml.Math.toRadians(0f), org.joml.Math.toRadians(0f), 0f)
    private val zaun=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(0f), 0f)
    private val zaun1=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(0f), 0f)
    private val zaun2=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(0f), 0f)
    private val zaunrechts1=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)
    private val zaunrechts2=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)
    private val zaunrechts3=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)
    private val zaunlinks1=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(270f), 0f)
    private val zaunlinks2=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(270f), 0f)
    private val zaunlinks3=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(270f), 0f)
    private val zaunhinten1=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(360f), 0f)
    private val zaunhinten2=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(360f), 0f)
    private val zaunhinten3=
        loadModel("assets/models/Zaun/13076_Gothic_Wood_Fence_Panel_v2_l3.obj",org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(360f), 0f)


    var lastX: Double = WINDOW.mousePos.xpos

    private var MouseX = 0.0
    private var MouseY = 0.0
    private var firstMouseMove = true

    init {
        /**Shader**/
        staticShader.shader("assets/shaders/third_vert.glsl", "assets/shaders/third_frag.glsl")
        staticShader.use()
        secondShader.shader("assets/shaders/second_vert.glsl", "assets/shaders/second_frag.glsl")
        toonShader.shader("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")
        skyboxShader.shader("assets/shaders/skybox_vert.glsl", "assets/shaders/skybox_frag.glsl")
        skyboxShader.use()


        /**Loading Skybox texture**/
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

        val facesCubeMapNight: ArrayList<String> = arrayListOf()
        facesCubeMapNight.addAll(
            listOf(
                "assets/textures/skybox/night/right.png",
                "assets/textures/skybox/night/left.png",
                "assets/textures/skybox/night/top.png",
                "assets/textures/skybox/night/bottom.png",
                "assets/textures/skybox/night/front.png",
                "assets/textures/skybox/night/back.png"
            )
        )

        cubeMapTexture = cubeMap.loadCubeMap(facesCubeMap)
        //glBindTexture(GL_TEXTURE_CUBE_MAP,cubeMapTexture)
        cubeMapNightTexture =cubeMapNight.loadCubeMap(facesCubeMapNight)

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

        texEmitBoden = Texture2D("assets/textures/grass2.png", true)
        texEmitBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR_MIPMAP_LINEAR)
        texDiffBoden = Texture2D("assets/textures/grass2.png", false)
        texDiffBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR, GL_LINEAR)
        texSpecBoden = Texture2D("assets/textures/grass2.png", false)
        texSpecBoden.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR, GL_LINEAR)

        val matBoden = Material(texDiffBoden, texEmitBoden, texSpecBoden, 60.0f, Vector2f(64F, 64F))
        meshBoden = Mesh(objMeshBoden.vertexData, objMeshBoden.indexData, vertexAttributes, matBoden)


        baum.translate(Vector3f(5f, 0f, -0f))
        baum2.translate(Vector3f(-6.5f, 0f, -2.5f))
        baum3.translate(Vector3f(-8f, 0f, -13f))
        baum4.translate(Vector3f(12f, 0f, -12f))
        busch.translate(Vector3f(2.2f, 0f, -9.3f))
        busch2.translate(Vector3f(-2.7f, 0f, -9.3f))
        laterne.translate(Vector3f(-5f, 0f, -8f))
        laterne2.translate(Vector3f(10f,0f,-7f))
        laterne3.translate(Vector3f(-3f,0f,1.9f))
        blume.translate(Vector3f(-5.5f, 0f, -2f))
        stein.translate(Vector3f(-7f, 0f, 5f))
        stein2.translate(Vector3f(7f, 0f, 2f))
        haus.translate(Vector3f(0f, 0f, -9f))
        katze.translate(Vector3f(-3f, 0f, -3f))
        hund.translate(Vector3f(-1f, 0f, -3f))
        bank.translate(Vector3f(-8f, 0f, -8f))
        bank2.translate(Vector3f(6f, 0f, -3f))
        brunnen.translate(Vector3f(0f, 0f, -3f))

        zaun.translate(Vector3f(5f,0f,-10f))
        zaun1.translate(Vector3f(7f,0f,-10f))
        zaun2.translate(Vector3f(9f,0f,-10f))
        zaunrechts1.translate(Vector3f(10f,0f,-11f))
        zaunrechts2.translate(Vector3f(10f,0f,-13f))
        zaunrechts3.translate(Vector3f(10f,0f,-15f))
        zaunlinks1.translate(Vector3f(4f,0f,-11f))
        zaunlinks2.translate(Vector3f(4f,0f,-13f))
        zaunlinks3.translate(Vector3f(4f,0f,-15f))
        zaunhinten1.translate(Vector3f(5f,0f,-16f))
        zaunhinten2.translate(Vector3f(7f,0f,-16f))
        zaunhinten3.translate(Vector3f(9f,0f,-16f))

        zaun.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaun1.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaun2.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunrechts1.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunrechts2.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunrechts3.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunlinks1.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunlinks2.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunlinks3.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunhinten1.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunhinten2.scale(Vector3f(0.01f, 0.01f, 0.01f))
        zaunhinten3.scale(Vector3f(0.01f, 0.01f, 0.01f))


        listOfFixObjects.add(baum)
        listOfFixObjects.add(baum2)
        listOfFixObjects.add(baum3)
        listOfFixObjects.add(baum4)
        listOfFixObjects.add(laterne)
        listOfFixObjects.add(laterne2)
        listOfFixObjects.add(laterne3)
        listOfFixObjects.add(blume)
        listOfFixObjects.add(busch)
        listOfFixObjects.add(busch2)
      //  listOfFixObjects.add(stein)
      //  listOfFixObjects.add(stein2)
        listOfFixObjects.add(haus)
      //  listOfFixObjects.add(katze)
      //  listOfFixObjects.add(hund)
        listOfFixObjects.add(bank)
        listOfFixObjects.add(bank2)
        listOfFixObjects.add(brunnen)
        listOfFixObjects.add(zaun)
        listOfFixObjects.add(zaun1)
        listOfFixObjects.add(zaun2)
        listOfFixObjects.add(zaunrechts1)
        listOfFixObjects.add(zaunrechts2)
        listOfFixObjects.add(zaunrechts3)
        listOfFixObjects.add(zaunlinks1)
        listOfFixObjects.add(zaunlinks2)
        listOfFixObjects.add(zaunlinks3)
        listOfFixObjects.add(zaunhinten1)
        listOfFixObjects.add(zaunhinten2)
        listOfFixObjects.add(zaunhinten3)


        /**Kamera**/
        camera.rotate(Math.toRadians(-40.0).toFloat(), 0.0f, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))

        camerafirstperson.rotate(Math.toRadians(-10.0).toFloat(),0f,0f)
        camerafirstperson.translate(Vector3f(0f,2f,-1f))

        camera.parent = figur
        camerafirstperson.parent = figur

        /**Licht**/
        pointLight = PointLight("pointLight[${pointLightList.size}]", Vector3f(0.0f, 10.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        pointLight.parent = figur
        pointLight.rotate(org.joml.Math.toRadians(-10.0f), 0.0f, 0.0f)

        bikeSpotLight = SpotLight("spotLight", Vector3f(3.0f, 3.0f, 3.0f), Vector3f(4f, 0f, 3f), org.joml.Math.toRadians(20.0f), org.joml.Math.toRadians(30.0f))
        bikeSpotLight.parent =hund
        bikeSpotLight.rotate(org.joml.Math.toRadians(-10.0f), 0.0f, 0.0f)

        pointLightList.add(PointLight("pointLight[${pointLightList.size}]", Vector3f(0.0f, 2.0f, 2.0f), Vector3f(4f, 0f, 3f)))
        pointLightList.add(PointLight("pointLight[${pointLightList.size}]", Vector3f(2.0f, 0.0f, 0.0f), Vector3f(4f, 0f, 3f)))
        spotLightList.add(SpotLight("spotLight[${spotLightList.size}]", Vector3f(10.0f, 300.0f, 300.0f), Vector3f(4f, 0f, 3f), org.joml.Math.toRadians(20.0f), org.joml.Math.toRadians(30.0f)))
        spotLightList.last().rotate(org.joml.Math.toRadians(20f), org.joml.Math.toRadians(60f), 0f)


    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        // setTimer(shaderWechsel)

        /**Modell render**/
        figur.render(staticShader)
        meshBoden.render(staticShader)
        fixObjectList.renderListOfObjects(staticShader)

        // pointLight.bind(staticShader)

        for (i in spawnTiere.getRenderList()) {
            i?.render(staticShader)
        }


        for (pointLight in pointLightList) {
            pointLight.bind(staticShader)
        }
        staticShader.setUniform("numPointLights", pointLightList.size)
        bikeSpotLight.bind(staticShader)

        /**Skybox render**/
        glDepthFunc(GL_LEQUAL)

        skyboxShader.use()
        skyboxShader.setUniform("view", camera.getCalculateViewMatrix(), false)
        skyboxShader.setUniform("projection", camera.getCalculateProjectionMatrix(), false)

        glBindVertexArray(cubeMap.skyboxVAO)
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0)
        bindTextures()
        glBindVertexArray(0)
        glDepthFunc(GL_LESS)

        //camera.bind(staticShader)
        if(WINDOW.getKeyState(GLFW_KEY_0)) {
            camerafirstperson.bind(staticShader)
        }
        else {
            camera.bind(staticShader)
        }
    }

    fun update(dt: Float, t: Float) {
        spawnTiere.move(dt,t)
        spawnTiere.spawn(t)

        if (WINDOW.getKeyState(GLFW_KEY_W)) {
            figur.translate(Vector3f(0.0f, 0.0f, -playerSpeed * dt))
        }
        if (WINDOW.getKeyState(GLFW_KEY_S)) {
            figur.translate(Vector3f(0.0f, 0.0f, playerSpeed * dt))
        }

        //links
        if (WINDOW.getKeyState(GLFW_KEY_A)) {
            figur.translate(Vector3f(-playerSpeed * dt, 0.0f, 0.0f))
        }
        //rechts
        if (WINDOW.getKeyState(GLFW_KEY_D)) {
            figur.translate(Vector3f(playerSpeed * dt, 0.0f, 0.0f))
        }


        if (collisionChecker.checkCollision(figur, listOfFixObjects)) {
            collision = true
            //playerSpeed = 0.2f //verlangsamt den Spieler
            //figur.translate(figur.getWorldPosition().mul(-1f)) Bringt die Figur auf Anfangsposition
            // playerSpeed = 1f
            // figur.translate(Vector3f(20.0f,0.0f,0.0f))
            println("collision detectet")
        }



    }


    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
        if (key == GLFW_KEY_1 && action == GLFW_PRESS) shaderToTronShaderChange()
        if(key == GLFW_KEY_2 && action == GLFW_PRESS) shadertoToonShaderChange()
        if(key == GLFW_KEY_3 && action == GLFW_PRESS) shaderToSecondShaderChange()
    }


    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawAngle = (xpos - MouseX).toFloat() * 0.002f
            if (!WINDOW.getKeyState(GLFW_KEY_LEFT_ALT)) {
                figur.rotate(0.0f, yawAngle, 0.0f)
            }
            else{
                camera.rotateAroundPoint(0.0f, yawAngle, 0.0f, Vector3f(0.0f, 0.0f, 0.0f))
            }
        } else firstMouseMove = false
        MouseX = xpos
        MouseY = ypos
    }

    //Zeitberchnen für shaderwechsel
    private fun bindTextures(){
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,cubeMapTexture)
        GL13.glActiveTexture(GL13.GL_TEXTURE1)
        glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,cubeMapNightTexture)

        time + 8.0f
        time %= 24f
        val dayTexture: Int
        val nightTexture: Int
        if (time >= 0 && time < 12) {
            dayTexture = cubeMapTexture
        } else if (time >= 13 && time < 24) {
            nightTexture = cubeMapNightTexture
        }else {
            nightTexture = cubeMapNightTexture
        }

    }


    fun shaderToTronShaderChange(){
        if (staticShader== toonShader|| staticShader==secondShader){
            staticShader=tronShader
            println("changed to TronShader")
        }else{
            staticShader= toonShader
            println("changed to ToonShader")
        }
    }
    fun shadertoToonShaderChange(){
        if (staticShader== tronShader){
            staticShader= toonShader
            println("changed to ToonShader")
        }else{
            staticShader= tronShader
            println("changed to TronShader")
        }
    }

    fun shaderToSecondShaderChange() {
        if (staticShader == toonShader || staticShader == tronShader) {
            staticShader = secondShader
            println("changed to SecondShader")
        } else {
            staticShader = tronShader
            println("changed to TronShader")
        }
    }
    fun cleanup() {}


}