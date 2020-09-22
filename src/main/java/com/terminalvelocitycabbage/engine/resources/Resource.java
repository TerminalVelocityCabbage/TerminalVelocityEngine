package com.terminalvelocitycabbage.engine.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface Resource {

	Identifier getIdentifier();

	InputStream openStream() throws IOException;

	Optional<String> asString();
}
