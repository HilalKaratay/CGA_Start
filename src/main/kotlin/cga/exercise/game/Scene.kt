package cga.exercise.game

import Skybox
import Texture2D
import cga.exercise.components.camera.Aspectratio.Companion.custom
import cga.exercise.components.camera.TronCamera

import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.ModelLoader.loadModel
import cga.framework.OBJLoader.loadOBJ
import org.joml.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL13.*
import kotlin.math.sqrt

/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
   private val staticShader: ShaderProgram


    //private val collisionChecker = Collision()
   // private val powerup = Renderable()
    private val bikeColBox = Transformable()

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

    /**Boden*/
    private val ground: Renderable
    private val groundMaterial: Material
    private val groundColor: Vector3f

    /**Licht**/
    private val bikePointLight: PointLight
    private val pointLightList = mutableListOf<PointLight>()

    private val bikeSpotLight: SpotLight
    private val spotLightList = mutableListOf<SpotLight>()

    /**Kamera**/
    private val camera: TronCamera
    private var oldMouseX = 0.0
    private var oldMouseY = 0.0
    private var firstMouseMove = true


    private var jump = 0
    private var jumpvector = 0.5f


    private var movementTimer = 0
    private var speedMultiplier:Double = 10.0
    private var collision = false

    val boxList = arrayListOf<Renderable>()

    //private val racoon: Enemy = Enemy(ModelLoader.loadModel("assets/models/Racoon/racoon.obj", 0f, 0f, 0f) as Renderable, true)
    private var figur = ModelLoader.loadModel("assets/models/Figur/figur.obj", Math.toRadians(0f), Math.toRadians(0f), 0f) ?: throw IllegalArgumentException("Could not load the model")

    var objectG = 2f
    var upwardsSpeed = 0f
    var enemyG = 0f

    /**Skybox**/
    private val skyboxShader: ShaderProgram
    private var skybox = Skybox()
    private var skyBoxTextures = ArrayList<String>()



    //scene setup
    init {

        /**Shader**/
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
        skyboxShader= ShaderProgram ("assets/shaders/skybox_vert.glsl", "assets/shaders/skybox_frag.glsl")



        //load textures
        val groundDifff = Texture2D("assets/textures/grass3.jpg", true)
        groundDifff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundSpecularr = Texture2D("assets/textures/grass3.jpg", true)
        groundSpecularr.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundEmitt = Texture2D("assets/textures/grass3.jpg", true)
        groundEmitt.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)

      bikeColBox.parent = figur
      bikeColBox.translate(Vector3f(0.5f,0.25f,-1.5f))

        //load textures
        val groundDiff = Texture2D("assets/textures/grass3.jpg", true)
        groundDiff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundSpecular = Texture2D("assets/textures/grass3.jpg", true)
        groundSpecular.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        val groundEmit = Texture2D("assets/textures/grass3.jpg", true)
        groundEmit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        groundMaterial = Material(groundDiff, groundEmit, groundSpecular, 60f, Vector2f(64.0f, 64.0f))

        //load an object and create a mesh
        val gres = loadOBJ("assets/models/ground.obj")
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


        /** Modelle **/
       // figur = loadModel("assets/models/Figur/figur.obj" ,Math.toRadians(0.0f), Math.toRadians(00.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
       // figur.scale(Vector3f(0.8f, 0.8f, 0.8f))

        baum = loadModel("assets/models/Baum/Baum.obj" ,Math.toRadians(-90.0f), Math.toRadians(90.0f), 0.0f)?: throw IllegalArgumentException("Could not load the model")
        baum.translate(Vector3f(7f, 0f, 0f))
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
        camera = TronCamera(
                custom(window.framebufferWidth, window.framebufferHeight),
                Math.toRadians(120.0f),
                0.1f,
                100.0f
        )
        camera.parent = figur
        camera.rotate(Math.toRadians(-35.0f), 0.0f, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))


        groundColor = Vector3f(1.0f, 1.0f, 1.0f)


        //bike point light
        bikePointLight = PointLight("pointLight[${pointLightList.size}]", Vector3f(0.0f, 2.0f, 0.0f), Vector3f(0.0f, 0.5f, 0.0f))
        bikePointLight.parent = figur
        pointLightList.add(bikePointLight)

        //bike spot light
        bikeSpotLight = SpotLight("spotLight[${spotLightList.size}]", Vector3f(3.0f, 3.0f, 3.0f), Vector3f(0.0f, 1.0f, -2.0f), Math.toRadians(20.0f), Math.toRadians(30.0f))
        bikeSpotLight.rotate(Math.toRadians(-10.0f), 0.0f, 0.0f)
        bikeSpotLight.parent = figur
        spotLightList.add(bikeSpotLight)

        // additional lights in the scene
        pointLightList.add(PointLight("pointLight[${pointLightList.size}]", Vector3f(0.0f, 2.0f, 2.0f), Vector3f(-10.0f, 2.0f, -10.0f)))
        pointLightList.add(PointLight("pointLight[${pointLightList.size}]", Vector3f(2.0f, 0.0f, 0.0f), Vector3f(10.0f, 2.0f, 10.0f)))
        spotLightList.add(SpotLight("spotLight[${spotLightList.size}]", Vector3f(10.0f, 300.0f, 300.0f), Vector3f(6.0f, 2.0f, 4.0f), Math.toRadians(20.0f), Math.toRadians(30.0f)))
        spotLightList.last().rotate(Math.toRadians(20f), Math.toRadians(60f), 0f)

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        //glDisable(GL_CULL_FACE); GLError.checkThrow()
        //glEnable(GL_CULL_FACE)
        //glFrontFace(GL_CCW); GLError.checkThrow()
        //glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()


        boxList.add(baum)


        /**Skybox**/
        // Order: right, left, top, bottom, front, back
        skyBoxTextures.add("assets/textures/skybox/right.jpg")
        skyBoxTextures.add("assets/textures/skybox/left.jpg")
        skyBoxTextures.add("assets/textures/skybox/top.jpg")
        skyBoxTextures.add("assets/textures/skybox/bottom.jpg")
        skyBoxTextures.add("assets/textures/skybox/front.jpg")
        skyBoxTextures.add("assets/textures/skybox/back.jpg")

        skybox.loadCubemap(skyBoxTextures)


    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()
        camera.bind(staticShader)


        val changingColor = Vector3f(1.0f, 1.0f, 1.0f)
        bikePointLight.lightColor = changingColor

        // bind lights
        for (pointLight in pointLightList) {
            pointLight.bind(staticShader)
        }
        staticShader.setUniform("numPointLights", pointLightList.size)
        for (spotLight in spotLightList) {
            spotLight.bind(staticShader, camera.calculateViewMatrix())
        }


        staticShader.setUniform("numSpotLights", spotLightList.size)

        // render objects
        staticShader.setUniform("shadingColor", groundColor)
        ground.render(staticShader)
        staticShader.setUniform("shadingColor",changingColor)

       figur?.render(staticShader)
        baum.render(staticShader)
        baum2.render(staticShader)
        blume.render(staticShader)
        laterne.render(staticShader)
        stein.render(staticShader)
        stein2.render(staticShader)
        brücke.render(staticShader)
        haus.render(staticShader)
        gras.render(staticShader)
        gras2.render(staticShader)

        staticShader.use()

        skybox.render(
            skyboxShader,
            camera.calculateViewMatrix(),
            camera.calculateProjectionMatrix()
        )


    }


    /** Collision Test 1**/
