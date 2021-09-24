#version 330

in vec4 gl_FragCoord;
in vec2 vertUV;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform vec4 borderColor;
uniform int borderThickness;
uniform int borderRadius;

vec2 uv = vec2(0);

float roundedRectangle (vec2 pos, vec2 size, float radius, float thickness, bool hasBorder) {
    vec2 modSize = size;
    if (hasBorder) { //Make the rectangle slightly smaller than the border
        modSize = size - vec2(thickness / 10);
    }
    float d = length(max(abs(uv - pos),modSize) - modSize) - radius;
    return smoothstep(0.6, 0.5, d / thickness * 5.0);
}

float roundedFrame (vec2 pos, vec2 size, float radius, float thickness) {
    float d = length(max(abs(uv - pos),size) - size) - radius;
    return smoothstep(0.6, 0.5, abs(d / thickness) * 5.0);
}

void main() {

    float intensity;
    vec4 outCol;

    //var values
    vec2 pos = vec2(0, 0);
    vec2 npos = gl_FragCoord.xy / screenRes.xy;   // 0.0 .. 1.0
    float aspect = screenRes.x / screenRes.y;   // aspect ratio x/y
    vec2 size = vec2(1, 0.5);
    // get uv position with origin at window center
    vec2 ratio = vec2(aspect, 1.0);                 // aspect ratio (x/y,1)
    uv = (2.0 * npos - 1.0) * ratio;             // -1.0 .. 1.0

    //background
    intensity = roundedRectangle(pos, size, borderRadius/200.0, borderThickness/80.0, true);
    outCol = mix(outCol, color, intensity);

    fragColor = outCol;

    //frame
    intensity = roundedFrame(pos, size, borderRadius/200.0, borderThickness/80.0);
    outCol = mix(outCol, borderColor, intensity);

    fragColor = mix(fragColor, outCol, intensity);
}