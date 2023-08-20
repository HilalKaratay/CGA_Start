package cga.framework

import RawMesh

data class RawModel(
        var meshes: MutableList<RawMesh> = mutableListOf(),
        var materials: MutableList<RawMaterial> = mutableListOf(),
        var textures: MutableList<String> = mutableListOf()
)