#version 330

in vec4 gl_FragCoord;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform mat3 cornerStuff;
uniform vec4 borderColor;
uniform int borderThickness;

void main() {

    //Make radius variable from uniform
    float radius = cornerStuff[2][2];
    float totalRadius = radius + borderThickness;

    //Create pixel coordinates from vertex positions
    vec2 tlP = ((vec2(cornerStuff[0][0], cornerStuff[0][1]) + 1) / 2) * screenRes;
    vec2 blP = ((vec2(cornerStuff[0][2], cornerStuff[1][0]) + 1) / 2) * screenRes;
    vec2 brP = ((vec2(cornerStuff[1][1], cornerStuff[1][2]) + 1) / 2) * screenRes;
    vec2 trP = ((vec2(cornerStuff[2][0], cornerStuff[2][1]) + 1) / 2) * screenRes;

    //Create reference coordinates for the center of the inner border radius circle
    vec2 tlR = vec2(tlP.x + totalRadius, tlP.y - totalRadius);
    vec2 blR = vec2(blP.x + totalRadius, blP.y + totalRadius);
    vec2 brR = vec2(brP.x - totalRadius, brP.y + totalRadius);
    vec2 trR = vec2(trP.x - totalRadius, trP.y - totalRadius);

    //Rounded corners
    //Top Left
    if (distance(tlR, gl_FragCoord.xy) < totalRadius) {
        fragColor = borderColor;
        if (distance(tlR, gl_FragCoord.xy) < radius) {
            fragColor = color;
        }
    }
    //Bottom Left
    if (distance(blR, gl_FragCoord.xy) < totalRadius) {
        fragColor = borderColor;
        if (distance(blR, gl_FragCoord.xy) < radius) {
            fragColor = color;
        }
    }
    //Bottom Right
    if (distance(brR, gl_FragCoord.xy) < totalRadius) {
        fragColor = borderColor;
        if (distance(brR, gl_FragCoord.xy) < radius) {
            fragColor = color;
        }
    }
    //Top Right
    if (distance(trR, gl_FragCoord.xy) < totalRadius) {
        fragColor = borderColor;
        if (distance(trR, gl_FragCoord.xy) < radius) {
            fragColor = color;
        }
    }

    //Fill in the border stuff
    if (gl_FragCoord.x > tlR.x && gl_FragCoord.x < trR.x) {
        if (gl_FragCoord.y > tlP.y - borderThickness || gl_FragCoord.y < blP.y + borderThickness) {
            fragColor = borderColor;
        } else {
            fragColor = color;
        }
    }
    if (gl_FragCoord.y > blR.y && gl_FragCoord.y < tlR.y) {
        if (gl_FragCoord.x < tlP.x + borderThickness || gl_FragCoord.x > brP.x - borderThickness) {
            fragColor = borderColor;
        } else {
            fragColor = color;
        }
    }

}