package com.mt.mybatislog;

import java.util.*;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.util.PropertiesComponent;
import com.mt.mybatislog.vo.SqlLogInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.mt.mybatislog.gui.MyBatisLogManager;

/**
 * MyBatisLogConsoleFilter
 *
 * @author MT
 */
public class MyBatisLogConsoleFilter implements Filter {

    public static final String PREPARING_KEY = MyBatisLogConsoleFilter.class.getName() + ".Preparing";
    //q:下面的代码什么意思？
    public static final String PARAMETERS_KEY = MyBatisLogConsoleFilter.class.getName() + ".Parameters";
    public static final String PARAMETERS_TYPE_KEY = MyBatisLogConsoleFilter.class.getName() + ".Types";
    public static final String KEYWORDS_KEY = MyBatisLogConsoleFilter.class.getName() + ".Keywords";
    public static final String IsMyBatis = MyBatisLogConsoleFilter.class.getName() + ".OrmType";


    public static final String INSERT_SQL_COLOR_KEY = MyBatisLogConsoleFilter.class.getName() + ".InsertSQLColor";
    public static final String DELETE_SQL_COLOR_KEY = MyBatisLogConsoleFilter.class.getName() + ".DeleteSQLColor";
    public static final String UPDATE_SQL_COLOR_KEY = MyBatisLogConsoleFilter.class.getName() + ".UpdateSQLColor";
    public static final String SELECT_SQL_COLOR_KEY = MyBatisLogConsoleFilter.class.getName() + ".SelectSQLColor";

    private static final char MARK = '?';

    private static final Set<String> NEED_BRACKETS;

    private final Project project;
    private volatile SqlLogInfo sqlLogInfo = new SqlLogInfo();

    private String sql = null;
    private static MyBatisLogManager manager ;

    static {
        Set<String> types = new HashSet<>(8);
        types.add("String");
        types.add("Date");
        types.add("Time");
        types.add("LocalDate");
        types.add("LocalTime");
        types.add("LocalDateTime");
        types.add("BigDecimal");
        types.add("Timestamp");
        types.add("Boolean");
        types.add("Character");
        NEED_BRACKETS = Collections.unmodifiableSet(types);
    }

    MyBatisLogConsoleFilter(Project project) {
        this.project = project;
    }

    @Override
    public @Nullable Result applyFilter(@NotNull String line, int entireLength) {

        manager = MyBatisLogManager.getInstance(project);
        if (Objects.isNull(manager)) {
            return null;
        }

        if (!manager.isRunning()) {
            return null;
        }
//        SqlUtil.printlnSqlByRowLog(this.project, line, (ConsoleViewContentType)null);
        final String preparingKey = manager.getPreparing();
        final String parametersKey = manager.getParameters();
        final String parametersTypeKey = manager.getParametersType();
        final List<String> keywords = manager.getKeywords();
        boolean isMyBatis = manager.isMyBatis();
        if (CollectionUtils.isNotEmpty(keywords)) {
            for (String keyword : keywords) {
                if (line.contains(keyword)) {
                    sql = null;
                    return null;
                }
            }
        }
        if (isMyBatis){
            if (line.contains(preparingKey)) {
                sqlLogInfo.setLogPrefix(StringUtils.substringBefore(line, preparingKey));
                sqlLogInfo.setPreparingRowLog(StringUtils.substringAfter(line, preparingKey));
                return null;
            }
            if (line.contains(parametersKey)) {
                if (StringUtils.isBlank(sqlLogInfo.getPreparingRowLog())) {
                    return null;
                }
                sqlLogInfo.setParametersRowLog(StringUtils.substringAfter(line, parametersKey));
                return null;
            }
            if (StringUtils.isBlank(sqlLogInfo.getPreparingRowLog())||StringUtils.isBlank(sqlLogInfo.getParametersRowLog())) {
                return null;
            }

        }else{

            if (line.contains(preparingKey)) {
                String logPrefix = StringUtils.substringBefore(line, preparingKey);
                sqlLogInfo.setLogPrefix(logPrefix);
                sqlLogInfo.setPreparingRowLog(StringUtils.substringAfter(line, preparingKey));
                return null;
            }
            if (line.contains(parametersKey)) {
                if (StringUtils.isBlank(sqlLogInfo.getPreparingRowLog())) {
                    return null;
                }
                sqlLogInfo.setParametersRowLog(StringUtils.substringAfter(line, parametersKey));
                return null;
            }
            if (line.contains(parametersTypeKey)) {
                if (StringUtils.isBlank(sqlLogInfo.getPreparingRowLog())) {
                    return null;
                }
                if (StringUtils.isBlank(sqlLogInfo.getParametersRowLog())) {
                    return null;
                }
                sqlLogInfo.setParameterTypesRowLog(StringUtils.substringAfter(line, parametersTypeKey));
                return null;
            }

            if (StringUtils.isBlank(sqlLogInfo.getPreparingRowLog())||StringUtils.isBlank(sqlLogInfo.getParametersRowLog())||StringUtils.isBlank(sqlLogInfo.getParameterTypesRowLog())) {
                return null;
            }
        }


        // 解析参数,并将解析后的sql返回
        String wholeSql = parseSql(sqlLogInfo.getPreparingRowLog(),parseParams(sqlLogInfo.getParametersRowLog(), sqlLogInfo.getParameterTypesRowLog())).toString();
        // 去除 wholeSql 前面的空格
        wholeSql = wholeSql.trim();
        String key;
        if (StringUtils.startsWithIgnoreCase(wholeSql, "insert")) {
            key = INSERT_SQL_COLOR_KEY;
        } else if (StringUtils.startsWithIgnoreCase(wholeSql, "delete")) {
            key = DELETE_SQL_COLOR_KEY;
        } else if (StringUtils.startsWithIgnoreCase(wholeSql, "update")) {
            key = UPDATE_SQL_COLOR_KEY;
        } else if (StringUtils.startsWithIgnoreCase(wholeSql, "select")) {
            key = SELECT_SQL_COLOR_KEY;
        } else {
            key = "unknown";
        }
        manager.println(sqlLogInfo.getLogPrefix(), wholeSql, PropertiesComponent.getInstance(project).getInt(key, ConsoleViewContentType.ERROR_OUTPUT.getAttributes().getForegroundColor().getRGB()));
        this.sqlLogInfo = new SqlLogInfo();

        return null;
    }

