package com.oleynik.qa.reproducer.features;

import org.testng.annotations.Test;

public class GroupsCheck {

    @Test(groups = "smoke")
    public void smoke_test() {
        System.out.println("SMOKE_TEST_RAN");
    }

    @Test(groups = "regression")
    public void regression_test() {
        System.out.println("REGRESSION_TEST_RAN");
    }
}
