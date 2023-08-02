package cga.exercise.components.skybox

import cga.exercise.components.geometry.Material

import cga.exercise.components.shader.ShaderProgram


    class Skybox(
        positiveX: FileHandle?, negativeX: FileHandle?, positiveY: FileHandle?, negativeY: FileHandle?,
        positiveZ: FileHandle?, negativeZ: FileHandle?
    ) : Disposable {
        protected var shader: ShaderProgram? = null
        private val boxModel: Model
        private val boxInstance: ModelInstance
        private var cubemap: Cubemap? = null
        private var positiveX: FileHandle? = null
        private var negativeX: FileHandle? = null
        private var positiveY: FileHandle? = null
        private var negativeY: FileHandle? = null
        private var positiveZ: FileHandle? = null
        private var negativeZ: FileHandle? = null

        init {
            set(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ)
            boxModel = createModel()
            boxInstance = ModelInstance(boxModel)
        }

        private fun createModel(): Model {
            val modelBuilder = ModelBuilder()
            return modelBuilder.createBox(
                1, 1, 1,
                Material(CubemapAttribute(CubemapAttribute.EnvironmentMap, cubemap)),
                VertexAttributes.Usage.Position
            )
        }

        val skyboxInstance: ModelInstance
            get() = boxInstance

        operator fun set(
            positiveX: FileHandle?, negativeX: FileHandle?, positiveY: FileHandle?, negativeY: FileHandle?,
            positiveZ: FileHandle?, negativeZ: FileHandle?
        ) {
            if (cubemap != null) {
                cubemap.dispose()
            }
            cubemap = Cubemap(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ)
            this.positiveX = positiveX
            this.negativeX = negativeX
            this.positiveY = positiveY
            this.negativeY = negativeY
            this.positiveZ = positiveZ
            this.negativeZ = negativeZ
        }

        fun getPositiveX(): FileHandle? {
            return positiveX
        }

        fun getNegativeX(): FileHandle? {
            return negativeX
        }

        fun getPositiveY(): FileHandle? {
            return positiveY
        }

        fun getNegativeY(): FileHandle? {
            return negativeY
        }

        fun getPositiveZ(): FileHandle? {
            return positiveZ
        }

        fun getNegativeZ(): FileHandle? {
            return negativeZ
        }

        fun dispose() {
            boxModel.dispose()
            cubemap.dispose()
        }
    }
