package com.worldline.appprofiles.wizard.ui.model.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.ProcessMessages;
import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationConfiguration;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.ChoiceEntriesCombination;
import com.worldline.appprofiles.wizard.ui.model.MandatoryConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.MavenRepositoryLocation;
import com.worldline.appprofiles.wizard.ui.model.ModuleLocation;
import com.worldline.appprofiles.wizard.ui.model.OptionalConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.SelectionConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IGlobalConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.ISelectionConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IValueConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IValueValidator;

/**
 * Implementation that reads extension point for Application Profile definition
 * 
 * @author mvanbesien
 * 
 */
public class ApplicationProfilesLoader {

	// All following constants correspond to keywords from the Extension Points

	private static final String PLUGINVERSION_VARIABLE = "${pluginVersion}";

	private static final String GLOBAL_CONFIGURATOR = "globalConfigurator";

	private static final String VARIABLE = "variable";

	private static final String VALIDATOR = "validator";

	private static final String PACK_SUFFIX = "packageSuffix";

	private static final String STATIC_CONFIGURATION_ENTRY = "mandatoryConfigurationEntry";

	private static final String TEXT_CONFIGURATION_ENTRY = "valueConfigurationEntry";

	private static final String CONFIGURATOR = "configurator";

	private static final String DEFAULT = "default";

	private static final String MESSAGE = "message";

	private static final String OPTIONAL_CONFIGURATION_ENTRY = "optionalConfigurationEntry";

	private static final String SELECTION_CONFIGURATION_ENTRY = "selectionConfigurationEntry";

	private static final String INCOMPATIBILITY = "incompatibility";

	private static final String MODULE_ID = "moduleId";

	private static final String REQUIREMENT = "requirement";

	private static final String REPOSITORY = "repository";

	private static final String VERSION = "version";

	private static final String ARTIFACT_ID = "artifactId";

	private static final String GROUP_ID = "groupId";

	private static final String MAVEN_LOCATION = "mavenLocation";

	private static final String INCOMPATIBILITIES = "incompatibilities";

	private static final String REQUIREMENTS = "requirements";

	private static final String LOCATION = "location";

	private static final String MANDATORY = "mandatory";

	private static final String CONFIGURATION_ID = "configurationId";

	private static final String CONFIGURATION_REFERENCE = "configurationReference";

	private static final String MODULE = "module";

	private static final String NAME = "name";

	private static final String KIND = "kind";

	private static final String ID = "id";

	private static final String PROFILE = "profile";

	private static final String CONFIGURATION = "configuration";

	private static final String PROFILES = "profiles";

	// End of the keywords defined in Extension Point

	/**
	 * List of the read application profiles. Only variable in the class that
	 * goes outside of it.
	 */
	private List<ApplicationProfile> applicationProfiles = new ArrayList<ApplicationProfile>();

	/*
	 * List of configurations sorted by name. Used as cache for matching them
	 * with modules
	 */
	private Map<String, ApplicationConfiguration> applicationConfigurations = new HashMap<String, ApplicationConfiguration>();

	/*
	 * List of modules sorted by name. Used as cache for matching them with
	 * their requirements & dependencies
	 */
	private Map<String, ApplicationModule> modulesCache = new HashMap<String, ApplicationModule>();

	/*
	 * Cache map, containing names of modules and their respective requirements
	 * (also using names)
	 */
	private Map<String, Set<String>> requirementReferencesCache = new HashMap<String, Set<String>>();

	/*
	 * Cache map, containing names of modules and their respective
	 * incompatibilities (also using names)
	 */
	private Map<String, Set<String>> incompatibilityReferencesCache = new HashMap<String, Set<String>>();

	/*
	 * Static class that holds singleton
	 */
	private static class SingletonHolder {
		static ApplicationProfilesLoader instance = new ApplicationProfilesLoader();
	}

