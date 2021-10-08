#version 330

layout(pixel_center_integer) in vec4 gl_FragCoord;
in vec2 vertUV;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform vec4 borderColor;
uniform float borderThickness;
uniform float borderRadius;
uniform vec4 positions;

float aspect = screenRes.x / screenRes.y;
vec2 ratio = vec2(aspect, 1.0);

void main() {

    //Convert positions to readable values
    vec2 start = positions.xz;
    vec2 dimensions = positions.yw;

    vec2 pixelUV = abs(((gl_FragCoord.xy / screenRes) * 2) - 1);
    vec2 modPixelUV = pixelUV * ratio * dimensions + start;

    //Non Corner Pixels
    fragColor = (pixelUV.x > aspect - borderThickness || modPixelUV.y > 1 - borderThickness) ? borderColor : color;

    //Test for if the modified UVs are working tl;dr they're not
    //fragColor = (modPixelUV.y > 0.97) ? vec4(0, 0, 1, 1) : vec4(modPixelUV.y, 0, 0, 0.5);

    //Corner Pixels
    if (modPixelUV.y > 1 - borderRadius && modPixelUV.x > aspect - borderRadius) {
        //Distances
        float dist = distance(vec2(1 - borderRadius), vec2(pixelUV.x * aspect + (1 - aspect), pixelUV.y));
        float wd = dist * 10.0/screenRes.y;

        //Border
        float aliasAlpha = smoothstep(borderRadius + wd, borderRadius - wd, dist);
        vec4 resBorderColor = vec4(borderColor.xyz, borderColor.a * aliasAlpha);
        //Inner
        float innerAliasAlpha = smoothstep(borderRadius - borderThickness + wd, borderRadius - borderThickness - wd, dist);
        vec4 resInnerColor = vec4(color.xyz, color.a * innerAliasAlpha);

        //Set Color
        fragColor = dist > borderRadius - borderThickness ? resBorderColor : resInnerColor;
    }
}