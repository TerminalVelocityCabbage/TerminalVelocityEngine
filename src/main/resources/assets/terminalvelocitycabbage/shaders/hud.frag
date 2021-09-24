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
        modSize = size - vec2(thickness / 10.0);
    }
    float d = length(max(abs(uv - pos),modSize) - modSize) - radius;
    return smoothstep(0.66, 0.33, d / thickness * 5.0);
}

float roundedFrame (vec2 pos, vec2 size, float radius, float thickness) {
    float d = length(max(abs(uv - pos),size) - size) - radius;
    return smoothstep(0.66, 0.33, abs(d / thickness) * 5.0);
}

void main() {

    float intensity;
    vec3 outCol;

    //var values
    vec2 pos = vec2(0.5, 0.6); //TODO make this a uniform (average of vertex positions)
    vec2 npos = gl_FragCoord.xy / screenRes.xy;     // 0.0 .. 1.0
    vec2 size = vec2(1, 0.1);  //TODO make this a uniform (dims/res/2)
    // get uv position with origin at window center
    float aspect = screenRes.x / screenRes.y;       // aspect ratio x/y
    vec2 ratio = vec2(aspect, 1.0);                 // aspect ratio (x/y,1)
    uv = (2.0 * npos - 1.0) * ratio;                // -1.0 .. 1.0

    bool hasBorder = borderThickness > 0;

    //background
    vec3 rectColor = color.rgb;
    intensity = roundedRectangle(pos, size, 0.1, 0.1, hasBorder) * color.a;
    outCol = mix(outCol, rectColor, intensity);

    //frame
    if (hasBorder) {
        vec3 frameColor = borderColor.rgb;
        intensity = roundedFrame(pos, size, 0.1, 0.1) * borderColor.a;
        outCol = mix(outCol, frameColor, intensity);
    }

    fragColor = vec4(outCol, 1.0);
}