package com.winstly.blog;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.winstly.blog");

    @Test
    void contentContext_shouldNotDependOnClassificationInfrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("com.winstly.blog.content..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.winstly.blog.classification.infrastructure..")
                .check(classes);
    }

    @Test
    void interactionContext_shouldNotDependOnContentPersistence() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("com.winstly.blog.interaction..")
                .and().resideOutsideOfPackage("com.winstly.blog.interaction.infrastructure.acl..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.winstly.blog.content.infrastructure.persistence..")
                .check(classes);
    }

    @Test
    void interactionContext_shouldNotDependOnClassificationInfrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("com.winstly.blog.interaction..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.winstly.blog.classification.infrastructure..")
                .check(classes);
    }
}
