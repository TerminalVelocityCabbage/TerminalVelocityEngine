package com.terminalvelocitycabbage.engine.resources;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UrlResource implements Resource {

	private final Identifier identifier;
	private final URL url;
	private boolean invalid;

	protected UrlResource(Identifier identifier, URL url) {
		this.identifier = identifier;
		this.url = url;
	}

	@Override
	public Identifier getIdentifier() {
		return identifier;
	}

	@Override
	public InputStream openStream() throws IOException {
		if (invalid) {
			throw new RuntimeException("Invalid Resource for url: " + url.toString());
		}
		return url.openStream();
	}

	@Override
	public Optional<ByteBuffer> getBytes() {
		try {
			InputStream stream = openStream();
			byte[] bytes = IOUtils.buffer(stream).readAllBytes();
			stream.close();
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes).flip();
			return Optional.of(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public Optional<String> asString() {
		Optional<String> out = Optional.empty();
		try {
			out = Optional.of(IOUtils.toString(openStream(), StandardCharsets.UTF_8.name()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public boolean isInvalid() {
		return invalid;
	}
}
