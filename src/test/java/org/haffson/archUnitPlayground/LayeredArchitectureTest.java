package org.haffson.archUnitPlayground;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class LayeredArchitectureTest {

    private final JavaClasses allClasses = new ClassFileImporter()
            .importPackages("org.haffson.archUnitPlayground");

    @Test
    public void controllersShouldNotAccessPersistence() {
        layeredArchitecture()
                .layer("Controller").definedBy("..controller..")
                .layer("Repository").definedBy("..repository..")
                .layer("Persistence").definedBy("..persistence..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Repository")
                .check(allClasses);
    }
}
