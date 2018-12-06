package org.haffson.archUnitPlayground;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.haffson.archUnitPlayground.controller.SmartControllerIsAllowedToDoEverything;
import org.haffson.archUnitPlayground.persistence.SomeDataObject;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringAllDependencies;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;


public class ArchUnitRulesFromPlantUML {

    URL myDiagram = getClass().getClassLoader().getResource("haffsons-architecture.puml");

    private final JavaClasses allClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DontIncludeTests())
            .importPackages("org.haffson.archUnitPlayground");

    @Test
    public void classesShouldAdhereToPlantUmlDiagram() {
        classes().should(adhereToPlantUmlDiagram(myDiagram, consideringOnlyDependenciesInDiagram()))
                .check(allClasses);;
    }

    @Test
    public void someClassesAreAllowedToDoEverything() {
        classes().should(adhereToPlantUmlDiagram(myDiagram, consideringOnlyDependenciesInDiagram()).ignoreDependencies(SmartControllerIsAllowedToDoEverything.class, SomeDataObject.class))
                .check(allClasses);;
    }
}
