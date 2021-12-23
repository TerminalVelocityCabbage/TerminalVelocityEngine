#version 330

uniform vec2 screenRes;


layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;
layout (location = 1) in vec3 colour;
layout (location = 1) in vec3 borderThickness;

out vec2 vertUV;

void main() {
    gl_Position = vec4(position * vec3(screenRes.xy, 1.0), 1.0);
    vertUV = uv;
}