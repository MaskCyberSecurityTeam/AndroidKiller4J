package com.funnysec.richardtang.androidkiller4j.constant;

import cn.hutool.core.io.resource.ResourceUtil;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * 项目中用到的字体图标
 *
 * @author RichardTang
 */
public class Icon {

    static {
        GlyphFontRegistry.register("icomoon", ResourceUtil.getStream("ttf/icomoon.ttf"), 20);
    }

    public final static GlyphFont ICOMOON      = GlyphFontRegistry.font("icomoon");
    public final static GlyphFont FONT_AWESOME = GlyphFontRegistry.font("FontAwesome");

    // MainView
    public final static Glyph MAIN_VIEW_LOG_CELAR = FONT_AWESOME.create(FontAwesome.Glyph.TRASH).size(20);

    // TookitView
    public final static Glyph TOOLKIT_VIEW_DISCONNECT    = ICOMOON.create("\ue060").size(20);
    public final static Glyph TOOLKIT_VIEW_ABOUT         = FONT_AWESOME.create("\uf2bb").size(20);
    public final static Glyph TOOLKIT_VIEW_CONNECT       = FONT_AWESOME.create("\uf2b5").size(20);
    public final static Glyph TOOLKIT_VIEW_LOG           = FONT_AWESOME.create(FontAwesome.Glyph.BUG).size(20);
    public final static Glyph TOOLKIT_VIEW_COMPILE       = FONT_AWESOME.create(FontAwesome.Glyph.LEGAL).size(20);
    public final static Glyph TOOLKIT_VIEW_REFRESH       = FONT_AWESOME.create(FontAwesome.Glyph.REFRESH).size(20);
    public final static Glyph TOOLKIT_VIEW_SIGNATURE     = FONT_AWESOME.create(FontAwesome.Glyph.PENCIL).size(20);
    public final static Glyph TOOLKIT_VIEW_SETTING       = FONT_AWESOME.create(FontAwesome.Glyph.COGS).size(20);
    public final static Glyph TOOLKIT_VIEW_OPEN          = FONT_AWESOME.create(FontAwesome.Glyph.FOLDER_OPEN).size(20);
    public final static Glyph TOOLKIT_VIEW_CUSTOM        = FONT_AWESOME.create(FontAwesome.Glyph.PUZZLE_PIECE).size(20);
    public final static Glyph TOOLKIT_VIEW_FIND_SHELL    = FONT_AWESOME.create(FontAwesome.Glyph.SEARCH).size(20);
    public final static Glyph TOOLKIT_VIEW_PROCESS       = FONT_AWESOME.create(FontAwesome.Glyph.AREA_CHART).size(20);
    public final static Glyph TOOLKIT_VIEW_INSTALL       = FONT_AWESOME.create(FontAwesome.Glyph.DOWNLOAD).size(20);
    public final static Glyph TOOLKIT_VIEW_BASH          = FONT_AWESOME.create(FontAwesome.Glyph.TERMINAL).size(20);
    public final static Glyph TOOLKIT_VIEW_ANDROID       = FONT_AWESOME.create(FontAwesome.Glyph.ANDROID).size(18);
    public final static Glyph TOOLKIT_VIEW_SYSTEM        = FONT_AWESOME.create(FontAwesome.Glyph.COG).size(18);
    public final static Glyph TOOLKIT_VIEW_DEVICE        = FONT_AWESOME.create(FontAwesome.Glyph.MOBILE_PHONE).size(22);
    public final static Glyph TOOLKIT_VIEW_FILE_EXPLORER = FONT_AWESOME.create(FontAwesome.Glyph.FOLDER).size(22);

    // SignatureView
    public final static Glyph SIGNATURE_VIEW_TAB = FONT_AWESOME.create(FontAwesome.Glyph.PENCIL).size(20);

    // TaskView
    public final static Glyph TASK_VIEW_TAB   = FONT_AWESOME.create(FontAwesome.Glyph.TASKS).size(20);
    public final static Glyph DEVICE_BASH_TAB = FONT_AWESOME.create(FontAwesome.Glyph.TERMINAL).size(20);

    // ControlView
    public final static Glyph DEVICE_LOG_OUTPUT = FONT_AWESOME.create(FontAwesome.Glyph.BUG).size(15);

    // DeviceLogView
    public final static Glyph DEVICE_LOG_TAB          = FONT_AWESOME.create(FontAwesome.Glyph.BUG).size(20);
    public final static Glyph DEVICE_LOG_FILTER_STOP  = ICOMOON.create("\uf28d").size(16);
    public final static Glyph DEVICE_LOG_FILTER_START = FONT_AWESOME.create(FontAwesome.Glyph.PLAY_CIRCLE).size(16);

    // DeviceProcessView
    public final static Glyph DEVICE_PROCESS_TAB   = FONT_AWESOME.create(FontAwesome.Glyph.AREA_CHART).size(20);
    public final static Glyph DEVICE_PROCESS_STOP  = ICOMOON.create("\uf28d").size(16);
    public final static Glyph DEVICE_PROCESS_START = FONT_AWESOME.create(FontAwesome.Glyph.PLAY_CIRCLE).size(16);

    // WorkbenchView
    public final static Glyph WORKBENCH_INFO    = FONT_AWESOME.create(FontAwesome.Glyph.INFO_CIRCLE).size(16);
    public final static Glyph WORKBENCH_MANAGER = FONT_AWESOME.create(FontAwesome.Glyph.SITEMAP).size(16);
    public final static Glyph WORKBENCH_SEARCH  = FONT_AWESOME.create(FontAwesome.Glyph.SEARCH).size(16);

    // DeviceExplorerView
    public final static Glyph DEVICE_EXPLORER_VIEW_TAB = FONT_AWESOME.create(FontAwesome.Glyph.FOLDER).size(22);

}
