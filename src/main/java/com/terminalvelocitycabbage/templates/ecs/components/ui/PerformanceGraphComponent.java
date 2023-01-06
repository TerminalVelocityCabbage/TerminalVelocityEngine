package com.terminalvelocitycabbage.templates.ecs.components.ui;

import com.terminalvelocitycabbage.engine.client.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.ecs.Component;
import com.terminalvelocitycabbage.engine.utils.Color;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.terminalvelocitycabbage.engine.utils.NanoVGUtils.rgba;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class PerformanceGraphComponent implements Component, UIRenderable {

    protected float xPos = 0;
    protected float yPos = 0;

    protected float width = 0;
    protected float height = 0;

    String font = null;

    Color backgroundColor;
    Color graphColor;
    Color titleFontColor;
    Color valueColor;

    Type type;
    ByteBuffer name;
    String readableName;
    float[] values;
    int head;
    static final NVGColor color = NVGColor.create();

    @Override
    public void setDefaults() {
        name = BufferUtils.createByteBuffer(32);
        values = new float[GRAPH_HISTORY_COUNT];
    }

    public enum Type {
        GRAPH_RENDER_FPS(0),
        GRAPH_RENDER_MS(1),
        GRAPH_RENDER_PERCENT(2);
        
        int index;
        
        Type(int index) {
            this.index = index;
        }
    }

    private static final int GRAPH_HISTORY_COUNT = 100;

    public PerformanceGraphComponent() {
        
    }

    @Override
    public void draw(long vg) {
        renderGraph(vg, xPos, yPos);
    }

    public void initGraph(Type style, String name, String font, float xPos, float yPos, float width, float height, Color backgroundColor, Color graphColor, Color titleFontColor, Color valueColor) {
        this.type = style;
        this.readableName = name;
        memUTF8(name, false, this.name);
        Arrays.fill(values, 0);
        head = 0;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.graphColor = graphColor;
        this.titleFontColor = titleFontColor;
        this.valueColor = valueColor;
    }

    public void updateGraph(float frameTime) {
        head = (head + 1) % GRAPH_HISTORY_COUNT;
        values[head] = frameTime;
    }

    private float getGraphAverage() {
        float avg = 0;
        for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
            avg += values[i];
        }
        return avg / GRAPH_HISTORY_COUNT;
    }

    private void renderGraph(long vg, float x, float y) {

        float avg = getGraphAverage();

        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x, y, width, height);
        NanoVG.nvgFillColor(vg, rgba(backgroundColor, color));
        NanoVG.nvgFill(vg);

        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgMoveTo(vg, x, y + height);
        if (type == Type.GRAPH_RENDER_FPS) {
            for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                float v = 1.0f / (0.00001f + values[(head + i) % GRAPH_HISTORY_COUNT]);
                float vx, vy;
                if (v > 1000.0f) {
                    v = 1000.0f;
                }
                vx = x + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                vy = y + height - ((v / 1000.0f) * height);
                NanoVG.nvgLineTo(vg, vx, vy);
            }
        } else if (type == Type.GRAPH_RENDER_PERCENT) {
            for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                float v = values[(head + i) % GRAPH_HISTORY_COUNT] * 1.0f;
                float vx, vy;
                if (v > 100.0f) {
                    v = 100.0f;
                }
                vx = x + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                vy = y + height - ((v / 100.0f) * height);
                NanoVG.nvgLineTo(vg, vx, vy);
            }
        } else {
            for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                float v = values[(head + i) % GRAPH_HISTORY_COUNT] * 1000.0f;
                float vx, vy;
                if (v > 4.0f) {
                    v = 4.0f;
                }
                vx = x + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                vy = y + height - ((v / 4.0f) * height);
                NanoVG.nvgLineTo(vg, vx, vy);
            }
        }
        NanoVG.nvgLineTo(vg, x + width, y + height);
        NanoVG.nvgFillColor(vg, rgba(graphColor, color));
        NanoVG.nvgFill(vg);

        NanoVG.nvgFontFace(vg, font);

        //Title at top left of graph
        if (name.get(0) != '\0') {
            NanoVG.nvgFontSize(vg, 14.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgFillColor(vg, rgba(titleFontColor, color));
            NanoVG.nvgText(vg, x + 3, y + 1, name);
        }

        //value and unit at right side
        if (type == Type.GRAPH_RENDER_FPS) {
            NanoVG.nvgFontSize(vg, 18.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgFillColor(vg, rgba(valueColor, color));
            NanoVG.nvgText(vg, x + width - 3, y + 1, String.format("%.2f FPS", 1.0f / avg));

            //Include ms at bottom too
            NanoVG.nvgFontSize(vg, 15.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_BOTTOM);
            NanoVG.nvgFillColor(vg, rgba(valueColor, color));
            NanoVG.nvgText(vg, x + width - 3, y + height - 1, String.format("%.2f ms", avg * 1000.0f));
        } else if (type == Type.GRAPH_RENDER_PERCENT) {
            NanoVG.nvgFontSize(vg, 18.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgFillColor(vg, rgba(valueColor, color));
            NanoVG.nvgText(vg, x + width - 3, y + 1, String.format("%.1f %%", avg * 1.0f));
        } else {
            NanoVG.nvgFontSize(vg, 18.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgFillColor(vg, rgba(valueColor, color));
            NanoVG.nvgText(vg, x + width - 3, y + 1, String.format("%.2f ms", avg * 1000.0f));
        }
    }

    public String getReadableName() {
        return readableName;
    }

    public Type getType() {
        return type;
    }
}
