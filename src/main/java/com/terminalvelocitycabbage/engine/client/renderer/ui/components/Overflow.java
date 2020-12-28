package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

public class Overflow {

	public static final Overflow SHOWN = new Overflow(X.SHOWN, Y.SHOWN);
	public static final Overflow HIDDEN = new Overflow(X.HIDDEN, Y.HIDDEN);
	public static final Overflow HIDE_X = new Overflow(X.HIDDEN, Y.SHOWN);
	public static final Overflow HIDE_Y = new Overflow(X.SHOWN, Y.HIDDEN);

	X overflowX;
	Y overflowY;

	private Overflow(X overflowX, Y overflowY) {
		this.overflowX = overflowX;
		this.overflowY = overflowY;
	}

	public boolean hideX() {
		return !overflowX.shown;
	}

	public boolean hideY() {
		return !overflowY.shown;
	}

	public enum X {

		SHOWN(true, "shown"),
		HIDDEN(false, "hidden");

		public boolean shown;
		public String name;

		X(boolean wrap, String name) {
			this.shown = wrap;
			this.name = name;
		}

		public boolean isShown() {
			return shown;
		}

		public String getName() {
			return name;
		}
	}

	public enum Y {

		SHOWN(true, "shown"),
		HIDDEN(false, "hidden");

		public boolean shown;
		public String name;

		Y(boolean wrap, String name) {
			this.shown = wrap;
			this.name = name;
		}

		public boolean isShown() {
			return shown;
		}

		public String getName() {
			return name;
		}
	}

}
