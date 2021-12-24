#version 330

uniform vec2 screenRes;


layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_uv;
layout (location = 2) in vec4 a_colour;
layout (location = 3) in vec2 a_borderRadius;
layout (location = 4) in vec2 a_borderThickness;

smooth out vec2 uv;
smooth out vec4 colour;
smooth out vec2 borderRadius;
smooth out vec2 borderThickness;

void main() {
    gl_Position = vec4(a_position, 1.0);
    uv = a_uv;
    colour = a_colour;
    borderRadius = a_borderRadius;
}