/*
 * generated by Xtext 2.15.0
 */
package de.upb.sede.dsl

import org.eclipse.xtext.conversion.IValueConverterService;
import de.upb.sede.dsl.converter.SecoTerminalConverters;
import org.eclipse.xtext.formatting2.IFormatter2

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class SecoRuntimeModule extends AbstractSecoRuntimeModule {
	override Class<? extends IValueConverterService> bindIValueConverterService() {
        return de.upb.sede.dsl.converter.SecoTerminalConverters;
    }
}
