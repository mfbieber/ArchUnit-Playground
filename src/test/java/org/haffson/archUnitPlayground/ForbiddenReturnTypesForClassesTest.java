package org.haffson.archUnitPlayground;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.HasReturnType;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.conditions.ArchPredicates;
import org.haffson.archUnitPlayground.repository.SomeRepository;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.tngtech.archunit.core.domain.JavaMember.Predicates.declaredIn;
import static com.tngtech.archunit.core.domain.properties.HasReturnType.Predicates.returnType;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.no;
import static org.haffson.archUnitPlayground.ForbiddenReturnTypesForClassesTest.MethodCondition.notReturn;
import static org.haffson.archUnitPlayground.ForbiddenReturnTypesForClassesTest.MethodTransformer.methods;


public class ForbiddenReturnTypesForClassesTest {

    private final JavaClasses allClasses = new ClassFileImporter()
        .importPackagesOf(ArchUnitPlaygroundApplication.class);

    @Nonnull
    private static DescribedPredicate<HasReturnType> booleanClass() {
        return  returnType(Boolean.class);
    }

    @Test
    public void doNotReturnBooleans() {
        no(methods())
                .that(are(declaredIn(SomeRepository.class)))
                .should(notReturn(booleanClass()))
                .check(allClasses);
    }

    /**
     * Extracts methods that are declared in a {@link JavaClass} and allows to
     * run checks on these methods.
     */
    static class MethodTransformer extends AbstractClassesTransformer<JavaMethod> {

        /**
         * Factory method that can be imported statically to get more readable
         * rule chains.
         *
         * Example:
         *
         *     no(methods()).that(...).should(...)
         *
         * @return The method transformer.
         */
        @Nonnull
        public static MethodTransformer methods() {
            return new MethodTransformer();
        }

        public MethodTransformer() {
            super("methods");
        }

        @Nonnull
        public Iterable<JavaMethod> doTransform(@Nonnull final JavaClasses classes) {
            return StreamSupport.stream(classes.spliterator(), false)
                .flatMap(javaClass -> javaClass.getMethods().stream())
                .collect(Collectors.toList());
        }
    }

    /**
     * Checks if a method definition fulfills a condition described by the given predicate.
     */
    static class MethodCondition extends ArchCondition<JavaMethod> {

        /**
         * A predicate that can handle a {@link JavaMethod} object.
         */
        @Nonnull
        private final DescribedPredicate<? super JavaMethod> predicate;

        /**
         * Factory method that can be imported statically to get more readable
         * rule chains.
         *
         * Example:
         *
         *      no(...).that(...).should(notReturn(...))
         *
         * @param predicate The predicate that is applied to the methods.
         * @return The condition object.
         */

        @Nonnull
        public static MethodCondition notReturn(final DescribedPredicate<? super JavaMethod> predicate) {
            return new MethodCondition(ArchPredicates.be(Objects.requireNonNull(predicate)));
        }

        public MethodCondition(@Nonnull final DescribedPredicate<? super JavaMethod> predicate) {
            super(Objects.requireNonNull(predicate).getDescription());
            this.predicate = predicate;
        }

        @Override
        public void check(@Nonnull final JavaMethod method, @Nonnull final ConditionEvents events) {
            events.add(new SimpleConditionEvent(method, predicate.apply(method), generateEventDescriptionFor(method)));
        }

        @Nonnull
        private String generateEventDescriptionFor(@Nonnull final JavaMethod method) {
            return method.getFullName() + " " + getDeclaringClass(method).getName() + ":" + determineLineNumberOfDeclaration(method);
        }

        /**
         * Determines the class that declares the given method.
         *
         * When the method is part of an anonymous class, the enclosing class is returned.
         */
        private JavaClass getDeclaringClass(@Nonnull final JavaMethod method) {
            JavaClass declaringClass = method.getOwner();
            while (declaringClass.getEnclosingClass().isPresent()) {
                declaringClass = declaringClass.getEnclosingClass().get();
            }
            return declaringClass;
        }

        /**
         * Tries to determine the line number of the method declaration.
         */
        private int determineLineNumberOfDeclaration(@Nonnull final JavaMethod method) {
            final int lineOfFirstStatement = Stream.of(
                    // Search for code calls within the method...
                    method.getMethodCallsFromSelf(),
                    method.getConstructorCallsFromSelf(),
                    method.getFieldAccesses()
                )
                .flatMap(Collection::stream)
                .mapToInt(JavaAccess::getLineNumber)
                // ... and find the first one.
                .min()
                .orElse(0);
            // The previous line should be the location of the declaration.
            return lineOfFirstStatement - 1;
        }
    }
}
