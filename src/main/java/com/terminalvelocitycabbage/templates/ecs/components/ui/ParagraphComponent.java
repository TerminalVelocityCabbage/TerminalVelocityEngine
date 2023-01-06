package com.terminalvelocitycabbage.templates.ecs.components.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.ecs.Component;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGGlyphPosition;
import org.lwjgl.nanovg.NVGTextRow;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.terminalvelocitycabbage.engine.utils.NanoVGUtils.rgba;
import static org.lwjgl.system.MemoryUtil.*;

public class ParagraphComponent implements Component, UIRenderable {

    private static final NVGTextRow.Buffer       rows      = NVGTextRow.create(3);
    private static final NVGGlyphPosition.Buffer glyphs    = NVGGlyphPosition.create(100);
    private static final FloatBuffer lineh  = BufferUtils.createFloatBuffer(1);
    private static final FloatBuffer bounds = BufferUtils.createFloatBuffer(4);
    private static final ByteBuffer paragraph = memUTF8(
            "This is longer chunk of text.\n  \n  Would have used lorem ipsum but she    was busy jumping over the lazy dog with the fox and all the men " +
                    "who came to the aid of the party.🎉",
            false
    );
    private static final ByteBuffer hoverText = memASCII("Hover your mouse over the text to see calculated caret position.", false);

    static final NVGColor color = NVGColor.create();
    String font;

    @Override
    public void draw(long vg) {
        drawParagraph(vg, 20, ClientBase.getWindow().height() - 400, 400f, 100, (float) ClientBase.getWindow().getCursorX(), (float) ClientBase.getWindow().getCursorY());
    }

    @Override
    public void setDefaults() {

    }

    public void initParagraph(String font) {
        this.font = font;
    }

    private void drawParagraph(long vg, float x, float y, float width, float height, float mx, float my) {
        float gx = 0.0f, gy = 0.0f;

        int gutter = 0;

        NanoVG.nvgSave(vg);

        NanoVG.nvgFontSize(vg, 18.0f);
        NanoVG.nvgFontFace(vg, font);
        NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
        NanoVG.nvgTextMetrics(vg, null, null, lineh);

        // The text break API can be used to fill a large buffer of rows,
        // or to iterate over the text just few lines (or just one) at a time.
        // The "next" variable of the last returned item tells where to continue.
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

                    gutter = lnum + 1;
                    gx = x - 10;
                    gy = y + lineh.get(0) / 2;
                }
                lnum++;
                y += lineh.get(0);
            }
            // Keep going...
            start = rows.get(nrows - 1).next();
        }

        if (gutter != 0) {
            drawLineNumber(vg, gutter, gx, gy, bounds);
        }

        y += 20.0f;

        drawTooltip(vg, x, y, mx, my);
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

    private static void drawLineNumber(long vg, int gutter, float gx, float gy, FloatBuffer bounds) {
        String txt = Integer.toString(gutter);

        NanoVG.nvgFontSize(vg, 13.0f);
        NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_RIGHT | NanoVG.NVG_ALIGN_MIDDLE);

        NanoVG.nvgTextBounds(vg, gx, gy, txt, bounds);

        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgFillColor(vg, rgba(255, 192, 0, 255, color));
        NanoVG.nvgRoundedRect(vg,
                (int)bounds.get(0) - 4,
                (int)bounds.get(1) - 2,
                (int)(bounds.get(2) - bounds.get(0)) + 8,
                (int)(bounds.get(3) - bounds.get(1)) + 4,
                ((int)(bounds.get(3) - bounds.get(1)) + 4) / 2 - 1);
        NanoVG.nvgFill(vg);

        NanoVG.nvgFillColor(vg, rgba(32, 32, 32, 255, color));
        NanoVG.nvgText(vg, gx, gy, txt);
    }

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
