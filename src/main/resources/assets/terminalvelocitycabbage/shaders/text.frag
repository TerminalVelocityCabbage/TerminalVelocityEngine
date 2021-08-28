#version 330 core
in vec2 vertTextureCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec4 textColor;

void main() {
    fragColor = texture(textureSampler, vertTextureCoord)*textColor;
}