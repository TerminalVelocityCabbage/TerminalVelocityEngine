package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FontInfo {

    int textureHeight;
    Map<Character, CharInfo> characterInfo;

    private FontInfo() {
        this.characterInfo = new HashMap<>();
    }

    public static FontInfo read(ResourceManager resourceManager, Identifier identifier) {
        FontInfo info = new FontInfo();
        Optional<Resource> resource = resourceManager.getResource(identifier);
        if (resource.isPresent()) {
            DataInputStream dataIn = resource.get().asDataStream();
            try {
                info.setHeight(dataIn.readInt());
                while (dataIn.available() > 0) {
                    info.addChar(dataIn.readChar(), new CharInfo(dataIn.readInt(), dataIn.readInt()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.crash("Font Read Error", new RuntimeException("Could not load font resource " + identifier.toString()));
        }
        return info;
    }

    private void setHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    private void addChar(Character character, CharInfo charInfo) {
        this.characterInfo.put(character, charInfo);
    }

    public CharInfo getCharInfo(char character) {
        if (characterInfo.containsKey(character)) {
            return characterInfo.get(character);
        } else {
            Log.crash("Font Read Error", new RuntimeException("Encountered invalid character in font " + this.toString()));
            return null;
        }
    }

    public Map<Character, CharInfo> getCharMap() {
        return characterInfo;
    }

    public int getTextureHeight() {
        return textureHeight;
    }
}
