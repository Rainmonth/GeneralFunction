package com.rainmonth.floatview;

/**
 * 悬浮View配置
 */
public class FloatViewConfig {
    // 是否自适应（即为获取到权限时 自动降级处理）
    public boolean autoCompat = true;
    // 是否自动隐藏
    public boolean autoHide = false;
    // 是否是拖动模式，true 表示可以在窗口内拖动，false 表示固定位置
    public boolean isDragMode = true;
    // 是否支持全局悬浮，true 表示即使应用退到后台，也可以悬浮展示
    public boolean isGlobalFloat = false;

}
