#version 330
layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 inTextureCoord;
layout (location = 2) in vec4 vertexColor;

out vec2 vertTextureCoord;
out vec4 vertVertexColor;

void main() {
    gl_Position = vec4(inPosition, 1.0);
    vertTextureCoord = inTextureCoord;
    vertVertexColor = vertexColor;
}