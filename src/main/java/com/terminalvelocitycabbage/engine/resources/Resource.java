package com.terminalvelocitycabbage.engine.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public interface Resource {

	Identifier getIdentifier();

	InputStream openStream() throws IOException;

	Optional<BufferedImage> getImage();

	Optional<String> asString();

	Optional<ByteBuffer> getBytes();
}
