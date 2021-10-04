#version 330

layout(pixel_center_integer) in vec4 gl_FragCoord;
in vec2 vertUV;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform vec4 borderColor;
uniform int borderThickness;
uniform int borderRadius;

float aspect = screenRes.x / screenRes.y;
vec2 ratio = vec2(aspect, 1.0);

void main() {

    float r = 0.3;
    vec2 uv = vertUV;
    vec2 pixelUV = abs(((gl_FragCoord.xy / screenRes) * 2) - 1);
    vec2 modPixelUV = pixelUV * ratio;

    //Good Pixels
    fragColor = vec4(0, 1, 0, 0.2);

    //Filter Corners
    if (modPixelUV.y > 1 - r) {
        if (modPixelUV.x > aspect - r) {
            if (distance(vec2(1 - r), pixelUV) > r) {
                fragColor = vec4(1, 0, 0, 1);
            }
        }
    }
}