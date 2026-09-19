package com.oleynik.qa.reproducer.features;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependsOnMethodsCheck {

    @Test
    public void prerequisite() {
        Assert.fail("Intentional failure: the dependent test must be skipped");
    }

    @Test(dependsOnMethods = "prerequisite")
    public void dependent() {
        Assert.fail("This method must not run after the prerequisite fails");
    }
}

