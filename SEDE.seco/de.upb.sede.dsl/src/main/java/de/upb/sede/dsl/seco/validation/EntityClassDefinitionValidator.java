/**
 *
 * $Id$
 */
package de.upb.sede.dsl.seco.validation;

import de.upb.sede.dsl.seco.EntityCast;
import de.upb.sede.dsl.seco.EntityMethod;

import java.util.List;
import org.eclipse.emf.common.util.EList;

/**
 * A sample validator interface for
 * {@link de.upb.sede.dsl.seco.EntityClassDefinition}. This doesn't really do
 * anything, and it's not a real EMF artifact. It was generated by the
 * org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's
 * code generator can be extended. This can be disabled with -vmargs
 * -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface EntityClassDefinitionValidator {
	boolean validate();

	boolean validateQualifiedName(String value);

	boolean validateWrapper(boolean value);

	boolean validateWrappedEntity(String value);

	boolean validateExtension(boolean value);

	boolean validateBaseEntities(EList<String> value);

	boolean validateMethods(EList<EntityMethod> value);

	boolean validateCasts(EList<EntityCast> value);

	boolean validateRuntimeInfo(String value);

	boolean validateBaseEntities(List<String> value);

	boolean validateMethods(List<EntityMethod> value);

	boolean validateCasts(List<EntityCast> value);
}
