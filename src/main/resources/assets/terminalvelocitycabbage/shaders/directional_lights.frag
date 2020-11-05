struct DirectionalLight {
    vec3 direction;
    vec4 color;
    float intensity;
};

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}
