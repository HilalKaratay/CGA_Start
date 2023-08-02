#version 330 core

in struct VertexData
{
    vec3 normal;

} vertexData;

in vec2 tc0;

uniform sampler2D emit;

out vec4 color;

void main(){
    vec3 normal = normalize(vertexData.normal);
    color = texture(emit, tc0);

}
