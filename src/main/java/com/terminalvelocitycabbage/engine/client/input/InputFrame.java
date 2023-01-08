package com.terminalvelocitycabbage.engine.client.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class InputFrame {

    //Mouse velocity variables
    Vector2d previousPos;
    Vector2f deltaMouseVector;
    Vector2i deltaScrollVector;
    //Mouse tracked states
    boolean focused;
    boolean inside;
    //Left mouse button tracking
    boolean lastLeftButtonReleased;
    boolean leftButtonReleased;
    boolean lastLeftButtonPressed;
    boolean leftButtonPressed;
    boolean refLeftMouseClicked;
    short leftButtonHeldTime;
    //Right mouse tracking
    boolean lastRightButtonReleased;
    boolean rightButtonReleased;
    boolean lastRightButtonPressed;
    boolean rightButtonPressed;
    boolean refRightMouseClicked;
    short rightButtonHeldTime;
    //Track last click time for double-clicking
    short ticksSinceLastClick;
    List<KeyBind> seenKeys;
    String seenChars;

    public InputFrame() {
        this(
                new Vector2d(-10, -10),
                new Vector2f(0, 0),
                new Vector2i(0, 0),
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                (short) -1,
                false,
                false,
                false,
                false,
                false,
                (short) -1,
                (short) -1,
                new ArrayList<>()
        );
    }
    
    public InputFrame(Vector2d previousPos, Vector2f deltaMouseVector, Vector2i deltaScrollVector, boolean focused,
                      boolean inside, boolean lastLeftButtonReleased, boolean leftButtonReleased,
                      boolean lastLeftButtonPressed, boolean leftButtonPressed, boolean refLeftMouseClicked,
                      short leftButtonHeldTime, boolean lastRightButtonReleased, boolean rightButtonReleased,
                      boolean lastRightButtonPressed, boolean rightButtonPressed, boolean refRightMouseClicked,
                      short rightButtonHeldTime, short ticksSinceLastClick, List<KeyBind> seenKeys) {
        //Mouse velocity variables
        this.previousPos = previousPos;
        this.deltaMouseVector = deltaMouseVector;
        this.deltaScrollVector = deltaScrollVector;
        this.focused = focused;
        this.inside = inside;
        this.lastLeftButtonReleased = lastLeftButtonReleased;
        this.leftButtonReleased = leftButtonReleased;
        this.lastLeftButtonPressed = lastLeftButtonPressed;
        this.leftButtonPressed = leftButtonPressed;
        this.refLeftMouseClicked = refLeftMouseClicked;
        this.leftButtonHeldTime = leftButtonHeldTime;
        this.lastRightButtonReleased = lastRightButtonReleased;
        this.rightButtonReleased = rightButtonReleased;
        this.lastRightButtonPressed = lastRightButtonReleased;
        this.rightButtonPressed = rightButtonPressed;
        this.refRightMouseClicked = refRightMouseClicked;
        this.rightButtonHeldTime = rightButtonHeldTime;
        this.ticksSinceLastClick = ticksSinceLastClick;
        this.seenKeys = seenKeys;
    }

    public Vector2d getPreviousPos() {
        return previousPos;
    }

    public Vector2f getDeltaMouseVector() {
        return deltaMouseVector;
    }

    public float getMouseDeltaX() {
        return deltaMouseVector.x;
    }

    public float getMouseDeltaY() {
        return deltaMouseVector.y;
    }

    public Vector2i getDeltaScrollVector() {
        return deltaScrollVector;
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isInside() {
        return inside;
    }

    public boolean isLastLeftButtonReleased() {
        return lastLeftButtonReleased;
    }

    public boolean isLeftButtonReleased() {
        return leftButtonReleased;
    }

    public boolean isLastLeftButtonPressed() {
        return lastLeftButtonPressed;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRefLeftMouseClicked() {
        return refLeftMouseClicked;
    }

    public short getLeftButtonHeldTime() {
        return leftButtonHeldTime;
    }

    public boolean isLastRightButtonReleased() {
        return lastRightButtonReleased;
    }

    public boolean isRightButtonReleased() {
        return rightButtonReleased;
    }

    public boolean isLastRightButtonPressed() {
        return lastRightButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public boolean isRefRightMouseClicked() {
        return refRightMouseClicked;
    }

    public short getRightButtonHeldTime() {
        return rightButtonHeldTime;
    }

    public short getTicksSinceLastClick() {
        return ticksSinceLastClick;
    }

    public List<KeyBind> getSeenKeys() {
        return seenKeys;
    }

    public boolean keyMatches(KeyBind keyBind) {
        for (KeyBind keyBind1 : seenKeys) {
            if (keyBind1.matches(keyBind)) return true;
        }
        return false;
    }

    public boolean keyMatches(KeyBind keyBind, boolean matchAction) {
        for (KeyBind keyBind1 : seenKeys) {
            if (keyBind1.matches(keyBind, matchAction)) return true;
        }
        return false;
    }

    public boolean keyMatches(KeyBind keyBind, boolean matchAction, boolean matchModifiers) {
        for (KeyBind keyBind1 : seenKeys) {
            if (keyBind1.matches(keyBind, matchAction, matchModifiers)) return true;
        }
        return false;
    }

    public String getSeenChars() {
        if (seenChars == null) return "";
        return seenChars;
    }
}
