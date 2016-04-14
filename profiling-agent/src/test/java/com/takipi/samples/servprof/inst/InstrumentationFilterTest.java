package com.takipi.samples.servprof.inst;

import com.takipi.samples.servprof.Options;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class InstrumentationFilterTest {


    private static InstrumentationFilter instFilter;

    @BeforeClass
    public static void setUp() throws Exception {
        Options options = Options.parse("c=(),m=()");

        options.print();
        instFilter = new InstrumentationFilter(options.getClassPattern(), options.getMethodPattern());

    }

    @Test
    public void testShouldInstrumentClass_basic() throws Exception {

        List<String> classpaths = new ArrayList<>();

        classpaths.add("org/hibernate/engine/jdbc/internal");
        classpaths.add("ee/ttu/catering/rest/model/MenuComment");
        classpaths.add("com/mcbfinance/aio/selfservice/facade/Test1");

        for (String classpath : classpaths) {
            assertTrue(classpath, instFilter.shouldInstrumentClass(classpath));
        }
    }

    @Test
    public void testShouldInstrumentClass_shouldNotInstrument() throws Exception {

        List<String> classpaths = new ArrayList<>();
        classpaths.add("java/util/String");
        classpaths.add("java/lang/String");
        classpaths.add("javax/lang/String");
        classpaths.add("com/oracle/String");
        classpaths.add("com/sun/String");
        classpaths.add("jdk/String");
//        classpaths.add("org/springframework/data/repository/config");
//        classpaths.add("org/springframework/data/repository/config/RepositoryComponentProvider");
        classpaths.add("org/springframework/data/repository/RepositoryDefinition");
        classpaths.add("com/mysql/jdbc/StringUtils");
        classpaths.add("com/mysql/jdbc/Util");
        classpaths.add("org.springframework.orm.jpa.AbstractEntityManagerFactoryBean");
        classpaths.add("org/hibernate/engine/jdbc/internal/LogicalConnectionImpl");
        classpaths.add("com/mcbfinance/aio/selfservice/facade/RegistrationRejectedException");
        classpaths.add("com/mysql/jdbc/exceptions/jdbc4/CommunicationsException");
        classpaths.add("com.mcbfinance.aio.config.selfservice.bankauth.fi.TupasConfig");
        classpaths.add("com/mcbfinance/aio/service/calculators/APRCalculator");

        for (String classpath : classpaths) {
            assertFalse(classpath, instFilter.shouldInstrumentClass(classpath));
        }
    }

    @Test
    public void testShouldInstrumentMethod_basic() throws Exception {
        List<String> methodNames = new ArrayList<>();
        methodNames.add("toString");

        for (String methodName : methodNames) {
            assertTrue(methodName, instFilter.shouldInstrumentMethod(methodName));
        }
    }
}