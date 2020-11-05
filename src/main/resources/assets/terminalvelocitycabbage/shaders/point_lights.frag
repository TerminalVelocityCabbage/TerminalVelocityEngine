struct Attenuation {
    float constant;
    float linear;
    float exponential;
};

struct PointLight {
    vec4 color;
    vec3 position;
    float intensity;
    Attenuation attenuation;
};

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {

    //Some inital setup
    vec3 lightDirection = light.position - position;
    vec3 unitToLightDirection  = normalize(lightDirection);
    vec4 lightColor = calcLightColor(light.color, light.intensity, position, unitToLightDirection, normal);
    float distance = length(lightDirection);

    //Attenuation (fade of light by distance)
    float attenuationFade = light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponential * (distance * distance));

    return lightColor / attenuationFade;
}