/*
    //AllgemeinesPrüfen auf Kollision
    fun collisionCheck(charakter:Renderable, object2:Renderable): Float{
        val xDistance=charakter.getPosition().x-object2.getPosition().x
        val yDistance=charakter.getPosition().y-object2.getPosition().y
        val zDistance=charakter.getPosition().z-object2.getPosition().z

        return Math.sqrt((xDistance*xDistance).toDouble()+
                (yDistance*yDistance).toDouble()+(zDistance*zDistance).toDouble()).toFloat()
    }

    //Kollisionsverhalten mit Wolke
    fun collisionDetectionCloud(drone:Renderable,cloud:Renderable){
        //Distanzen prüfen
        if (collisionCheck(drone,cloud)<=0.2f){
            //wenn getroffen, Drohne neue Position
            drone.translate(randomPosition())
        }
    }

    //Abfrage der einzelnen möglichen Kollisionen
    fun collisionDetection(){


            collisionDetectionCloud(figur,baum)


    }*/



    fun update(dt: Float, t: Float) {

        //checkCollision(figur, baum)
        checkCollisionObject(figur, baum)

        collisionObj(figur, baum, dt)
        collisionBounce(figur, baum)
        platform(figur, baum, dt)

        /* val moveMul = 5.0f
        val rotateMul = 0.5f * Math.PI.toFloat()
        if (window.getKeyState(GLFW_KEY_W)) {
            figur.translate(Vector3f(0.0f, 0.0f, -dt * moveMul))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            figur.translate(Vector3f(0.0f, 0.0f, dt * moveMul))
        }
        if (window.getKeyState(GLFW_KEY_A) ) {
            figur.rotate(0.0f, dt * rotateMul, 0.0f)
        }
        if (window.getKeyState(GLFW_KEY_D) ) {
            figur.rotate(0.0f, -dt * rotateMul, 0.0f)
        }*/

        if (window.getKeyState(org.lwjgl.glfw.GLFW.GLFW_KEY_W)) {
            figur.translate(Vector3f(0.0f, 0.0f, -100.0f * dt))
            //collisionDetection()
        }
        if (window.getKeyState(org.lwjgl.glfw.GLFW.GLFW_KEY_S)) {
            figur.translate(Vector3f(0.0f, 0.0f, 100.0f * dt))
            //collisionDetection()
        }
        if (window.getKeyState(org.lwjgl.glfw.GLFW.GLFW_KEY_A)){
            figur.translate(Vector3f(-50.0f * dt, 0.0f, 0.0f))
            //collisionDetection()
      }
        if (window.getKeyState(org.lwjgl.glfw.GLFW.GLFW_KEY_D)) {
            figur.translate(Vector3f(50.0f * dt, 0.0f, 0.0f))
            //collisionDetection()
        }


        //SPRINGEN
        if (window.getKeyState(GLFW_KEY_SPACE) && jump == 0) {
            figur?.translate(Vector3f(0.0f, 1.0f, 0f * dt))
            //jump = 1
        }

        if (window.getKeyState(GLFW_KEY_F)) {
            bikeSpotLight.rotate(Math.PI.toFloat() * dt, 0.0f, 0.0f)
        }

       // checkCollisionObject(figur, baum)


    }
