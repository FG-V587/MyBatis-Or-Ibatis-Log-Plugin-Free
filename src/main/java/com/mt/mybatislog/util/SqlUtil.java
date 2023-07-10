package com.mt.mybatislog.util;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.util.ArrayUtil;
import com.mt.mybatislog.BasicFormatter;
import com.mt.mybatislog.enums.JavaTypeEnum;
import com.mt.mybatislog.gui.MyBatisLogManager;
import com.mt.mybatislog.vo.SqlLogInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 18T004
 */
public class SqlUtil {
    private static final BasicFormatter BASIC_FORMATTER = new BasicFormatter();
    private static final String SEPARATE = "(\\(.*?\\))?,\\s";
    private static final Pattern PATTERN_SEPARATE = Pattern.compile("(\\(.*?\\))?,\\s");
    public static boolean ENABLE_FORMAT = true;
    public static boolean SHOW_LITERAL = false;
    private static final Map<String, SqlLogInfo> SQL_LOG_INFO_MAP = new ConcurrentHashMap();
    private static String preparingLine = "";
    private static String parametersLine = "";
    private static String parametersTypesLine = "";
    private static Project Project;

    public SqlUtil() {
    }

    public static String getSqlType(String sql) {
        if (StringUtils.isBlank(sql)) {
            return "";
        } else {
            String lowerLine = sql.toLowerCase().trim();
            if (lowerLine.startsWith("insert")) {
                return "insert";
            } else if (lowerLine.startsWith("update")) {
                return "update";
            } else if (lowerLine.startsWith("delete")) {
                return "delete";
            } else {
                return lowerLine.startsWith("select") ? "select" : "";
            }
        }
    }

    public static String[] recoverSql(String preparingRowLog, String parametersRowLog) {

        String preparingKeyWord = ConfigUtil.getPreparingKeyWord();
        String parametersKeyWord = ConfigUtil.getParametersKeyWord();
        return recoverSql(preparingKeyWord, parametersKeyWord, preparingRowLog, parametersRowLog);
    }

