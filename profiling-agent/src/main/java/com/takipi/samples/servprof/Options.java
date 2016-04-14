package com.takipi.samples.servprof;

import java.util.HashMap;
import java.util.Map;

public class Options {
    private static final String OUTPUT_FILE_FORMAT_KEY = "o";
    private static final String REPORT_INTERVAL_KEY = "r";
    private static final String CLASS_PATTERN_KEY = "c";
    private static final String METHOD_PATTERN_KEY = "m";
    private static final String VM_PROCESS_ID_KEY = "p";

    private final String outputFileName;
    private final String classPattern;
    private final String methodPattern;
    private final long reportIntervalMillis;
    private final String vmProcessId;

    public static Options parse(String optionsStr) {
        Map<String, String> optionMap = toMap(optionsStr);

        String outputFileFormat = readString(optionMap, OUTPUT_FILE_FORMAT_KEY, "%S");
        String outputFileName = String.format(outputFileFormat, Long.toString(System.currentTimeMillis()));

        String classPattern = readString(optionMap, CLASS_PATTERN_KEY, "");
        String methodPattern = readString(optionMap, METHOD_PATTERN_KEY, "");
        long reportIntervalMillis = readLong(optionMap, REPORT_INTERVAL_KEY, 1000);
        String vmProcessId = readString(optionMap, VM_PROCESS_ID_KEY, "");

//		-javaagent:C:\Users\Viktor.Reinok\profiling-agent\target\profiling-agent-jar-with-dependencies.jar=c=
//				(||||com.mysql.jdbc.(\w)),m=( ),r=100,o=log_%s.txt
//				-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005

//        String extraClassPattern = "";
//        extraClassPattern += "|";
//        extraClassPattern += "org.springframework.data.jpa.repository.query.AbstractJpaQuery";
//        extraClassPattern += "|";
//        extraClassPattern += "org.springframework.data.jpa.repository.query.JpaQueryExecution";
//        extraClassPattern += "|";
//        extraClassPattern += "org.springframework.data.jpa.repository.query.PartTreeJpaQuery";
//        extraClassPattern += "|";
//        extraClassPattern += "org.springframework.jdbc.core.JdbcTemplate";
//        extraClassPattern += "|";
//        extraClassPattern += "ee.ttu.catering.rest.service.*";
//        extraClassPattern += "|";
//        extraClassPattern += "com.mysql.jdbc.PreparedStatement";
//        extraClassPattern += "|";
//        extraClassPattern += "org.hibernate.engine.jdbc.internal.StatementPreparerImpl";
//        extraClassPattern += "|";
//        extraClassPattern += "com.mysql.jdbc.ConnectionImpl";
//        extraClassPattern += "|";
//        extraClassPattern += "org.springframework.data.repository.Repository";
//        extraClassPattern += "|";

        String extraClassPattern = "";
        extraClassPattern += "(";
       /* extraClassPattern += "";
        extraClassPattern += "";
        extraClassPattern += "org.springframework.data.jpa.repository.query.*|";
        extraClassPattern += "org.springframework.data.repository.*";
        extraClassPattern += "org.springframework.jdbc.core.*|";
        extraClassPattern += "((?!com.mcbfinance.aio.selfservice.facade.RegistrationRejectedException|" +
                "com.mcbfinance.aio.config.selfservice.bankauth.fi.TupasConfig|" +
                "com.mcbfinance.aio.service.calculators.APRCalculator|" +
                "com.mcbfinance.aio.service.mmp.MMPServiceImpl|" +
                "com.mcbfinance.aio.repository.SalesForceInfoRepositoryImpl|" +
                "com.mcbfinance.aio.service.worapay.WoraPayServiceImpl|" +
                "com.mcbfinance.aio.util.external.PersonGenerator|" +
                "com.mcbfinance.aio.external.lv.creditreform.DummyCreditReformClient|" +
                ")(com.mcbfinance.aio.*))|";
//        extraClassPattern += "((?!com.mysql.jdbc.StringUtils|com/mysql/jdbc/Util|com.mysql.jdbc.exceptions.jdbc4.CommunicationsException|com.mysql.jdbc.exceptions.*)(com.mysql.jdbc.*))|";
        extraClassPattern += "((?!org.hibernate.engine.jdbc.internal.LogicalConnectionImpl)(org.hibernate.engine.jdbc.internal.*))|";
*/
        extraClassPattern += "com.takipi.samples.servprof.main.*|";
        extraClassPattern += "com.takipi.samples.servprof.main.*|";
//        org.apache.tomcat.jdbc.pool.DataSource, org.apache.tomcat.dbcp.dbcp.PoolingDataSource, org.apache.commons.dbcp2.PoolingDataSource, org.apache.commons.dbcp.PoolingDataSource, com.mchange.v2.c3p0.AbstractPoolBackedDataSource, com.mchange.v2.c3p0.PoolBackedDataSource, com.mchange.v2.c3p0.ComboPooledDataSource, com.jolbox.bonecp.BoneCPDataSource, snaq.db.DBPoolDataSource, com.zaxxer.hikari.HikariDataSource, org.jboss.jca.adapters.jdbc.WrapperDataSource, org.springframework.jdbc.datasource.SingleConnectionDataSource, org.springframework.jdbc.datasource.DriverManagerDataSource, org.springframework.jdbc.datasource.SimpleDriverDataSource
        extraClassPattern += "ee.ttu.catering.rest.service.*|";
        extraClassPattern += "ee.ttu.catering.rest.*|";

        extraClassPattern += "(?!";
        extraClassPattern += "org.springframework.data.repository.config.*|";
        extraClassPattern += ")";
        extraClassPattern += ")";





        StringBuilder sb = new StringBuilder(extraClassPattern);
        classPattern = sb.insert(classPattern.lastIndexOf(")"), extraClassPattern).toString();

        extraClassPattern.replace(")", extraClassPattern + ")");

        String extraMethodPattern = "|";
        extraMethodPattern += "execute";
        extraMethodPattern += "|";
        extraMethodPattern += "doExecute";
        extraMethodPattern += "|";
        extraMethodPattern += "applyLockMode";
        extraMethodPattern += "|";
        extraMethodPattern += "getAll";
        extraMethodPattern += "|";
        extraMethodPattern += "get";
        extraMethodPattern += "|";
        extraMethodPattern += "executeQuery";
        extraMethodPattern += "|";
        extraMethodPattern += "executeQueryStatement";
        extraMethodPattern += "|";
//        extraMethodPattern2 += "prepareQueryStatement";
//        extraMethodPattern2 += "|";
        extraMethodPattern += "PreparedStatement";
        extraMethodPattern += "|";
//        extraMethodPattern += "clientPrepareStatement";
//        extraMethodPattern += "|";
        extraMethodPattern += "prepareStatement";
        extraMethodPattern += "|";
        extraMethodPattern += "findBy";
        extraMethodPattern += "|";
        extraMethodPattern += "read";
        extraMethodPattern += "|";
        extraMethodPattern += "\\S*";
        extraMethodPattern += "|";



        String extraMethodPattern2 = "";
        extraMethodPattern2 += "(?!clientPrepareStatement|prepareQueryStatement|changeProduct)\\w*";
        extraMethodPattern2 += "";


        sb = new StringBuilder(methodPattern);
        methodPattern = sb.insert(methodPattern.lastIndexOf(")"), extraMethodPattern2).toString();

        return new Options(outputFileName, extraClassPattern, methodPattern, reportIntervalMillis, vmProcessId);
    }

