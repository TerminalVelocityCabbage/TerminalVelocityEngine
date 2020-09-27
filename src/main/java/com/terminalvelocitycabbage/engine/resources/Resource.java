package com.terminalvelocitycabbage.engine.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public interface Resource {

	Identifier getIdentifier();

	InputStream openStream() throws IOException;

	Optional<String> asString();

	Optional<ByteBuffer> getByteBufferFromPNG();
}
