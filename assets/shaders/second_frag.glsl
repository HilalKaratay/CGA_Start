#version 330 core

in struct VertexData
{
        vec3 normal;
} vertexData;


out vec4 color;


void main(){
    vec3 normal = normalize(vertexData.normal);
    color = vec4(abs(normal.r), abs(normal.g), abs(normal.b), 1.0f);

}