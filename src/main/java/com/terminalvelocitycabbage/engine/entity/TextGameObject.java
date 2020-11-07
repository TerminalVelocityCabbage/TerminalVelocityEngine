package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;

import java.util.ArrayList;

public class TextGameObject extends EmptyGameObject {

	public static final float Z_POS = 0.0f;
	private final FontTexture fontTexture;
	private String text;
	private Model model;

	public TextGameObject(String text, FontTexture texture) throws Exception {
		super();
		this.text = text;
		this.fontTexture = texture;
		this.model = createTextModel();
	}

	/**
	 * A Text Model has children of all the "words" of a text, which can be defined as
	 * any character sequence between two whitespace characters.
	 *
	 * A word is the first letter of the word with children as the successive letters of the word if any.
	 *
	 * @return A Model with the above format
	 */
	private Model createTextModel() {

		//Create an empty list to store the words
		//These will be the children of the Text model in the future
		ArrayList<Model.Part> words = new ArrayList<>();

		//Create a word to start the Text
		Model.Part word = null;
		//Store the width of the previous character so we can offset the position of the next (so they're next to one another)
		float previousWidth = 0;
		//A boolean representing weather a new word should be started.
		boolean startNewWord = true;
		//Start the loop with a single offset character since we initialize the list with the first
		for (char character : text.toCharArray()) {

			if (startNewWord) {
				word = new Model.Part(buildCharacterMesh(character));
				//TODO set the position of the words
			} else {
				//Create a mesh from the character and create a letter out of it
				Model.Part letter = new Model.Part(buildCharacterMesh(character));
				letter.setPosition(previousWidth, 0, 0);
				word.addChild(letter);
			}
			startNewWord = false;

			//If the character is a whitespace character that marks a new word to be created
			if (Character.isWhitespace(character)) {
				//Finish the current word and add it to the text
				words.add(word);
				//Mark the loop as needing a new word
				startNewWord = true;
				//No need to add a whitespace character to the beginning of the next
				continue;
			}

			//Save the width of this character so that the next one can be offset by that amount
			FontTexture.CharInfo charInfo = fontTexture.getCharInfo(character);
			previousWidth = charInfo.getWidth();
		}

		//Create a TextModel from the Words as children
		Model model = new Model(words);
		//Set the model's texture to the font's
		model.setMaterial(fontTexture.getTexture().toMaterial());
		return model;
	}

	private Mesh buildCharacterMesh(char character) {

		FontTexture.CharInfo charInfo = fontTexture.getCharInfo(character);

		//Top Left vertex
		Vertex topLeft = new Vertex()
				.setXYZ(0, 0, Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 0);
		//Bottom Left vertex
		Vertex bottomLeft = new Vertex()
				.setXYZ(0, (float) fontTexture.getHeight(), Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 1);
		//Bottom Right vertex
		Vertex bottomRight = new Vertex()
				.setXYZ(charInfo.getWidth(), (float) fontTexture.getHeight(), Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 1);
		//Top Right vertex
		Vertex topRight = new Vertex()
				.setXYZ(charInfo.getWidth(), 0, Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 0);

		return new TexturedRectangle(topLeft, bottomLeft, bottomRight, topRight);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.model.destroy();
		this.model = createTextModel();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	@Override
	public void update() {
		super.update();
		model.update(position, rotation, scale);
	}

	public void destroy() {
		model.destroy();
	}

	public Model getModel() {
		return model;
	}
}
