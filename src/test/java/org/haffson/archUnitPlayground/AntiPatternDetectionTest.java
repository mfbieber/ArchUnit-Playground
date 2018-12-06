package org.haffson.archUnitPlayground;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.conditions.ArchPredicates;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.conditions.ArchConditions.callConstructor;
import static com.tngtech.archunit.lang.conditions.ArchConditions.callMethod;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Checks for common anti-patterns that might lead to long-term problems that are hard to spot.
 */
public class AntiPatternDetectionTest {

    private final JavaClasses allClasses = new ClassFileImporter()
            .importPackages("org.haffson.archUnitPlayground");

    @Test
    public void doNotUseSystemExit() {
        noClasses().should(callMethod(System.class, "exit", Integer.TYPE))
            .because("the JVM should not be killed from anywhere in the code. You should implement procedures for a controlled shutdown.")
            .check(allClasses);
    }

    @Test
    public void isInterfaceTest() {
        JavaClasses commonClasses = new ClassFileImporter()
                .importPackages("..common..");

        JavaClasses haffsonClasses = new ClassFileImporter()
                .importPackages("..haffson..");

        noClasses().that().resideInAPackage("..haffson..")
                .should().implement(JavaClass.Predicates.resideInAnyPackage("..common..").and(ArchPredicates.are(JavaClass.Predicates.INTERFACES)))
        .check(haffsonClasses);
    }
}