package com.terminalvelocitycabbage.engine.model;

public abstract class Model {

	//A model is rendered in a single UBO
	//The goal of the model class is to be able to give Primitives in 3d space to be combined in init time into
	//a single UBO to be reused during the game loop

	//All models will be made up of quads (two tris not a literal OpenGL quad) so we can just have an array of
	//these quads in 3d space to work with


}
