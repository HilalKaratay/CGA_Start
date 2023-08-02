#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

out struct VertexData
{
    vec3 normal;
} vertexData;


void main(){
    vec4 pos = view_matrix * model_matrix * vec4(position, 1.0f);

    vec4 norm = transpose(inverse(view_matrix * model_matrix)) * vec4(normal, 1.0f);

    gl_Position = projection_matrix * pos;
    vertexData.normal = norm.xyz;
}