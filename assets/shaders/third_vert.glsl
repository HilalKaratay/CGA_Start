#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec2 tcMultiplier;

out struct VertexData
{
    vec3 normal;
} vertexData;

out vec2 tc0;

void main() {
    vec4 pos = view_matrix * model_matrix * vec4(position, 1.0f);

    vec4 norm = transpose(inverse(view_matrix * model_matrix)) * vec4(normal, 0.0f);

    tc0 = tcMultiplier * texCoords;
    gl_Position = projection_matrix * pos;
    vertexData.normal = norm.rgb;

}