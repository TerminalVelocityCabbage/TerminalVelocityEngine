package com.terminalvelocitycabbage.engine.client.ui;

import com.terminalvelocitycabbage.engine.utils.Color;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGGlyphPosition;
import org.lwjgl.nanovg.NVGTextRow;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static com.terminalvelocitycabbage.engine.utils.NanoVGUtils.rgba;
import static org.lwjgl.system.MemoryUtil.*;

public class TVEUI {

    public static class PerformanceGraph {

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

        public void initGraph(Type style, String name, String font, float xPos, float yPos, float width, float height, Color backgroundColor, Color graphColor, Color titleFontColor, Color valueColor, int graphHistoryCount) {
            this.name = BufferUtils.createByteBuffer(32);
            this.values = new float[graphHistoryCount];
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

        public void renderGraph(long vg) {

            float avg = getGraphAverage();

            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgRect(vg, xPos, yPos, width, height);
            NanoVG.nvgFillColor(vg, rgba(backgroundColor, color));
            NanoVG.nvgFill(vg);

            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgMoveTo(vg, xPos, yPos + height);
            if (type == Type.GRAPH_RENDER_FPS) {
                for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                    float v = 1.0f / (0.00001f + values[(head + i) % GRAPH_HISTORY_COUNT]);
                    float vx, vy;
                    if (v > 1000.0f) {
                        v = 1000.0f;
                    }
                    vx = xPos + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                    vy = yPos + height - ((v / 1000.0f) * height);
                    NanoVG.nvgLineTo(vg, vx, vy);
                }
            } else if (type == Type.GRAPH_RENDER_PERCENT) {
                for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                    float v = values[(head + i) % GRAPH_HISTORY_COUNT] * 1.0f;
                    float vx, vy;
                    if (v > 100.0f) {
                        v = 100.0f;
                    }
                    vx = xPos + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                    vy = yPos + height - ((v / 100.0f) * height);
                    NanoVG.nvgLineTo(vg, vx, vy);
                }
            } else {
                for (int i = 0; i < GRAPH_HISTORY_COUNT; i++) {
                    float v = values[(head + i) % GRAPH_HISTORY_COUNT] * 1000.0f;
                    float vx, vy;
                    if (v > 4.0f) {
                        v = 4.0f;
                    }
                    vx = xPos + ((float)i / (GRAPH_HISTORY_COUNT - 1)) * width;
                    vy = yPos + height - ((v / 4.0f) * height);
                    NanoVG.nvgLineTo(vg, vx, vy);
                }
            }
            NanoVG.nvgLineTo(vg, xPos + width, yPos + height);
            NanoVG.nvgFillColor(vg, rgba(graphColor, color));
            NanoVG.nvgFill(vg);

            NanoVG.nvgFontFace(vg, font);

            //Title at top left of graph
            if (name.get(0) != '\0') {
                NanoVG.nvgFontSize(vg, 14.0f);
                NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
                NanoVG.nvgFillColor(vg, rgba(titleFontColor, color));
                NanoVG.nvgText(vg, xPos + 3, yPos + 1, name);
            }

