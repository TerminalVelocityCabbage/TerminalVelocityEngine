#version 330

smooth in vec2 uv;
smooth in vec4 colour;
smooth in vec2 borderRadius;
smooth in vec2 borderThickness;

out vec4 fragColor;

void main() {
    vec2 absoluteUv = abs(uv);
    fragColor = (absoluteUv.x > 1-borderThickness.x || absoluteUv.y > 1-borderThickness.y) ? vec4(0,1.0,0,1) : colour;

    if(absoluteUv.x > 1-borderRadius.x && absoluteUv.y > 1-borderRadius.y) {
        fragColor = vec4(1,0,1,1);
    }

}