	/**
	 * @return singleton instance of Profile Loader implementation
	 */
	public static ApplicationProfilesLoader getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Creates Application Profiles Loader instance. Triggers load of extension
	 * point process.
	 */
	private ApplicationProfilesLoader() {
		Activator.logger.info(ProcessMessages.LOAD_PROCESS_STARTED.value());
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID,
				PROFILES);
		// Read Profiles & extensions separately
		for (IExtension extension : extensionPoint.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				if (CONFIGURATION.equals(configurationElement.getName()))
					this.loadConfiguration(configurationElement);
			}
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				if (PROFILE.equals(configurationElement.getName()))
					try {
						this.loadProfile(configurationElement);
					} catch (InvalidRegistryObjectException e) {
						e.printStackTrace();
					} catch (BundleNotFoundException e) {
						Activator.logger.log(Level.SEVERE, e.getMessage(), e);
						Activator.getDefault().getLog()
								.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
					}
			}
		}
		// Match Incompatibilities
		for (String sourceId : incompatibilityReferencesCache.keySet()) {
			for (String destinationId : incompatibilityReferencesCache.get(sourceId)) {
				try {
					ApplicationModule sourceModule = modulesCache.get(sourceId);
					ApplicationModule destinationModule = modulesCache.get(destinationId);
					if (sourceModule == null)
						throw new ApplicationModuleNotFoundException(sourceId);

					if (destinationModule == null)
						throw new ApplicationModuleNotFoundException(destinationId);

					sourceModule.getIncompatibilities().add(destinationModule);
					Activator.logger.info(ProcessMessages.INCOMPATIBILITY_REMATCHED.value(sourceId, destinationId));
				} catch (ApplicationModuleNotFoundException e) {
					Activator.logger.log(Level.SEVERE,
							ProcessMessages.INCOMPATIBILITY_EXCEPTION.value(sourceId, destinationId), e);
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}

		}

		// Match Requirements
		for (String sourceId : requirementReferencesCache.keySet()) {
			for (String destinationId : requirementReferencesCache.get(sourceId)) {
				try {
					ApplicationModule sourceModule = modulesCache.get(sourceId);
					ApplicationModule destinationModule = modulesCache.get(destinationId);
					if (sourceModule == null)
						throw new ApplicationModuleNotFoundException(sourceId);

					if (destinationModule == null)
						throw new ApplicationModuleNotFoundException(destinationId);

					sourceModule.getRequirements().add(destinationModule);
					Activator.logger.info(ProcessMessages.REQUIREMENT_REMATCHED.value(sourceId, destinationId));
				} catch (ApplicationModuleNotFoundException e) {
					Activator.logger.log(Level.SEVERE,
							ProcessMessages.REQUIREMENT_EXCEPTION.value(sourceId, destinationId), e);
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}
		}

		Activator.logger.info("Extension Point load process finished.");
	}

	/**
	 * Loads the profile contained in the provided configuration element.
	 * 
	 * @param configurationElement
	 * @throws BundleNotFoundException
	 * @throws InvalidRegistryObjectException
	 */
	private void loadProfile(IConfigurationElement configurationElement) throws InvalidRegistryObjectException,
			BundleNotFoundException {

		try {
			// Getting information from extension point
			String profileId = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, ID);
			String profileKind = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, KIND);
			String profileName = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, NAME);
			String description = ExtensionPointHelper.optionalArgumentValue(configurationElement, "description");

			// Creating data & processing sub elements
			ApplicationProfile profile = new ApplicationProfile(profileId, profileName, profileKind);
			if (description != null)
				profile.setDescription(description);

			Activator.logger.info(ProcessMessages.PROFILE_CREATED.value(profileId, profileName));
			applicationProfiles.add(profile);
			for (IConfigurationElement childElement : configurationElement.getChildren(MODULE)) {
				try {
					ApplicationModule module = loadModule(childElement);

					module.setParentProfile(profile);
					profile.getModules().add(module);
				} catch (ApplicationConfigurationNotFoundException e) {
					Activator.logger.severe(ProcessMessages.MODULE_EXCEPTION.value(profile.getId()));
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}

			for (IConfigurationElement childElement : configurationElement.getChildren(CONFIGURATION_REFERENCE)) {
				try {
					loadConfigurationReferences(profile, childElement);
				} catch (ApplicationConfigurationNotFoundException e) {
					Activator.logger.severe(ProcessMessages.PROFILE_EXCEPTION.value(profileId));
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
				}
			}

			String documentationURL = ExtensionPointHelper.optionalArgumentValue(configurationElement,
					"documentationURL");
			if (documentationURL != null) {
				Activator.logger.info(ProcessMessages.PROFILE_DOCUMENTATION.value(documentationURL, profileId));
				profile.setDocumentationURL(documentationURL);
			}

			IGlobalConfigurator globalConfigurator = ExtensionPointHelper.optionalExecutable(IGlobalConfigurator.class,
					configurationElement, GLOBAL_CONFIGURATOR);
			if (globalConfigurator != null) {
				Activator.logger.info(ProcessMessages.GLOBAL_CONFIGURATOR.value(
						globalConfigurator.getClass().getName(), profileId));
				profile.setGlobalConfigurator(globalConfigurator);
			}

		} catch (NullArgumentException nae) {
			Activator.logger.severe(ProcessMessages.PROFILE_EXCEPTION2.value());
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, nae.getMessage(), nae));
		}

	}

	/**
	 * For a profile, loads all the configuration references as defined in
	 * Extension Point
	 * 
	 * @param profile
	 * @param childElement
	 * @throws ApplicationConfigurationNotFoundException
	 */
	private void loadConfigurationReferences(ApplicationProfile profile, IConfigurationElement childElement)
			throws ApplicationConfigurationNotFoundException {
		try {
			String configurationId = ExtensionPointHelper.mandatoryArgumentValue(childElement, CONFIGURATION_ID);
			ApplicationConfiguration configuration = this.applicationConfigurations.get(configurationId);

			if (configuration == null)
				throw new ApplicationConfigurationNotFoundException(profile.getId(), configurationId);

			profile.addConfiguration(configuration);
			Activator.logger.info(ProcessMessages.CONF_REFERENCE_CREATED.value(configurationId, profile.getId(),
					"Profile"));
		} catch (NullArgumentException nae) {
			Activator.logger.info(ProcessMessages.CONF_REFERENCE_EXCEPTION.value(profile.getId()));
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, nae.getMessage(), nae));
		}

	}

	/**
	 * For a profile, loads all the configuration references as defined in
	 * Extension Point
	 * 
	 * @param module
	 * @param childElement
	 * @throws ApplicationConfigurationNotFoundException
	 */
	private void loadConfigurationReferences(ApplicationModule module, IConfigurationElement childElement)
			throws ApplicationConfigurationNotFoundException {
		try {
			String configurationId = ExtensionPointHelper.mandatoryArgumentValue(childElement, CONFIGURATION_ID);
			ApplicationConfiguration configuration = this.applicationConfigurations.get(configurationId);

			if (configuration == null)
				throw new ApplicationConfigurationNotFoundException(module.getId(), configurationId);

			module.addConfiguration(configuration);
			Activator.logger.info(ProcessMessages.CONF_REFERENCE_CREATED.value(configurationId, module.getId(),
					"Module"));
		} catch (NullArgumentException nae) {
			Activator.logger.info(ProcessMessages.CONF_REFERENCE_EXCEPTION.value(module.getId()));
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, nae.getMessage(), nae));
		}

	}

	/**
	 * For a profile, loads its modules from the Extension Point
	 * 
	 * @param profile
	 * @param configurationElement
	 * @return
	 * @throws BundleNotFoundException
	 * @throws InvalidRegistryObjectException
	 */
	private ApplicationModule loadModule(IConfigurationElement configurationElement) throws NullArgumentException,
			ApplicationConfigurationNotFoundException, InvalidRegistryObjectException, BundleNotFoundException {
		// Getting information from extension point
		String moduleId = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, ID);
		String moduleName = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, NAME);
		boolean moduleMandatory = Boolean.parseBoolean(ExtensionPointHelper.optionalArgumentValue(configurationElement,
				MANDATORY));

		Activator.logger.info(ProcessMessages.MODULE_CREATED.value(moduleId));
		ModuleLocation location = null;
		for (int i = 0; i < configurationElement.getChildren(LOCATION).length && location == null; i++) {
			location = this.loadLocation(configurationElement.getChildren(LOCATION)[i]);
		}

		if (location == null) {
			throw new NullArgumentException(configurationElement.getName(), LOCATION);
		}

		String defaultSelected = ExtensionPointHelper.optionalArgumentValue(configurationElement, "defaultSelected");

		ApplicationModule module = new ApplicationModule(moduleId, moduleName, moduleMandatory, location,
				Boolean.parseBoolean(defaultSelected));

		Activator.logger.info(ProcessMessages.MODULE_CREATED2.value(moduleId));
		this.modulesCache.put(moduleId, module);

		String projectNamePrefix = ExtensionPointHelper.optionalArgumentValue(configurationElement, "prefix");
		if (projectNamePrefix != null) {
			Activator.logger.info(ProcessMessages.MODULE_PREFIX_STORED.value(projectNamePrefix, module.getId()));
			module.setProjectNamePrefix(projectNamePrefix);
		}

		String projectNameSuffix = ExtensionPointHelper.optionalArgumentValue(configurationElement, "suffix");
		if (projectNameSuffix != null) {
			Activator.logger.info(ProcessMessages.MODULE_SUFFIX_STORED.value(projectNameSuffix, module.getId()));
			module.setProjectNameSuffix(projectNameSuffix);
		}

		String packageNameSuffix = ExtensionPointHelper.optionalArgumentValue(configurationElement, PACK_SUFFIX);
		if (packageNameSuffix != null) {
			Activator.logger.info(ProcessMessages.PACK_SUFFIX_STORED.value(packageNameSuffix, module.getId()));
			module.setPackageNameSuffix(packageNameSuffix);
		}

		for (IConfigurationElement childElement : configurationElement.getChildren(REQUIREMENTS)) {
			this.preloadRequirements(module, childElement);
		}
		for (IConfigurationElement childElement : configurationElement.getChildren(INCOMPATIBILITIES)) {
			this.preloadIncompatibilities(module, childElement);
		}

		for (IConfigurationElement childElement : configurationElement.getChildren(CONFIGURATION_REFERENCE)) {
			this.loadConfigurationReferences(module, childElement);
		}

		for (IConfigurationElement childElement : configurationElement.getChildren(MODULE)) {
			ApplicationModule childModule = loadModule(childElement);

			childModule.setParentModule(module);
			module.getSubModules().add(childModule);
		}

		return module;
	}

	/**
	 * Loads Location from provided Configuration Element
	 * 
	 * @param configurationElement
	 * @return
	 * @throws NullArgumentException
	 * @throws BundleNotFoundException
	 */
	private ModuleLocation loadLocation(IConfigurationElement configurationElement) throws NullArgumentException,
			BundleNotFoundException {

		ModuleLocation moduleLocation = null;
		Map<String, String> moduleLocationProperties = new HashMap<String, String>();

		for (int i = 0; i < configurationElement.getChildren().length; i++) {
			IConfigurationElement childElement = configurationElement.getChildren()[i];

			if ("property".equals(childElement.getName())) {
				String key = ExtensionPointHelper.mandatoryArgumentValue(childElement, "key");
				String value = ExtensionPointHelper.optionalArgumentValue(childElement, "value");
				moduleLocationProperties.put(key, value);
			} else if (moduleLocation == null) {
				if (MAVEN_LOCATION.equals(childElement.getName())) {
					String groupId = ExtensionPointHelper.mandatoryArgumentValue(childElement, GROUP_ID);
					String artifactId = ExtensionPointHelper.mandatoryArgumentValue(childElement, ARTIFACT_ID);
					String version = ExtensionPointHelper.mandatoryArgumentValue(childElement, VERSION);
					String repository = ExtensionPointHelper.mandatoryArgumentValue(childElement, REPOSITORY);
					Activator.logger.info(ProcessMessages.LOCATION_LOADED.value());
					moduleLocation = new MavenRepositoryLocation(groupId, artifactId, this.extractVersion(version,
							configurationElement), repository);
				}
			}
		}

		if (moduleLocation != null) {
			moduleLocation.getProperties().putAll(moduleLocationProperties);
			return moduleLocation;
		}

		Activator.logger.severe(ProcessMessages.NO_LOCATION.value());
		return null;
	}

	/**
	 * Delegate method, to support the ${pluginVersion} property.
	 * 
	 * @param version
	 * @param configurationElement
	 * @return
	 * @throws BundleNotFoundException
	 */
	private String extractVersion(String version, IConfigurationElement configurationElement)
			throws BundleNotFoundException {
		if (PLUGINVERSION_VARIABLE.equals(version)) {
			String contributorName = configurationElement.getContributor().getName();
			Bundle contributorBundle = Platform.getBundle(contributorName);
			if (contributorBundle == null)
				throw new BundleNotFoundException(contributorName);

			Version bundleVersion = contributorBundle.getVersion();
			String builtMavenVersion = this.buildMavenVersion(bundleVersion);
			Activator.logger.info(ProcessMessages.VERSION_RESOLVED.value(contributorName, bundleVersion.toString(),
					builtMavenVersion));
			return builtMavenVersion;
		}
		return version;
	}

	/**
	 * Converts a bundle version into a maven version
	 * 
	 * @param bundleVersion
	 * @return
	 */
	private String buildMavenVersion(Version bundleVersion) {

		// Qualifier is null, we return the standard version
		if (bundleVersion.getQualifier() == null)
			return String.format("%d.%d.%d", bundleVersion.getMajor(), bundleVersion.getMinor(),
					bundleVersion.getMicro());

		// Qualifier is qualifier. We are in dev, hence we use snapshot
		if ("qualifier".equals(bundleVersion.getQualifier()))
			return String.format("%d.%d.%d-SNAPSHOT", bundleVersion.getMajor(), bundleVersion.getMinor(),
					bundleVersion.getMicro());

		// The qualifier contains the snapshot keyword. We use snapshot
		if (bundleVersion.getQualifier().toLowerCase().indexOf("snapshot") > -1)
			return String.format("%d.%d.%d-SNAPSHOT", bundleVersion.getMajor(), bundleVersion.getMinor(),
					bundleVersion.getMicro());

		// Last case: final equivalent version
		return String.format("%d.%d.%d-%s", bundleVersion.getMajor(), bundleVersion.getMinor(),
				bundleVersion.getMicro(), bundleVersion.getQualifier());
	}

	/**
	 * Preloads requirements. Here, we preload and not load, because we make the
	 * match between modules' names. A real match between the instances is done
	 * at the end of the process, when we are sure all the modules are loaded.
	 * 
	 * @param module
	 * @param configurationElement
	 */
	private void preloadRequirements(ApplicationModule module, IConfigurationElement configurationElement) {
		for (IConfigurationElement childElement : configurationElement.getChildren(REQUIREMENT)) {
			try {
				String moduleId = ExtensionPointHelper.mandatoryArgumentValue(childElement, MODULE_ID);
				Set<String> requirementReferences = requirementReferencesCache.get(module.getId());
				if (requirementReferences == null) {
					requirementReferences = new HashSet<String>();
					requirementReferencesCache.put(module.getId(), requirementReferences);
				}
				requirementReferences.add(moduleId);
				Activator.logger.info(ProcessMessages.REQUIREMENT_PRELOADED.value(module.getId(), moduleId));
			} catch (NullArgumentException e) {
				Activator.logger.severe(ProcessMessages.REQUIREMENT_PRELOAD_ERROR.value(module.getId()));
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
	}

	/**
	 * Preloads incompatibilities. Here, we preload and not load, because we
	 * make the match between modules' names. A real match between the instances
	 * is done at the end of the process, when we are sure all the modules are
	 * loaded.
	 * 
	 * @param module
	 * @param configurationElement
	 */
	private void preloadIncompatibilities(ApplicationModule module, IConfigurationElement configurationElement) {
		for (IConfigurationElement childElement : configurationElement.getChildren(INCOMPATIBILITY)) {
			try {
				String moduleId = ExtensionPointHelper.mandatoryArgumentValue(childElement, MODULE_ID);
				Set<String> incompatibilityReferences = incompatibilityReferencesCache.get(module.getId());
				if (incompatibilityReferences == null) {
					incompatibilityReferences = new HashSet<String>();
					incompatibilityReferencesCache.put(module.getId(), incompatibilityReferences);
				}
				incompatibilityReferences.add(moduleId);
				Activator.logger.info(ProcessMessages.INCOMPATIBILITY_PRELOADED.value(module.getId(), moduleId));
			} catch (NullArgumentException e) {
				Activator.logger.severe(ProcessMessages.INCOMPATIBILITY_PRELOAD_ERROR.value(module.getId()));
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}
	}

	/**
	 * Loads Configuration Element from Extension Point
	 * 
	 * @param configurationElement
	 */
	private void loadConfiguration(IConfigurationElement configurationElement) {
		try {
			String configurationName = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, NAME);
			String configurationVersion = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, VERSION);
			String configurationId = ExtensionPointHelper.mandatoryArgumentValue(configurationElement, ID);

			ApplicationConfiguration configuration = new ApplicationConfiguration(configurationId, configurationName,
					configurationVersion);
			Activator.logger.info(ProcessMessages.CONFIGURATION_CREATED.value(configurationId));
			this.applicationConfigurations.put(configurationId, configuration);

			for (IConfigurationElement childElement : configurationElement.getChildren()) {
				if (OPTIONAL_CONFIGURATION_ENTRY.equals(childElement.getName())) {
					String entryMessage = ExtensionPointHelper.mandatoryArgumentValue(childElement, MESSAGE);
					String entryVariable = ExtensionPointHelper.optionalArgumentValue(childElement, VARIABLE);
					boolean entryDefaultValue = Boolean.parseBoolean(ExtensionPointHelper.optionalArgumentValue(
							childElement, DEFAULT));
					String entryId = ExtensionPointHelper.mandatoryArgumentValue(childElement, ID);
					IConfigurator configurator = ExtensionPointHelper.optionalExecutable(IConfigurator.class,
							childElement, CONFIGURATOR);
					OptionalConfigurationEntry entry = new OptionalConfigurationEntry(entryId, entryMessage,
							configurator, entryDefaultValue);
					entry.setVariable(entryVariable);
					configuration.addConfigurationEntry(entry);
					Activator.logger.info(ProcessMessages.CHOICE_CONFENTRY_LOADED.value(configurator == null ? "<NONE>"
							: configurator.getClass().getSimpleName(), configurationId));
				} else if (SELECTION_CONFIGURATION_ENTRY.equals(childElement.getName())) {
					String entryMessage = ExtensionPointHelper.mandatoryArgumentValue(childElement, MESSAGE);
					String entryVariable = ExtensionPointHelper.optionalArgumentValue(childElement, VARIABLE);
					boolean entryDefaultValue = Boolean.parseBoolean(ExtensionPointHelper.optionalArgumentValue(
							childElement, DEFAULT));
					String entryId = ExtensionPointHelper.mandatoryArgumentValue(childElement, ID);
					ISelectionConfigurator configurator = ExtensionPointHelper.optionalExecutable(
							ISelectionConfigurator.class, childElement, CONFIGURATOR);
					SelectionConfigurationEntry entry = new SelectionConfigurationEntry(entryId, entryMessage,
							configurator, entryDefaultValue);
					entry.setVariable(entryVariable);
					configuration.addConfigurationEntry(entry);
					Activator.logger.info(ProcessMessages.CHOICE_CONFENTRY_LOADED.value(configurator == null ? "<NONE>"
							: configurator.getClass().getSimpleName(), configurationId));
				} else if (TEXT_CONFIGURATION_ENTRY.equals(childElement.getName())) {
					String entryId = ExtensionPointHelper.mandatoryArgumentValue(childElement, ID);
					String entryVariable = ExtensionPointHelper.optionalArgumentValue(childElement, VARIABLE);
					String entryMessage = ExtensionPointHelper.mandatoryArgumentValue(childElement, MESSAGE);
					String entryDefaultValue = ExtensionPointHelper.optionalArgumentValue(childElement, DEFAULT);
					IValueConfigurator configurator = ExtensionPointHelper.optionalExecutable(IValueConfigurator.class,
							childElement, CONFIGURATOR);
					IValueValidator validator = ExtensionPointHelper.optionalExecutable(IValueValidator.class,
							childElement, VALIDATOR);
					ValueConfigurationEntry entry = new ValueConfigurationEntry(entryId, entryMessage, configurator,
							entryDefaultValue, validator);
					entry.setVariable(entryVariable);
					configuration.addConfigurationEntry(entry);
					Activator.logger.info(ProcessMessages.VALUE_CONFENTRY_LOADED.value(configurator == null ? "<NONE>"
							: configurator.getClass().getSimpleName(), configurationId));
				} else if (STATIC_CONFIGURATION_ENTRY.equals(childElement.getName())) {
					String entryId = ExtensionPointHelper.mandatoryArgumentValue(childElement, ID);
					String entryVariable = ExtensionPointHelper.optionalArgumentValue(childElement, VARIABLE);
					IConfigurator configurator = ExtensionPointHelper.optionalExecutable(IConfigurator.class,
							childElement, CONFIGURATOR);
					MandatoryConfigurationEntry entry = new MandatoryConfigurationEntry(entryId, configurator);
					entry.setVariable(entryVariable);
					configuration.addConfigurationEntry(entry);
					Activator.logger
							.info(ProcessMessages.MANDATORY_CONFENTRY_LOADED.value(configurator == null ? "<NONE>"
									: configurator.getClass().getSimpleName(), configurationId));
				}
			}

			this.loadOptionalConfigurationEntries(configurationElement, configuration);

		} catch (NullArgumentException nae) {
			Activator.logger.severe(ProcessMessages.CONFIGURATION_EXCEPTION.value());
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, nae.getMessage(), nae));
		} catch (OptionalEntryNotFoundException e) {
			Activator.logger.severe(ProcessMessages.CONFIGURATION_EXCEPTION.value());
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}

	}

	protected void loadOptionalConfigurationEntries(IConfigurationElement configurationElement,
			ApplicationConfiguration configuration) throws NullArgumentException, OptionalEntryNotFoundException {
		for (IConfigurationElement childElement : configurationElement.getChildren()) {
			if ("optionalEntriesCombination".equals(childElement.getName())) {
				IConfigurator configurator = ExtensionPointHelper.optionalExecutable(IConfigurator.class, childElement,
						"configurator");
				String entryVariable = ExtensionPointHelper.optionalArgumentValue(childElement, VARIABLE);
				ChoiceEntriesCombination combination = new ChoiceEntriesCombination(configurator);
				combination.setVariable(entryVariable);
				for (IConfigurationElement referenceElement : childElement.getChildren("optionalEntryReference")) {
					String referenceId = ExtensionPointHelper.mandatoryArgumentValue(referenceElement, "entryId");
					AbstractConfigurationEntry configurationEntry = configuration.getConfigurationEntry(referenceId);
					if (configurationEntry instanceof OptionalConfigurationEntry) {
						combination.getOptionalConfigurationEntries().add(
								(OptionalConfigurationEntry) configurationEntry);
					} else
						throw new OptionalEntryNotFoundException(referenceId, configuration.getId());
				}
				configuration.getOptionalEntriesCombinations().add(combination);
				Activator.logger.info(ProcessMessages.OPTIONAL_ENTRIES_COMBINATION_CREATED.value(combination
						.getOptionalConfigurationEntries().size(), configurator == null ? "<NONE>" : configurator
						.getClass().getSimpleName()));
			}
		}
	}

	/**
	 * Returns application profiles
	 * 
	 * @return
	 */
	public List<ApplicationProfile> getApplicationProfiles() {
		return applicationProfiles;
	}

}
