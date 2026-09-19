package com.oleynik.qa.reproducer.features;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParametersCheck {

    @Parameters("browser")
    @Test
    public void reads_parameter_from_system_property(String browser) {
        Assert.assertEquals(browser, "chrome");
    }
}

