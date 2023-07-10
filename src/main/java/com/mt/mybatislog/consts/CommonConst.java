//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mt.mybatislog.consts;

import java.util.regex.Pattern;

public final class CommonConst {
    public static final String PROJECT_ID = "MyBatisLogPlugin";
    public static final String DEFAULT_MAPPER_ID_PREFIX = "DEBUG";
    public static final String DEFAULT_MAPPER_ID_SUFFIX = "==>";
    public static final String DEFAULT_PREPARING_KEY_WORD = "Preparing:";
    public static final String DEFAULT_PARAMETERS_KEY_WORD = "Parameters:";
    public static final String DB_STARTUP_KEY = "MyBatisLogPlugin:start_up";
    public static final String DB_START_KEY = "MyBatisLogPlugin:start";
    public static final String DB_PRETTY_KEY = "MyBatisLogPlugin:pretty";
    public static final String DB_MAPPER_ID_PREFIX_KEY = "MyBatisLogPlugin:mapper_id_prefix";
    public static final String DB_MAPPER_ID_SUFFIX_KEY = "MyBatisLogPlugin:mapper_id_suffix";
    public static final String DB_PREPARING_KEY = "MyBatisLogPlugin:preparing";
    public static final String DB_PARAMETERS_KEY = "MyBatisLogPlugin:parameters";
    public static final String DB_SELECT_COLOR_RGB_KEY = "MyBatisLogPlugin:select_color_rgb";
    public static final String DB_INSERT_COLOR_RGB_KEY = "MyBatisLogPlugin:insert_color_rgb";
    public static final String DB_UPDATE_COLOR_RGB_KEY = "MyBatisLogPlugin:update_color_rgb";
    public static final String DB_DELETE_COLOR_RGB_KEY = "MyBatisLogPlugin:delete_color_rgb";
    public static final char C_PARENTHESES_LEFT = '(';
    public static final char C_PARENTHESES_right = ')';
    public static final String S_PARENTHESES_LEFT = "(";
    public static final String S_PARENTHESES_right = ")";
    public static final String SPACE = " ";
    public static final String SEMICOLON = ";";
    public static final String SQL_ANNOTATION = "# ";
    public static final String SPLIT_LINE_BEGIN_INDEX = "# [INDEX:{}] # BEGIN ===========================================================================================================================================";
    public static final String SPLIT_LINE_END_INDEX = "# [INDEX:{}] # END =============================================================================================================================================";
    public static final String SPLIT_LINE_BEGIN = "# BEGIN ===========================================================================================================================================";
    public static final String SPLIT_LINE_END = "# END =============================================================================================================================================";
    public static final String SELECT = "select";
    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String SELECT_SQL_COLOR_RGB = "";
    public static final String INSERT_SQL_COLOR_RGB = "";
    public static final String UPDATE_SQL_COLOR_RGB = "";
    public static final String DELETE_SQL_COLOR_RGB = "";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String TIMESTAMP = "timestamp";
    public static final Pattern COLOR_RGB_PATTERN = Pattern.compile("^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$");

    private CommonConst() {
    }
}
