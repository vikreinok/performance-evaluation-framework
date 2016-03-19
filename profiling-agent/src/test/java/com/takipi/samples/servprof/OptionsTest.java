package com.takipi.samples.servprof;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class OptionsTest {

    @Before
    public void setUp() throws Exception {
//        Options options = new Options(null, null, null, null, 1000, null);

    }

    @Test
    public void testParse() throws Exception {

        String vmString = "-javaagent:C:\\Users\\Viktor.Reinok\\profiling-agent\\target\\profiling-agent-jar-with-dependencies.jar=c= (),m=(),r=100,o=log_%s.txt -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005";
    }
}