/*
 * generated by Xtext 2.15.0
 */
package de.upb.sede.dsl.web

import com.google.inject.Guice
import com.google.inject.Injector
import de.upb.sede.dsl.SecoRuntimeModule
import de.upb.sede.dsl.SecoStandaloneSetup
import de.upb.sede.dsl.ide.SecoIdeModule
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages in web applications.
 */
class SecoWebSetup extends SecoStandaloneSetup {
	
	override Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new SecoRuntimeModule, new SecoIdeModule, new SecoWebModule))
	}
	
}