            //value and unit at right side
            if (type == Type.GRAPH_RENDER_FPS) {
                NanoVG.nvgFontSize(vg, 18.0f);
                NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
                NanoVG.nvgFillColor(vg, rgba(valueColor, color));
                NanoVG.nvgText(vg, xPos + width - 3, yPos + 1, String.format("%.2f FPS", 1.0f / avg));

                //Include ms at bottom too
                NanoVG.nvgFontSize(vg, 15.0f);
                NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_BOTTOM);
                NanoVG.nvgFillColor(vg, rgba(valueColor, color));
                NanoVG.nvgText(vg, xPos + width - 3, yPos + height - 1, String.format("%.2f ms", avg * 1000.0f));
            } else if (type == Type.GRAPH_RENDER_PERCENT) {
                NanoVG.nvgFontSize(vg, 18.0f);
                NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
                NanoVG.nvgFillColor(vg, rgba(valueColor, color));
                NanoVG.nvgText(vg, xPos + width - 3, yPos + 1, String.format("%.1f %%", avg * 1.0f));
            } else {
                NanoVG.nvgFontSize(vg, 18.0f);
                NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_TOP);
                NanoVG.nvgFillColor(vg, rgba(valueColor, color));
                NanoVG.nvgText(vg, xPos + width - 3, yPos + 1, String.format("%.2f ms", avg * 1000.0f));
            }
        }

        public String getReadableName() {
            return readableName;
        }

        public Type getType() {
            return type;
        }
    }

    public static class Paragraph {

        private static final NVGTextRow.Buffer       rows      = NVGTextRow.create(3);
        private static final NVGGlyphPosition.Buffer glyphs    = NVGGlyphPosition.create(100);
        private static final FloatBuffer lineh  = BufferUtils.createFloatBuffer(1);

        static final NVGColor color = NVGColor.create();
        String font;
        String text;

        public void initParagraph(String font) {
            this.font = font;
        }

        public void updateParagraph(String text) {
            this.text = text;
        }

        public void drawParagraph(long vg, float x, float y, float width, float height, float mx, float my) {

            NanoVG.nvgSave(vg);

            NanoVG.nvgFontSize(vg, 18.0f);
            NanoVG.nvgFontFace(vg, font);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgTextMetrics(vg, null, null, lineh);

            // The text break API can be used to fill a large buffer of rows,
            // or to iterate over the text just few lines (or just one) at a time.
            // The "next" variable of the last returned item tells where to continue.
            ByteBuffer paragraph = memUTF8(text, false);
            long start = memAddress(paragraph);
            long end = start + paragraph.remaining();
            int  nrows, lnum = 0;
            while ((nrows = NanoVG.nnvgTextBreakLines(vg, start, end, width, MemoryUtil.memAddress(rows), 3)) != 0) {
                for (int i = 0; i < nrows; i++) {
                    NVGTextRow row = rows.get(i);
                    boolean    hit = mx > x && mx < (x + width) && my >= y && my < (y + lineh.get(0));

                    NanoVG.nvgBeginPath(vg);
                    NanoVG.nvgFillColor(vg, rgba(255, 255, 255, hit ? 64 : 16, color));
                    NanoVG.nvgRect(vg, x, y, row.width(), lineh.get(0));
                    NanoVG.nvgFill(vg);

                    NanoVG.nvgFillColor(vg, rgba(255, 255, 255, 255, color));
                    NanoVG.nnvgText(vg, x, y, row.start(), row.end());

                    if (hit) {
                        drawCaret(vg, row, lineh.get(0), x, y, mx);
                    }
                    lnum++;
                    y += lineh.get(0);
                }
                // Keep going...
                start = rows.get(nrows - 1).next();
            }
        }

        private static void drawCaret(long vg, NVGTextRow row, float lineh, float x, float y, float mx) {
            float caretx = (mx < x + row.width() / 2) ? x : x + row.width();

            float px = x;

            int nglyphs = NanoVG.nnvgTextGlyphPositions(vg, x, y, row.start(), row.end(), MemoryUtil.memAddress(glyphs), 1000);
            for (int j = 0; j < nglyphs; j++) {
                NVGGlyphPosition glyphPosition = glyphs.get(j);

                float x0  = glyphPosition.x();
                float x1  = (j + 1 < nglyphs) ? glyphs.get(j + 1).x() : x + row.width();
                float gx2 = x0 * 0.3f + x1 * 0.7f;

                if (mx >= px && mx < gx2) {
                    caretx = glyphPosition.x();
                }
                px = gx2;
            }
            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgFillColor(vg, rgba(255, 192, 0, 255, color));
            NanoVG.nvgRect(vg, caretx, y, 1, lineh);
            NanoVG.nvgFill(vg);
        }
    }

    public static class Tooltip {

        private static final FloatBuffer bounds = BufferUtils.createFloatBuffer(4);
        private static final ByteBuffer hoverText = memASCII("Hover your mouse over the text to see calculated caret position.", false);
        static final NVGColor color = NVGColor.create();

        private static void drawTooltip(long vg, float x, float y, float mx, float my) {
            NanoVG.nvgFontSize(vg, 13.0f);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
            NanoVG.nvgTextLineHeight(vg, 1.2f);

            NanoVG.nvgTextBoxBounds(vg, x, y, 150, hoverText, bounds);

            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgFillColor(vg, rgba(220, 220, 220, 255, color));
            NanoVG.nvgRoundedRect(vg, bounds.get(0) - 2, bounds.get(1) - 2, (int)(bounds.get(2) - bounds.get(0)) + 4, (int)(bounds.get(3) - bounds.get(1)) + 4, 3);
            int px = (int)((bounds.get(2) + bounds.get(0)) / 2);
            NanoVG.nvgMoveTo(vg, px, bounds.get(1) - 10);
            NanoVG.nvgLineTo(vg, px + 7, bounds.get(1) + 1);
            NanoVG.nvgLineTo(vg, px - 7, bounds.get(1) + 1);
            NanoVG.nvgFill(vg);

            NanoVG.nvgFillColor(vg, rgba(0, 0, 0, 220, color));
            NanoVG.nvgTextBox(vg, x, y, 150, hoverText);

            NanoVG.nvgRestore(vg);
        }
    }
}
