package com.oleynik.qa.reproducer.suite;

import org.testng.annotations.Test;

public class ExcludedTest {

    @Test
    public void excluded_by_testng_xml_but_discovered_when_xml_is_ignored() {
        System.out.println("EXCLUDED_TEST_RAN");
    }
}

