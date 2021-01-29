package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FontTexture {

	private static final String IMAGE_FORMAT = "png";
	private static final String DEFAULT_CHARSET = "ISO-8859-1";
	private static final int CHAR_PADDING = 2;
	private static final float DEFAULT_SIZE = 32f;

	private Font font;
	private String charSetName;
	private Map<Character, CharInfo> charMap;

	private Texture texture;
	private int height;
	private int position;

	public FontTexture(Font font, String charSetName) {
		this.font = font;
		this.charSetName = charSetName;
		charMap = new HashMap<>();

		buildTexture();
	}

	public FontTexture(ResourceManager resourceManager, Identifier identifier) {
		this(resourceManager, identifier, DEFAULT_CHARSET, DEFAULT_SIZE);
	}

	public FontTexture(ResourceManager resourceManager, Identifier identifier, float size) {
		this(resourceManager, identifier, DEFAULT_CHARSET, size);
	}

	public FontTexture(ResourceManager resourceManager, Identifier identifier, String charSet, float size) {
		Optional<Resource> resource = resourceManager.getResource(identifier);
		if (resource.isPresent()) {
			try {
				this.font = Font.createFont(Font.TRUETYPE_FONT, resource.get().openStream()).deriveFont(size);
				this.charSetName = charSet;
				charMap = new HashMap<>();
				buildTexture();
			} catch (FontFormatException | IOException e) {
				Log.error("Could not load font " + identifier.getPath());
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Could not load font resource " + identifier.toString());
		}
	}

	public int getPosition() {
		return position;
	}

	public int getHeight() {
		return height;
	}

	public Texture getTexture() {
		return texture;
	}

	public CharInfo getCharInfo(char c) {
		return charMap.get(c);
	}

	public Map<Character, CharInfo> getCharMap() {
		return charMap;
	}

	private String getAllAvailableChars(String charsetName) {
		CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
		StringBuilder result = new StringBuilder();
		for (char c = 0; c < Character.MAX_VALUE; c++) {
			if (ce.canEncode(c)) {
				result.append(c);
			}
		}
		return result.toString();
	}

	private void buildTexture() {
		// Get the font metrics for each character for the selected font by using image
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2D = img.createGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setFont(font);
		FontMetrics fontMetrics = g2D.getFontMetrics();

		String allChars = getAllAvailableChars(charSetName);
		this.position = 0;
		this.height = fontMetrics.getHeight();
		for (char c : allChars.toCharArray()) {
			// Get the size for each character and update global image size
			CharInfo charInfo = new CharInfo(position, fontMetrics.charWidth(c));
			charMap.put(c, charInfo);
			position += charInfo.getWidth() + CHAR_PADDING;
		}
		g2D.dispose();

		// Create the image associated to the charset
		img = new BufferedImage(position, height, BufferedImage.TYPE_4BYTE_ABGR);
		g2D = img.createGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setFont(font);
		fontMetrics = g2D.getFontMetrics();
		g2D.setColor(Color.WHITE);
		int startX = 0;
		for (char c : allChars.toCharArray()) {
			CharInfo charInfo = charMap.get(c);
			g2D.drawString("" + c, startX, fontMetrics.getAscent());
			startX += charInfo.getWidth() + CHAR_PADDING;
		}
		g2D.dispose();

		InputStream inputStream = null;
		try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(img, IMAGE_FORMAT, out);
			out.flush();
			inputStream = new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			Log.error("Could not convert image to ByteBuffer " + e.getMessage());
		}
		//ImageIO.write(img, IMAGE_FORMAT, new File("Temp.png"));
		texture = new Texture(inputStream);
	}
}
