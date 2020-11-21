package com.terminalvelocitycabbage.engine.client.renderer.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.debug.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FontTexture {

	private static final String IMAGE_FORMAT = "png";

	private static final int CHAR_PADDING = 2;

	private final Font font;

	private final String charSetName;

	private final Map<Character, CharInfo> charMap;

	private Texture texture;

	private int height;

	private int width;

	public FontTexture(Font font, String charSetName) throws Exception {
		this.font = font;
		this.charSetName = charSetName;
		charMap = new HashMap<>();

		buildTexture();
	}

	public int getWidth() {
		return width;
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

	private void buildTexture() throws Exception {
		// Get the font metrics for each character for the selected font by using image
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2D = img.createGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setFont(font);
		FontMetrics fontMetrics = g2D.getFontMetrics();

		String allChars = getAllAvailableChars(charSetName);
		this.width = 0;
		this.height = fontMetrics.getHeight();
		for (char c : allChars.toCharArray()) {
			// Get the size for each character and update global image size
			CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
			charMap.put(c, charInfo);
			width += charInfo.getWidth() + CHAR_PADDING;
		}
		g2D.dispose();

		// Create the image associated to the charset
		img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
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

		ByteBuffer buf = null;
		try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			ImageIO.write(img, IMAGE_FORMAT, out);
			buf = ByteBuffer.wrap(out.toByteArray());
			buf.flip();
		} catch (IOException e) {
			Log.error("Could not convert image to ByteBuffer " + e.getMessage());
		}
		if (buf == null) {
			Log.error("Null image byte buffer");
		}
		//ImageIO.write(img, IMAGE_FORMAT, new File("Temp.png"));
		texture = new Texture(buf);
	}

	public static class CharInfo {

		private final int startX;

		private final int width;

		public CharInfo(int startX, int width) {
			this.startX = startX;
			this.width = width;
		}

		public int getStartX() {
			return startX;
		}

		public int getWidth() {
			return width;
		}
	}
}
