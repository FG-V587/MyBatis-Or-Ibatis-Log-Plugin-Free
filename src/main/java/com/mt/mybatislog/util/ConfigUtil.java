//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mt.mybatislog.util;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.color.ColorUtil;

import java.util.Locale;

public class ConfigUtil {
    public ConfigUtil() {
    }

    public static void setShowMyBatisLog(Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Mybatis Log Plus");
        if (toolWindow != null) {
            if (!toolWindow.isVisible()) {
                toolWindow.show(() -> {
                });
            }

            if (!toolWindow.isActive()) {
                toolWindow.activate(() -> {
                });
            }
        }

    }

    public static String getMapperIdSuffix() {
        String mapperIdSuffix = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:mapper_id_suffix");
        return StringUtils.isNotBlank(mapperIdSuffix) ? mapperIdSuffix : "==>";
    }

    public static void setMapperIdSuffix(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:mapper_id_suffix", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getMapperIdPrefix() {
        String mapperIdPrefix = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:mapper_id_prefix");
        return StringUtils.isNotBlank(mapperIdPrefix) ? mapperIdPrefix : "DEBUG";
    }

    public static void setMapperIdPrefix(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:mapper_id_prefix", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getPreparingKeyWord() {
        String preparingKeyWord = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:preparing");
        return StringUtils.isNotBlank(preparingKeyWord) ? preparingKeyWord : "Preparing:";
    }

    public static void setPreparingKeyWord(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:preparing", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getParametersKeyWord() {
        String parametersKeyWord = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:parameters");
        return StringUtils.isNotBlank(parametersKeyWord) ? parametersKeyWord : "Parameters:";
    }

    public static void setParametersKeyWord(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:parameters", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static boolean getStart() {
        int start = PropertiesComponent.getInstance().getInt("MyBatisLogPlugin:start", 1);
        return start == 1;
    }

    public static void setStart(int value) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:start", value, 1);
    }

    public static boolean getPrettySql() {
        int start = PropertiesComponent.getInstance().getInt("MyBatisLogPlugin:pretty", 1);
        return start == 1;
    }

    public static void setPrettySql(int value) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:pretty", value, 1);
    }

    public static String getSelectColorRgb() {
        String rgb = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:select_color_rgb");
        return StringUtils.isNotBlank(rgb) ? rgb : ColorUtil.toHexString(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor()).toUpperCase(Locale.ROOT);
    }

    public static void setSelectColorRgb(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:select_color_rgb", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getInsertColorRgb() {
        String rgb = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:insert_color_rgb");
        return StringUtils.isNotBlank(rgb) ? rgb : ColorUtil.toHexString(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor()).toUpperCase(Locale.ROOT);
    }

    public static void setInsertColorRgb(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:insert_color_rgb", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getUpdateColorRgb() {
        String rgb = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:update_color_rgb");
        return StringUtils.isNotBlank(rgb) ? rgb : ColorUtil.toHexString(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor()).toUpperCase(Locale.ROOT);
    }

    public static void setUpdateColorRgb(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:update_color_rgb", StringUtils.isBlank(value) ? defaultValue : value);
    }

    public static String getDeleteColorRgb() {
        String rgb = PropertiesComponent.getInstance().getValue("MyBatisLogPlugin:delete_color_rgb");
        return StringUtils.isNotBlank(rgb) ? rgb : ColorUtil.toHexString(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor()).toUpperCase(Locale.ROOT);
    }

    public static void setDeleteColorRgb(String value, String defaultValue) {
        PropertiesComponent.getInstance().setValue("MyBatisLogPlugin:delete_color_rgb", StringUtils.isBlank(value) ? defaultValue : value);
    }
}
