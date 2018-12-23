/**
 *
 * $Id$
 */
package de.upb.sede.dsl.seco.validation;

import de.upb.sede.dsl.seco.TransformDirection;

/**
 * A sample validator interface for {@link de.upb.sede.dsl.seco.EntityCast}.
 * This doesn't really do anything, and it's not a real EMF artifact. It was
 * generated by the org.eclipse.emf.examples.generator.validator plug-in to
 * illustrate how EMF's code generator can be extended. This can be disabled
 * with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface EntityCastValidator {
	boolean validate();

	boolean validateDirection(TransformDirection value);

	boolean validateResultingEntity(String value);

	boolean validateAdditionalData(String value);
}
