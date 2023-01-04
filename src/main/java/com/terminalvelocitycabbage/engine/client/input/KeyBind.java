package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.ClientBase;

public record KeyBind(long window, int keyCode, int scancode, int action, int modifiers) {

    /**
     * Manually creates a keybind for comparison later, this constructor is intended for Engine use only and should not
     * be used by the user, instead use the keybind builder for your own keybinds.
     *
     * @param keyCode The GLFW key code assigned to this keybind
     * @param scancode one of {@link KeyScanCodes}
     * @param action one of {@link KeyActions}
     * @param modifiers one of {@link KeyModifiers}
     */
    @Deprecated()
    public KeyBind(int keyCode, int scancode, int action, int modifiers) {
        this(ClientBase.getWindow().getID(), keyCode, scancode, action, modifiers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean matches(KeyBind keyBind) {
        return matches(keyBind, true, false);
    }

    public boolean matches(KeyBind keyBind, boolean matchAction) {
        return matches(keyBind, matchAction, false);
    }

    public boolean matches(KeyBind keyBind, boolean matchAction, boolean matchModifiers) {
        if (matchAction) {
            if (matchModifiers) return equalsKeyModifiersAction(keyBind);
            else return equalsKeyAction(keyBind);
        } else {
            if (matchModifiers) return equalsKeyModifiers(keyBind);
            else return equalsKey(keyBind);
        }
    }

    /**
     * @param o the keybind you want to compare this to
     * @return a boolean whether this key and action match that of the provided o
     */
    private boolean equalsKeyAction(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode && action == keyBind.action;
    }

    /**
     * @param o the keybind you want to compare this to
     * @return a boolean whether this key and modifiers and action match that of the provided o
     */
    private boolean equalsKeyModifiersAction(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode && action == keyBind.action && modifiers == keyBind.modifiers;
    }

    /**
     * @param o the keybind you want to compare this to
     * @return a boolean whether this key and modifiers match that of the provided o
     */
    private boolean equalsKeyModifiers(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode && modifiers == keyBind.modifiers;
    }

    /**
     * @param o the keybind you want to compare this to
     * @return a boolean whether this key match that of the provided o
     */
    private boolean equalsKey(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode;
    }

    public static class Builder {

        int keyBind;
        int keyAction;
        int keyScanCode;
        byte keyModifiers;

        Builder() {
            keyBind = -1;
            keyAction = KeyActions.PRESS.getActionInt();
            keyScanCode = KeyScanCodes.ANY.getScanCodeInt();
            keyModifiers = KeyModifiers.NONE.getModifierByte();
        }

        public Builder key(int key) {
            keyBind = key;
            return this;
        }

        public Builder  action(KeyActions action) {
            keyAction = action.getActionInt();
            return this;
        }

        public Builder modifiers(KeyModifiers... modifiers) {
            byte modifiersByte = 0;
            for (KeyModifiers keyModifiers1 : modifiers) {
                modifiersByte += keyModifiers1.getModifierByte();
            }
            keyModifiers = modifiersByte;
            return this;
        }

        public Builder scanCodes(KeyScanCodes scanCode) {
            keyScanCode = scanCode.getScanCodeInt();
            return this;
        }

        public KeyBind build() {
            return new KeyBind(keyBind, keyScanCode, keyAction, keyModifiers);
        }
    }
}
