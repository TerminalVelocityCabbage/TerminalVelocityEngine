uniform vec3 ambientLight;
uniform float specularPower;

struct Attenuation {
    float constant;
    float linear;
    float exponential;
};

vec4 calcLightColor(vec4 lightColor, float lightIntensity, vec3 position, vec3 toLightDirection, vec3 normal) {

    //Setup
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightDirection = -toLightDirection;
    vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));

    //Diffuse
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
    diffuseColor = materialDiffuseColor * lightColor * lightIntensity * diffuseFactor;

    //Specular
    vec4 specularColor = vec4(0, 0, 0, 0);
    float specularFactor = pow(max(dot(cameraDirection, reflectedLight), 0.0), specularPower);
    specularColor = materialSpecularColor * lightIntensity * specularFactor * materialReflectivity * lightColor;

    return (diffuseColor + specularColor);
}
