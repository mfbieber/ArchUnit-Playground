package org.haffson.archUnitPlayground;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.conditions.ArchConditions.callConstructor;
import static com.tngtech.archunit.lang.conditions.ArchConditions.callMethod;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Checks for common anti-patterns that might lead to long-term problems that are hard to spot.
 */
public class AntiPatternDetectionTest {

    private final JavaClasses allClasses = new ClassFileImporter()
        .importPackagesOf(ArchUnitPlaygroundApplication.class);

    @Test
    public void doNotUseSystemExit() {
        noClasses().should(callMethod(System.class, "exit", Integer.TYPE))
            .because("the JVM should not be killed from anywhere in the code. You should implement procedures for a controlled shutdown.")
            .check(allClasses);
    }
}