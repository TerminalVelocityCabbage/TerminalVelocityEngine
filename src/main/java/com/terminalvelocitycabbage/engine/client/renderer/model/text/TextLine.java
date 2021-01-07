package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import java.util.List;

public class TextLine {

	public List<TextCharacter> characters;

	public TextLine(List<TextCharacter> characters) {
		this.characters = characters;
	}

	//TODO make this return a boolean if the line goes over a width
	public void addCharacter(TextCharacter character) {
		characters.add(character);
	}

	public void removeCharacter() {
		characters.remove(characters.size() - 1);
	}
}
