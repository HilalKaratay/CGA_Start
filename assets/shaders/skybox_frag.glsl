#version 330 core

out vec4 FragColor;

in vec3 TexCoords;

uniform float playTime;

uniform samplerCube cubeMapDay;
uniform samplerCube cubeMapNight;
uniform float blendFactor;
uniform vec3 fogColour;

const float lowerLimit = 0.0;
const float upperLimit =10.0;


void main()
{

    vec4 dayTexture = texture(cubeMapDay, TexCoords);
    vec4 nightTexture = texture(cubeMapNight,TexCoords);
    vec4 finalColour = mix(dayTexture,nightTexture,blendFactor);


    float factor =(TexCoords.y-lowerLimit)/(upperLimit-lowerLimit);
    factor = clamp(factor,0.0,1.0);
    FragColor = mix(vec4(fogColour,1.0),finalColour, factor);

}