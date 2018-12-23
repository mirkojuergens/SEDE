/**
 *
 * $Id$
 */
package de.upb.sede.dsl.seco.validation;

import de.upb.sede.dsl.seco.FieldValue;

/**
 * A sample validator interface for {@link de.upb.sede.dsl.seco.Field}. This
 * doesn't really do anything, and it's not a real EMF artifact. It was
 * generated by the org.eclipse.emf.examples.generator.validator plug-in to
 * illustrate how EMF's code generator can be extended. This can be disabled
 * with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface FieldValidator {
	boolean validate();

	boolean validateName(String value);

	boolean validateDereference(boolean value);

	boolean validateMemeber(FieldValue value);
}
