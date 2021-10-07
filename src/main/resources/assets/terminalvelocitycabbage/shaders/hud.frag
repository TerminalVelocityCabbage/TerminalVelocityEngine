#version 330

layout(pixel_center_integer) in vec4 gl_FragCoord;
in vec2 vertUV;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform vec4 borderColor;
uniform int borderThickness;
uniform float borderRadius;

float aspect = screenRes.x / screenRes.y;
vec2 ratio = vec2(aspect, 1.0);

void main() {

    vec2 uv = vertUV;
    vec2 pixelUV = abs(((gl_FragCoord.xy / screenRes) * 2) - 1);
    vec2 modPixelUV = pixelUV * ratio;

    //Good Pixels
    fragColor = color;

    //Filter Corners
    if (modPixelUV.y > 1 - borderRadius) {
        if (modPixelUV.x > aspect - borderRadius) {
            float dist = distance(vec2(1 - borderRadius), vec2(pixelUV.x * aspect + (1 - aspect), pixelUV.y));
            float wd = dist * 10.0/screenRes.y;
            fragColor = vec4(color.xyz, smoothstep(borderRadius + wd, borderRadius - wd, dist));
        }
    }
}