/*
        //collision check //ansonten geht es dadurch
        if(collisionChecker.checkCollision(bikeColBox, boxList))
        {
            collision = true
            speedMultiplier = 0.0
        }

        //Power UP
        if(collisionChecker.checkCollisionBikePowerUp(bikeColBox,powerup))
        {
            speedMultiplier *= 0.5f
            figur?.translate(Vector3f(0f,0f,60f))
        }
        movementTimer++
    }*/

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {
        if (!firstMouseMove) {
            val yawAngle = (xpos - oldMouseX).toFloat() * 0.002f
            val pitchAngle = (ypos - oldMouseY).toFloat() * 0.0005f
            if (!window.getKeyState(GLFW_KEY_LEFT_ALT)) {
                figur?.rotate(0.0f, -yawAngle, 0.0f)
            }
            else{
                camera.rotateAroundPoint(0.0f, -yawAngle, 0.0f, Vector3f(0.0f, 0.0f, 0.0f))
            }
        } else firstMouseMove = false
        oldMouseX = xpos
        oldMouseY = ypos
    }

    fun cleanup() {}

    fun collosionDetection(gameObjectX: Renderable, gameObjectY: Renderable): Float {
        val collisionX : Float = gameObjectX.getPosition().x - gameObjectY.getPosition().x
        val collisionY : Float = gameObjectX.getPosition().y - gameObjectY.getPosition().y
        val collisionZ: Float = gameObjectX.getPosition().z - gameObjectY.getPosition().z

        return sqrt(collisionX * collisionX + collisionY * collisionY + collisionZ * collisionZ)
    }

    fun collisionObj(gameObjectX: Renderable, gameObjectY: Renderable, time: Float) {
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z < gameObjectY.getWorldPosition().z && upwardsSpeed == 0f) {
            upwardsSpeed = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z > gameObjectY.getWorldPosition().z && upwardsSpeed == 0f) {
            upwardsSpeed = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 2f && gameObjectX.getWorldPosition().y < gameObjectY.getWorldPosition().y && upwardsSpeed != 0f) {
            upwardsSpeed = objectG
            gameObjectX.translate(Vector3f(0f, -gameObjectX.getWorldPosition().y, 0f))
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 2f && gameObjectX.getWorldPosition().y > gameObjectY.getWorldPosition().y && upwardsSpeed != 0f) {
            upwardsSpeed = objectG
            gameObjectY.translate(Vector3f(0f, -gameObjectY.getWorldPosition().y, 0f))
        }
    }

    fun collisionBounce(gameObjectX: Renderable, gameObjectY: Renderable) {
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z < gameObjectY.getWorldPosition().z && enemyG == 0f) {
            enemyG = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z > gameObjectY.getWorldPosition().z && enemyG == 0f) {
            enemyG = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z == gameObjectY.getWorldPosition().z && enemyG == 0f) {
            enemyG = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3f && gameObjectX.getWorldPosition().y < gameObjectY.getWorldPosition().y && enemyG != 0f) {
            enemyG = objectG
            gameObjectX.translate(Vector3f(0f, (-gameObjectX.getWorldPosition().y), 0f))
            if(gameObjectX.getWorldPosition().z < gameObjectY.getWorldPosition().z)
                gameObjectX.translate(Vector3f(0f, 0f, (-gameObjectX.getWorldPosition().z)*0.002f))
            if(gameObjectX.getWorldPosition().z > gameObjectY.getWorldPosition().z)
                gameObjectX.translate(Vector3f(0f, 0f, (gameObjectX.getWorldPosition().z)*0.002f))
            if(gameObjectX.getWorldPosition().z == gameObjectY.getWorldPosition().z)
                gameObjectX.translate(Vector3f(0f, (gameObjectY.getWorldPosition().y - gameObjectX.getWorldPosition().y)*0.002f, 0f))
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3f && gameObjectX.getWorldPosition().y > gameObjectY.getWorldPosition().y && enemyG != 0f) {
            enemyG = objectG
            gameObjectY.translate(Vector3f(0f, (-gameObjectY.getWorldPosition().y), 0f))
            if(gameObjectY.getWorldPosition().z < gameObjectX.getWorldPosition().z)
                gameObjectY.translate(Vector3f(0f, 0f, (-gameObjectY.getWorldPosition().z)*0.002f))
            if(gameObjectY.getWorldPosition().z > gameObjectX.getWorldPosition().z)
                gameObjectY.translate(Vector3f(0f, 0f, (gameObjectY.getWorldPosition().z)*0.002f))
            if(gameObjectX.getWorldPosition().z == gameObjectY.getWorldPosition().z)
                gameObjectX.translate(Vector3f(0f, (gameObjectX.getWorldPosition().y - gameObjectY.getWorldPosition().y)*0.002f, 0f))
        }
    }

    fun platform(gameObjectX: Renderable, gameObjectY: Renderable, time: Float) {
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z < gameObjectY.getWorldPosition().z && upwardsSpeed == 0f) {
            upwardsSpeed = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.0f && gameObjectX.getWorldPosition().z > gameObjectY.getWorldPosition().z && upwardsSpeed == 0f) {
            upwardsSpeed = objectG
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 2f && gameObjectX.getWorldPosition().y < gameObjectY.getWorldPosition().y && upwardsSpeed != 0f) {
            upwardsSpeed = objectG
            gameObjectX.translate(Vector3f(0f, gameObjectY.getWorldPosition().y*0.005f+gameObjectX.getWorldPosition().y*0.02f, 0f))
        }
        if(collosionDetection(gameObjectX, gameObjectY) <= 0.9f && gameObjectX.getWorldPosition().y > gameObjectY.getWorldPosition().y && upwardsSpeed != 0f) {
            upwardsSpeed = objectG
            gameObjectX.translate(Vector3f(0f, gameObjectY.getWorldPosition().y*0.005f+gameObjectX.getWorldPosition().y*0.02f, 0f))
        }
    }

    fun checkCollisionObject(gameObjectX: Renderable, gameObjectY: Renderable): Boolean {
        var boo = false
        //Distanz von Spieler zu anderen Objekten wird berechnet
        //Distanz von Player zu Obekten, um sich zum Beispiel zu verstecken
        if(collosionDetection(gameObjectX, gameObjectY) <= 3.5f) {
            boo = true
            println("berührung")
        }
        return boo
    }

    /*
    //Berechnung von Kollision
    fun checkCollision(gameObjectX: Renderable, gameObjectY: Renderable): Boolean {
        var boo = false
        //Distanz von Spieler zu anderen Objekten wird berechnet
        //Distanz von Player zu Obekten, um sich zum Beispiel zu verstecken
        if(collosionDetection(gameObjectX, gameObjectY) <= 3f) {
            distanceEnemy = 100.0f
            boo = true
            println("berührung")
        }
        return boo

    }*/

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
     //   camera.fov += Math.toRadians(yoffset.toFloat())
   }
}



