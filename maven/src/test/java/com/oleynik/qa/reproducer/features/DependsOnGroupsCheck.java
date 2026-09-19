package com.oleynik.qa.reproducer.features;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependsOnGroupsCheck {

    private boolean prerequisiteCompleted;

    @Test(groups = "prerequisite-group")
    public void prerequisite() {
        prerequisiteCompleted = true;
    }

    @Test(dependsOnGroups = "prerequisite-group")
    public void dependent() {
        Assert.assertTrue(prerequisiteCompleted, "The prerequisite group must run first");
    }
}

