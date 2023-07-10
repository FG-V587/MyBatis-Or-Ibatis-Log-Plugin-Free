package com.mt.mybatislog.util;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsolePrintlnUtil {
    public static Map<String, ConsoleView> consoleViewMap = new ConcurrentHashMap();

    public ConsolePrintlnUtil() {
    }

    public static void setConsoleView(Project project, ConsoleView consoleView) {
        consoleViewMap.put(project.getBasePath(), consoleView);
    }

    public static void println(Project project, String rowLine, ConsoleViewContentType consoleViewContentType) {
        println(project, rowLine, consoleViewContentType, true, true);
    }

    public static void println(ConsoleView consoleView, String rowLine, ConsoleViewContentType consoleViewContentType) {
        println(consoleView, rowLine, consoleViewContentType, true, true);
    }

    public static void println(Project project, String rowLine, ConsoleViewContentType consoleViewContentType, boolean splitLine) {
        println(project, rowLine, consoleViewContentType, true, splitLine);
    }

    public static void println(ConsoleView consoleView, String rowLine, ConsoleViewContentType consoleViewContentType, boolean splitLine) {
        println(consoleView, rowLine, consoleViewContentType, true, splitLine);
    }

    public static void println(Project project, String rowLine, ConsoleViewContentType consoleViewContentType, boolean lineBreak, boolean splitLine) {
        ConsoleView consoleView = (ConsoleView)consoleViewMap.get(project.getBasePath());
        println(consoleView, rowLine, consoleViewContentType, lineBreak, splitLine);
    }

    public static void println(ConsoleView consoleView, String rowLine, ConsoleViewContentType consoleViewContentType, boolean lineBreak, boolean splitLine) {
        if (!ObjectUtils.isEmpty(consoleView)) {
            if (splitLine) {
                consoleView.print("# BEGIN ===========================================================================================================================================\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
            }

            if (lineBreak) {
                consoleView.print(rowLine + "\n", consoleViewContentType);
            } else {
                consoleView.print(rowLine, consoleViewContentType);
            }

            if (splitLine) {
                consoleView.print("# END =============================================================================================================================================\n\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
            }

        }
    }
}
