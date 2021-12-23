package com.terminalvelocitycabbage.engine.client.renderer.elements;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;

import java.util.*;

public class RenderFormat {

    public static final RenderFormat POSITION = new RenderFormat(RenderElement.POSITION);
    public static final RenderFormat POSITION_UV = new RenderFormat(RenderElement.POSITION, RenderElement.UV);
    public static final RenderFormat POSITION_UV_NORMAL = new RenderFormat(RenderElement.POSITION, RenderElement.UV, RenderElement.NORMAL);
    public static final RenderFormat POSITION_UV_COLOUR = new RenderFormat(RenderElement.POSITION, RenderElement.UV, RenderElement.COLOUR_RGBA);
    public static final RenderFormat POSITION_UV_NORMAL_COLOUR = new RenderFormat(RenderElement.POSITION, RenderElement.UV, RenderElement.NORMAL, RenderElement.COLOUR_RGBA);
    public static final RenderFormat POSITION_COLOUR = new RenderFormat(RenderElement.POSITION, RenderElement.COLOUR_RGBA);

    public static final RenderFormat UI = new RenderFormat(
        RenderElement.POSITION, RenderElement.UV, RenderElement.COLOUR_RGBA, RenderElement.BORDER_THICKNESS
    );

    private final List<RenderElement> elementList;
    private final Map<RenderElement, Integer> offsetList = new HashMap<>();

    private final int stride;

    public RenderFormat(RenderElement... elementList) {
        this.elementList = Arrays.asList(elementList);
        int stride = 0;
        int offset = 0;
        for (RenderElement element : this.elementList) {
            this.offsetList.put(element, offset);
            stride += element.getSize();
            offset += element.getCount();
        }
        this.stride = stride;
    }

    public int getStrideBytes() {
        return stride;
    }

    public int getStride() {
        return this.stride / Float.BYTES;
    }

    public List<RenderElement> getElementList() {
        return elementList;
    }

    public float[] getData(RenderElement element, Vertex vertex) {
        if(!this.offsetList.containsKey(element)) {
            return new float[element.getCount()];
        }
        return vertex.grabData(this.offsetList.get(element), element.getCount());
    }

    public int getElementOffset(RenderElement element, int fallback) {
        return this.offsetList.getOrDefault(element, fallback);
    }
}
