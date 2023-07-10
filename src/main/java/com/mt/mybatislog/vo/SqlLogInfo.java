package com.mt.mybatislog.vo;

public class SqlLogInfo {
    private String logPrefix;
    private String preparingRowLog;
    private String parametersRowLog;
    private String parameterTypesRowLog;

    public String getLogPrefix() {
        return logPrefix;
    }

    public void setLogPrefix(String logPrefix) {
        this.logPrefix = logPrefix;
    }

    public static SqlLogInfoBuilder builder() {
        return new SqlLogInfoBuilder();
    }

    public String getPreparingRowLog() {
        return this.preparingRowLog;
    }

    public String getParametersRowLog() {
        return this.parametersRowLog;
    }

    public void setPreparingRowLog(String preparingRowLog) {
        this.preparingRowLog = preparingRowLog;
    }

    public String getParameterTypesRowLog() {
        return parameterTypesRowLog;
    }

    public void setParameterTypesRowLog(String parameterTypesRowLog) {
        this.parameterTypesRowLog = parameterTypesRowLog;
    }

    public void setParametersRowLog(String parametersRowLog) {
        this.parametersRowLog = parametersRowLog;
    }

    @Override
    public String toString() {
        return "SqlLogInfo{" +
                "preparingRowLog='" + preparingRowLog + '\'' +
                ", parametersRowLog='" + parametersRowLog + '\'' +
                '}';
    }

    public SqlLogInfo() {
    }

    public SqlLogInfo(String preparingRowLog, String parametersRowLog,String parameterTypesRowLog) {
        this.preparingRowLog = preparingRowLog;
        this.parametersRowLog = parametersRowLog;
        this.parameterTypesRowLog = parameterTypesRowLog;
    }

    public void setParametersTypesRowLog(String parameterTypesRowLog) {
        this.parameterTypesRowLog = parameterTypesRowLog;
    }

    public static class SqlLogInfoBuilder {
        private String preparingRowLog;
        private String parametersRowLog;
        private String parameterTypesRowLog;

        public SqlLogInfoBuilder() {
        }

        public SqlLogInfoBuilder preparingRowLog(String preparingRowLog) {
            this.preparingRowLog = preparingRowLog;
            return this;
        }

        public SqlLogInfoBuilder parametersRowLog(String parametersRowLog) {
            this.parametersRowLog = parametersRowLog;
            return this;
        }

        public SqlLogInfo build() {
            return new SqlLogInfo(this.preparingRowLog, this.parametersRowLog,this.parameterTypesRowLog);
        }


        public SqlLogInfoBuilder parametersTypesRowLog(String parameterTypesRowLog) {
            this.parameterTypesRowLog = parameterTypesRowLog;
            return this;
        }
    }
}
