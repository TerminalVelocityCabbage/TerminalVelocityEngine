struct SpotLight {
    PointLight pointLight;
    vec3 coneDirection;
    float cutoff;
};

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {

    //Some initial setup
    vec3 lightDirection = light.pointLight.position - position;
    vec3 unitFromLightDirection = -normalize(lightDirection);
    //Get the angle of the vertex to the light source direction to determine later if it's within the cone angle
    float spotAlpha = dot(unitFromLightDirection, normalize(light.coneDirection));

    vec4 color = vec4(0, 0, 0, 0);
    if (spotAlpha > light.cutoff) {
        color = calcPointLight(light.pointLight, position, normal);
        //Determine the percentage of light getting to a fragment based on the distance from the cone direction vector
        color *= (1.0 - (1.0 - spotAlpha) / (1.0 - light.cutoff));
    }

    return color;
}
