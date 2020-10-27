package com.terminalvelocitycabbage.engine.client.resources;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
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
	public Optional<DataInputStream> asDataStream() {
		if (invalid) {
			throw new RuntimeException("Invalid Resource for url: " + url.toString());
		}
		try {
			return Optional.of(new DataInputStream(url.openStream()));
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
