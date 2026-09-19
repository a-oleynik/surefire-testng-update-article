# Contributor and agent guide

## Purpose

This repository is a reproducible compatibility experiment, not a general TestNG workshop. Keep every change small enough that the Maven and Gradle results remain directly comparable.

## Required verification

Run the commands documented in the root `README.md` before submitting a change. Compare the reported test counts and expected failures, not only the process exit code.

Do not make the legacy-provider, missing-parameter, or `dependsOnMethods` scenarios green by weakening their assertions: those checks are intentionally negative.

## Invariants

- Keep `maven/src/test/resources/testng.xml` and `gradle/src/test/resources/testng.xml` equivalent.
- Keep `SelectedTest` and `ExcludedTest` equivalent in the Maven and Gradle projects.
- Both selection tests must pass when executed. Selection is proven by report presence and test counts, not by an intentionally failing control test.
- Do not add an explicit `testng-engine` dependency to the main Maven project; automatic engine provisioning is one of the verified claims.
- Keep Maven and Gradle as independent builds. Do not turn the repository into a Maven multi-module or Gradle multi-project build.
- Update the expected-results table in `README.md` whenever a dependency version or scenario changes.
