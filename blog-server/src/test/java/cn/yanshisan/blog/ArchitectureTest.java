package cn.yanshisan.blog;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("cn.yanshisan.blog");

    @Test
    void contentContext_shouldNotDependOnClassificationInfrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("cn.yanshisan.blog.content..")
                .should().dependOnClassesThat()
                .resideInAPackage("cn.yanshisan.blog.classification.infrastructure..")
                .check(classes);
    }

    @Test
    void interactionContext_shouldNotDependOnContentPersistence() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("cn.yanshisan.blog.interaction..")
                .and().resideOutsideOfPackage("cn.yanshisan.blog.interaction.infrastructure.acl..")
                .should().dependOnClassesThat()
                .resideInAPackage("cn.yanshisan.blog.content.infrastructure.persistence..")
                .check(classes);
    }

    @Test
    void interactionContext_shouldNotDependOnClassificationInfrastructure() {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage("cn.yanshisan.blog.interaction..")
                .should().dependOnClassesThat()
                .resideInAPackage("cn.yanshisan.blog.classification.infrastructure..")
                .check(classes);
    }
}