    static StringBuilder parseSql(String sql, Queue<Map.Entry<String, String>> params) {

        final StringBuilder sb = new StringBuilder(sql);

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != MARK) {
                continue;
            }

            final Map.Entry<String, String> entry = params.poll();
            if (Objects.isNull(entry)) {
                continue;
            }


            sb.deleteCharAt(i);

            if (NEED_BRACKETS.contains(entry.getValue())) {
                sb.insert(i, String.format("'%s'", entry.getKey()));
            } else {
                sb.insert(i, entry.getKey());
            }


        }

        return sb;
    }

    static Queue<Map.Entry<String, String>> parseParams(String line, String parametersTypes) {
        line = StringUtils.removeEnd(line, "\n");
        if (StringUtils.isBlank(line)) {
            return new ArrayDeque<>(0);
        }
        boolean isMyBatis = manager.isMyBatis();
        Queue<Map.Entry<String, String>> queue = null;
        if (isMyBatis) {
            final String[] strings = StringUtils.splitByWholeSeparator(line, ", ");
             queue = new ArrayDeque<>(strings.length);
            for (String s : strings) {
                String value = StringUtils.substringBeforeLast(s, "(");
                String type = StringUtils.substringBetween(s, "(", ")");
                if (StringUtils.isEmpty(type)) {
                    queue.offer(new AbstractMap.SimpleEntry<>(value, null));
                } else {
                    queue.offer(new AbstractMap.SimpleEntry<>(value, type));
                }
            }
        } else {
            parametersTypes = StringUtils.deleteWhitespace(parametersTypes);
            parametersTypes = StringUtils.substringBetween(parametersTypes, "[", "]");
            line = StringUtils.substringBetween(line, "[", "]");
            parametersTypes = StringUtils.replace(parametersTypes, "java.lang.", "");
            parametersTypes = StringUtils.replace(parametersTypes, "java.sql.", "");
            parametersTypes = StringUtils.replace(parametersTypes, "java.math.", "");

            String[] parametersType = StringUtils.splitByWholeSeparator(parametersTypes, ",");

            String[] strings = StringUtils.splitByWholeSeparator(line, ",");

            queue = new ArrayDeque<>(strings.length);
            if (parametersType.length != strings.length) {
                return queue;
            }

            for (String value : strings) {
                if (StringUtils.startsWith(value, " ") && value.length() >= 2) {
                    value = StringUtils.substring(value, 1);
                }
                queue.offer(new AbstractMap.SimpleEntry<>(value, parametersType[queue.size()]));
            }
        }


        return queue;
    }

}
