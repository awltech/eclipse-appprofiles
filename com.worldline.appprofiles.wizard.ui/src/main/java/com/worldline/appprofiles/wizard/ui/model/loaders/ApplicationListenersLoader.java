package com.worldline.appprofiles.wizard.ui.model.loaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IGlobalConfigurator;

/**
 * This Loader retrieves and stores the implementations of profile and
 * application creation listeners
 * 
 * @since 1.0
 * @author mvanbesien
 */
public class ApplicationListenersLoader {

	// Constants used in extension point
	private static final String EXTENSION_POINT_NAME = "profilesListeners";

	private static final String CONFIGELT_PROFILE_NAME = "profileListener";

	private static final String CONFIGELT_MODULE_NAME = "moduleListener";

	private static final String ATTR_PROFILE_ID_NAME = "profileId";

	private static final String ATTR_PROFILE_IMPL_NAME = "implementation";

	private static final String ATTR_MODULE_ID_NAME = "moduleId";

	private static final String ATTR_MODULE_IMPL_NAME = "implementation";

	// Maps holding instances of profiles and modules listeners.
	private Map<String, Set<IGlobalConfigurator>> profileListeners = new HashMap<String, Set<IGlobalConfigurator>>();

	private Map<String, Set<IConfigurator>> moduleListeners = new HashMap<String, Set<IConfigurator>>();

	// Private Constructor
	private ApplicationListenersLoader() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID,
				EXTENSION_POINT_NAME);
		for (IExtension extension : extensionPoint.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				if (CONFIGELT_PROFILE_NAME.equals(configurationElement.getName())) {
					try {
						String profileId = ExtensionPointHelper.mandatoryArgumentValue(configurationElement,
								ATTR_PROFILE_ID_NAME);
						IGlobalConfigurator profileListenerImpl = ExtensionPointHelper.mandatoryExecutable(
								IGlobalConfigurator.class, configurationElement, ATTR_PROFILE_IMPL_NAME);
						insert(this.profileListeners, profileId, profileListenerImpl);
					} catch (NullArgumentException e) {
						Activator.logger
								.warning("A profile listener will not be referenced as the ID is undefined (Contributed by "
										+ extension.getContributor().getName() + ").");
					} catch (NullExecutableException e) {
						Activator.logger
								.warning("A profile listener will not be referenced as the Implementation is undefined (Contributed by "
										+ extension.getContributor().getName() + ").");
					}
				} else if (CONFIGELT_MODULE_NAME.equals(configurationElement.getName())) {
					try {
						String profileId = ExtensionPointHelper.mandatoryArgumentValue(configurationElement,
								ATTR_MODULE_ID_NAME);
						IConfigurator moduleListenerImpl = ExtensionPointHelper.mandatoryExecutable(
								IConfigurator.class, configurationElement, ATTR_MODULE_IMPL_NAME);
						insert(this.moduleListeners, profileId, moduleListenerImpl);
					} catch (NullArgumentException e) {
						Activator.logger
								.warning("A module listener will not be referenced as the ID is undefined (Contributed by "
										+ extension.getContributor().getName() + ").");
					} catch (NullExecutableException e) {
						Activator.logger
								.warning("A module listener will not be referenced as the Implementation is undefined (Contributed by "
										+ extension.getContributor().getName() + ").");
					}
				}
			}
		}
	}

	// Technical method that adds elements in maps of lists.
	private static <K, V> void insert(Map<K, Set<V>> map, K key, V value) {
		Set<V> set = map.get(key);
		if (set == null) {
			set = new HashSet<V>();
			map.put(key, set);
		}
		set.add(value);
	}

	// Private singleton holder
	private static class SingletonHolder {
		private static final ApplicationListenersLoader instance = new ApplicationListenersLoader();
	}

	/**
	 * Returns the Singleton instance
	 */
	public static final ApplicationListenersLoader getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Returns the list of listeners assigned to the module, which ID is passed
	 * as parameter.
	 * 
	 * @param moduleId
	 * @return
	 */
	public Set<IConfigurator> getModuleListeners(String moduleId) {
		if (this.moduleListeners.containsKey(moduleId)) {
			return Collections.unmodifiableSet(this.moduleListeners.get(moduleId));
		}
		return Collections.emptySet();
	}

	/**
	 * Returns the list of listeners assigned to the profile, which ID is passed
	 * as parameter.
	 * 
	 * @param moduleId
	 * @return
	 */
	public Set<IGlobalConfigurator> getProfileListeners(String profileId) {
		if (this.profileListeners.containsKey(profileId)) {
			return Collections.unmodifiableSet(this.profileListeners.get(profileId));
		}
		return Collections.emptySet();
	}

}