    private static long readLong(Map<String, String> map, String key, long defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        Object value = map.get(key);

        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong((String) value);
    }

    private static String readString(Map<String, String> map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        Object value = map.get(key);

        if (value == null) {
            return defaultValue;
        }

        return (String) value;
    }

    private Options(String outputFilePrefix, String classPattern, String methodPattern, long reportIntervalMillis, String vmProcessId) {
        this.outputFileName = outputFilePrefix;
        this.classPattern = classPattern;
        this.methodPattern = methodPattern;
        this.reportIntervalMillis = reportIntervalMillis;
        this.vmProcessId = vmProcessId;
    }

    public String outputFileName() {
        return outputFileName;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public long getReportIntervalMillis() {
        return reportIntervalMillis;
    }

    private static Map<String, String> toMap(String optionsStr) {
        Map<String, String> optionMap = new HashMap<>();

        if (optionsStr == null) {
            return optionMap;
        }

        String[] pairs = optionsStr.split(",");

        for (String pair : pairs) {
            int splitIndex = pair.indexOf("=");

            String key = pair.substring(0, splitIndex);
            String value = pair.substring(splitIndex + 1);

            optionMap.put(key, value);
        }

        return optionMap;
    }

    public void print() {
        System.out.println("> Output file name: " + outputFileName);
        System.out.println("> Class Pattern:    " + classPattern);
        System.out.println("> Method Pattern:   " + methodPattern);
        System.out.println("> Report interval:  " + reportIntervalMillis + " ms");
    }

    public String getVmProcessId() {
        return vmProcessId;
    }
}
