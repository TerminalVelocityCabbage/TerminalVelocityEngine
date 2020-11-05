struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectivity;
};

uniform sampler2D textureSampler;
uniform Material material;

vec4 materialAmbientColor;
vec4 materialDiffuseColor;
vec4 materialSpecularColor;

void setupColors(Material material, vec2 textCoord) {
    if (material.hasTexture == 1) {
        materialAmbientColor = texture(textureSampler, textCoord);
        materialDiffuseColor = materialAmbientColor;
        materialSpecularColor = materialAmbientColor;
    } else {
        materialAmbientColor = material.ambient;
        materialDiffuseColor = material.diffuse;
        materialSpecularColor = material.specular;
    }
}