    public static String[] recoverSql(String preparingKeyWord, String parametersKeyWord, String preparingRowLog, String parametersRowLog) {
        String[] preparingLineSplit = preparingRowLog.split(preparingKeyWord);
        String[] parametersLineSplit = parametersRowLog.split(parametersKeyWord);
        Object[] preparingArr = getPreparing(preparingLineSplit);
        Object[] parametersArr = getParameters(parametersLineSplit);

        try {
            String var10000 = SqlUtil.format((String)preparingArr[1], parametersArr).trim();
            String result = var10000 + ";";
            if (ConfigUtil.getPrettySql()) {
                result = BASIC_FORMATTER.format(result);
            }

            return new String[]{(String)preparingArr[0], result};
        } catch (Exception var10) {
            String result = BASIC_FORMATTER.format((String)preparingArr[1]);
            return new String[]{(String)preparingArr[0], result};
        }
    }
    public static String formatWith(String strPattern, String placeHolder, Object... argArray) {
        if (org.apache.commons.lang3.StringUtils.isBlank(strPattern) || org.apache.commons.lang3.StringUtils.isBlank(placeHolder) || ArrayUtil.isEmpty(argArray)) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        final int placeHolderLength = placeHolder.length();

        // 初始化定义好的长度以获得更好的性能
        final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf(placeHolder, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }

            // 转义符
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == '\\') {// 转义符
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == '\\') {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(SqlUtil.utf8Str(argArray[argIndex]));
                    handledPosition = delimIndex + placeHolderLength;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(placeHolder.charAt(0));
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                sbuf.append(strPattern, handledPosition, delimIndex);
                sbuf.append(SqlUtil.utf8Str(argArray[argIndex]));
                handledPosition = delimIndex + placeHolderLength;
            }
        }

        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPatternLength);

        return sbuf.toString();
    }
    public static String utf8Str(Object obj) {
        return str(obj, "UTF-8");
    }
    public static String str(Object obj, String charset) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return str((byte[]) obj, charset);
        } else if (obj instanceof Byte[]) {
            return str((Byte[]) obj, charset);
        } else if (obj instanceof ByteBuffer) {
            return str((ByteBuffer) obj, charset);
        } else if (SqlUtil.isArray(obj)) {
            return SqlUtil.toString(obj);
        }

        return obj.toString();
    }
    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }

        if (obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        } else if (obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        } else if (obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        } else if (obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        } else if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        } else if (obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        } else if (obj instanceof double[]) {
            return Arrays.toString((double[]) obj);
        } else if (SqlUtil.isArray(obj)) {
            // 对象数组
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception ignore) {
                //ignore
            }
        }

        return obj.toString();
    }
    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }
    private static Object[] getPreparing(String[] preparingRowLogSplitArr) {
        String mapperId = "";
        String preparingSql = "";
        if (preparingRowLogSplitArr.length == NumberUtils.INTEGER_TWO) {
            mapperId = preparingRowLogSplitArr[0];
            preparingSql = preparingRowLogSplitArr[1].replaceAll("\\?", "{}");
        }

        return new Object[]{mapperId, preparingSql};
    }

    private static Object[] getParameters(String[] parametersRowLogSplit) {
        if (parametersRowLogSplit.length == NumberUtils.INTEGER_TWO) {
            String[] split = parametersRowLogSplit[1].split("(\\(.*?\\))?,\\s");
            Matcher matcher = PATTERN_SEPARATE.matcher(parametersRowLogSplit[1]);
            List<String> params = new ArrayList();
            String[] var4 = split;
            int var5 = split.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String item = var4[var6];
                String group = "";
                if (matcher.find()) {
                    group = matcher.group().replaceAll(",", "").trim();
                }

                String s = item.trim();
                String[] parametersTypeOrValue = getParametersValueAndType(s + group);
                String typeFormat = quotationTypeFormat(parametersTypeOrValue);
                params.add(typeFormat);
            }

            return params.toArray(new String[0]);
        } else {
            return new String[0];
        }
    }

    private static String quotationTypeFormat(String[] parametersTypeOrValue) {
        if (EnumUtils.isValidEnumIgnoreCase(JavaTypeEnum.class, parametersTypeOrValue[1])) {
            JavaTypeEnum javaTypeEnum = (JavaTypeEnum)EnumUtils.getEnumIgnoreCase(JavaTypeEnum.class, parametersTypeOrValue[1]);
            return SHOW_LITERAL && javaTypeEnum.isShowLiteral() ? SqlUtil.format("{}'{}'", new Object[]{javaTypeEnum.getLiteralName(), parametersTypeOrValue[0]}) : SqlUtil.format("'{}'", new Object[]{parametersTypeOrValue[0]});
        } else {
            return parametersTypeOrValue[0];
        }
    }
    public static String format(String strPattern, Object... argArray) {
        return formatWith(strPattern, "{}", argArray);
    }
    private static String[] getParametersValueAndType(String s) {
        if (s.endsWith(")")) {
            char[] value = s.toCharArray();
            int find = -1;

            for(int j = value.length - 1; j >= 0; --j) {
                if (value[j] == '(') {
                    find = j;
                    break;
                }
            }

            if (find >= 0) {
                String val = s.substring(0, find);
                String type = s.substring(find + 1, value.length - 1);
                return new String[]{val, type};
            }
        }

        return new String[]{s, ""};
    }

    public static void printlnSqlByRowLog(Project project, String currentRowLog, ConsoleViewContentType consoleViewContentType) {
        ConsoleView consoleView = (ConsoleView)ConsolePrintlnUtil.consoleViewMap.get(project.getBasePath());
        printlnSqlByRowLog(project,consoleView, currentRowLog, consoleViewContentType);
    }

    public static void printlnSqlByRowLog(ConsoleView consoleView, String currentRowLog, ConsoleViewContentType consoleViewContentType) {
        String preparing = ConfigUtil.getPreparingKeyWord();
        String parameters = ConfigUtil.getParametersKeyWord();
        if (currentRowLog.contains(preparing)) {
            preparingLine = currentRowLog;
            collectSqlInfo(currentRowLog, "Preparing:");
        }

        if (currentRowLog.contains(parameters)) {
            parametersLine = currentRowLog;
            collectSqlInfo(currentRowLog, "Parameters:");
        }

        if (ObjectUtils.isEmpty(SQL_LOG_INFO_MAP)) {
            if (!StringUtils.isBlank(preparingLine) && !StringUtils.isBlank(parametersLine)) {
                String[] restoreSql = recoverSql(preparingLine, parametersLine);
                ConsolePrintlnUtil.println(consoleView, "# BEGIN ===========================================================================================================================================", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                ConsolePrintlnUtil.println(consoleView, "# " + restoreSql[0], ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                printlnSql(consoleView, restoreSql[1], consoleViewContentType);
                ConsolePrintlnUtil.println(consoleView, "# END =============================================================================================================================================\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                preparingLine = "";
                parametersLine = "";
            }
        } else {
            Iterator<Map.Entry<String, SqlLogInfo>> iterator = SQL_LOG_INFO_MAP.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, SqlLogInfo> entry = (Map.Entry)iterator.next();
                SqlLogInfo sqlLogInfo = (SqlLogInfo)entry.getValue();
                if (ObjectUtils.isNotEmpty(sqlLogInfo) && StringUtils.isNotBlank(sqlLogInfo.getPreparingRowLog()) && StringUtils.isNotBlank(sqlLogInfo.getParametersRowLog())) {
                    String[] restoreSql = recoverSql(sqlLogInfo.getPreparingRowLog(), sqlLogInfo.getParametersRowLog());
                    ConsolePrintlnUtil.println(consoleView, "# BEGIN ===========================================================================================================================================", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    ConsolePrintlnUtil.println(consoleView, "# " + restoreSql[0], ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    printlnSql(consoleView, restoreSql[1], consoleViewContentType);
                    ConsolePrintlnUtil.println(consoleView, "# END =============================================================================================================================================\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    iterator.remove();
                    preparingLine = "";
                    parametersLine = "";
                }
            }

        }
    }
    public static void printlnSqlByRowLog(Project project,ConsoleView consoleView, String currentRowLog, ConsoleViewContentType consoleViewContentType) {
        final MyBatisLogManager manager = MyBatisLogManager.getInstance(project);
        String parameters = manager.getParameters();
        String preparing = manager.getPreparing();
        String parametersTypes = manager.getParametersType();
//        String preparing = ConfigUtil.getPreparingKeyWord();
//        String parameters = ConfigUtil.getParametersKeyWord();
        if (currentRowLog.contains(preparing)) {
            preparingLine = currentRowLog;
            collectSqlInfo(currentRowLog, "Preparing:");
        }

        if (currentRowLog.contains(parameters)) {
            parametersLine = currentRowLog;
            collectSqlInfo(currentRowLog, "Parameters:");
        }

        if (currentRowLog.contains(parametersTypes)){
            parametersTypesLine = currentRowLog;
            collectSqlInfo(currentRowLog, "ParametersType:");
        }
        if (ObjectUtils.isEmpty(SQL_LOG_INFO_MAP)) {
            if (!StringUtils.isBlank(preparingLine) && !StringUtils.isBlank(parametersLine)) {
                String[] restoreSql = recoverSql(preparingLine, parametersLine);
                ConsolePrintlnUtil.println(consoleView, "# BEGIN ===========================================================================================================================================", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                ConsolePrintlnUtil.println(consoleView, "# " + restoreSql[0], ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                printlnSql(consoleView, restoreSql[1], consoleViewContentType);
                ConsolePrintlnUtil.println(consoleView, "# END =============================================================================================================================================\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                preparingLine = "";
                parametersLine = "";
            }
        } else {
            Iterator<Map.Entry<String, SqlLogInfo>> iterator = SQL_LOG_INFO_MAP.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, SqlLogInfo> entry = (Map.Entry)iterator.next();
                SqlLogInfo sqlLogInfo = (SqlLogInfo)entry.getValue();
                if (ObjectUtils.isNotEmpty(sqlLogInfo) && StringUtils.isNotBlank(sqlLogInfo.getPreparingRowLog()) && StringUtils.isNotBlank(sqlLogInfo.getParametersRowLog())) {
                    String[] restoreSql = recoverSql(sqlLogInfo.getPreparingRowLog(), sqlLogInfo.getParametersRowLog());
                    ConsolePrintlnUtil.println(consoleView, "# BEGIN ===========================================================================================================================================", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    ConsolePrintlnUtil.println(consoleView, "# " + restoreSql[0], ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    printlnSql(consoleView, restoreSql[1], consoleViewContentType);
                    ConsolePrintlnUtil.println(consoleView, "# END =============================================================================================================================================\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT, false);
                    iterator.remove();
                    preparingLine = "";
                    parametersLine = "";
                }
            }

        }
    }

    public static void printlnSql(Project project, String sql, ConsoleViewContentType contentType) {
        ConsoleView consoleView = (ConsoleView)ConsolePrintlnUtil.consoleViewMap.get(project.getBasePath());
        printlnSql(consoleView, sql, contentType);
    }

    public static void printlnSql(ConsoleView consoleView, String sql, ConsoleViewContentType contentType) {
        String sqlType = getSqlType(sql);
        if (ObjectUtils.isNotEmpty(contentType)) {
            ConsolePrintlnUtil.println(consoleView, sql, contentType, false);
        } else {
            switch (sqlType) {
                case "select":
                    ConsolePrintlnUtil.println(consoleView, sql, getViewContentType("select"), false);
                    break;
                case "insert":
                    ConsolePrintlnUtil.println(consoleView, sql, getViewContentType("insert"), false);
                    break;
                case "update":
                    ConsolePrintlnUtil.println(consoleView, sql, getViewContentType("update"), false);
                    break;
                case "delete":
                    ConsolePrintlnUtil.println(consoleView, sql, getViewContentType("delete"), false);
                    break;
                default:
                    ConsolePrintlnUtil.println(consoleView, sql, ConsoleViewContentType.LOG_ERROR_OUTPUT, false);
            }

        }
    }

    private static void collectSqlInfo(String currentRowLog, String type) {
        String mapperId = org.apache.commons.lang3.StringUtils.substringBetween(currentRowLog, ConfigUtil.getMapperIdPrefix(), ConfigUtil.getMapperIdSuffix());
        if (!StringUtils.isBlank(mapperId)) {
            mapperId = mapperId.trim();
            SqlLogInfo sqlLogInfo = (SqlLogInfo)SQL_LOG_INFO_MAP.get(mapperId);
            switch (type) {
                case "Preparing:":
                    if (ObjectUtils.isEmpty(sqlLogInfo)) {
                        sqlLogInfo = SqlLogInfo.builder().preparingRowLog(currentRowLog).build();
                    } else {
                        sqlLogInfo.setPreparingRowLog(currentRowLog);
                    }

                    SQL_LOG_INFO_MAP.put(mapperId, sqlLogInfo);
                    break;
                case "Parameters:":
                    if (ObjectUtils.isEmpty(sqlLogInfo)) {
                        sqlLogInfo = SqlLogInfo.builder().parametersRowLog(currentRowLog).build();
                    } else {
                        sqlLogInfo.setParametersRowLog(currentRowLog);
                    }

                    SQL_LOG_INFO_MAP.put(mapperId, sqlLogInfo);
                    break;
                case "ParametersTypes:":
                    if (ObjectUtils.isEmpty(sqlLogInfo)) {
                        sqlLogInfo = SqlLogInfo.builder().parametersTypesRowLog(currentRowLog).build();
                    } else {
                        sqlLogInfo.setParametersTypesRowLog(currentRowLog);
                    }

                    SQL_LOG_INFO_MAP.put(mapperId, sqlLogInfo);
                    break;
            }

        }
    }

    private static ConsoleViewContentType getViewContentType(String sqlType) {
        if (StringUtils.isBlank(sqlType)) {
            return ConsoleViewContentType.LOG_INFO_OUTPUT;
        } else {
            switch (sqlType) {
                case "select":
                    if (ObjectUtils.isEmpty(ConfigUtil.getSelectColorRgb())) {
                        return ConsoleViewContentType.LOG_INFO_OUTPUT;
                    }

                    return new ConsoleViewContentType("SELECT_SQL_COLOR_RGB", new TextAttributes(getSqlColor("select"), (Color)null, (Color)null, (EffectType)null, 0));
                case "insert":
                    if (ObjectUtils.isEmpty(ConfigUtil.getInsertColorRgb())) {
                        return ConsoleViewContentType.LOG_INFO_OUTPUT;
                    }

                    return new ConsoleViewContentType("INSERT_SQL_COLOR_RGB", new TextAttributes(getSqlColor("insert"), (Color)null, (Color)null, (EffectType)null, 0));
                case "update":
                    if (ObjectUtils.isEmpty(ConfigUtil.getUpdateColorRgb())) {
                        return ConsoleViewContentType.LOG_INFO_OUTPUT;
                    }

                    return new ConsoleViewContentType("UPDATE_SQL_COLOR_RGB", new TextAttributes(getSqlColor("update"), (Color)null, (Color)null, (EffectType)null, 0));
                case "delete":
                    if (ObjectUtils.isEmpty(ConfigUtil.getDeleteColorRgb())) {
                        return ConsoleViewContentType.LOG_INFO_OUTPUT;
                    }

                    return new ConsoleViewContentType("DELETE_SQL_COLOR_RGB", new TextAttributes(getSqlColor("delete"), (Color)null, (Color)null, (EffectType)null, 0));
                default:
                    return ConsoleViewContentType.LOG_INFO_OUTPUT;
            }
        }
    }

    public static Color getSqlColor(String sqlType) {
        if (StringUtils.isBlank(sqlType)) {
            return new JBColor(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor(), ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor());
        } else {
            switch (sqlType) {
                case "select":
                    return new JBColor(Color.decode(ConfigUtil.getSelectColorRgb()), Color.decode(ConfigUtil.getSelectColorRgb()));
                case "insert":
                    return new JBColor(Color.decode(ConfigUtil.getInsertColorRgb()), Color.decode(ConfigUtil.getInsertColorRgb()));
                case "update":
                    return new JBColor(Color.decode(ConfigUtil.getUpdateColorRgb()), Color.decode(ConfigUtil.getUpdateColorRgb()));
                case "delete":
                    return new JBColor(Color.decode(ConfigUtil.getDeleteColorRgb()), Color.decode(ConfigUtil.getDeleteColorRgb()));
                default:
                    return new JBColor(ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor(), ConsoleViewContentType.LOG_INFO_OUTPUT.getAttributes().getForegroundColor());
            }
        }
    }

    public static void setSqlColorRgb(String value, String defaultValue, @NotNull String sqlType) {
        switch (sqlType) {
            case "select":
                ConfigUtil.setSelectColorRgb(value, defaultValue);
                break;
            case "insert":
                ConfigUtil.setInsertColorRgb(value, defaultValue);
                break;
            case "update":
                ConfigUtil.setUpdateColorRgb(value, defaultValue);
                break;
            case "delete":
                ConfigUtil.setDeleteColorRgb(value, defaultValue);
        }

    }
}
