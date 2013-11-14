package com.worldline.appprofiles.wizard.ui.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.worldline.appprofiles.wizard.maven.Maven2ProjectBuilder;
import com.worldline.appprofiles.wizard.maven.Maven2ProjectCreator;
import com.worldline.appprofiles.wizard.maven.archetype.Archetype;
import com.worldline.appprofiles.wizard.maven.archetype.ArchetypeApplication;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.ProcessMessages;
import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationConfiguration;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.ChoiceConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ChoiceEntriesCombination;
import com.worldline.appprofiles.wizard.ui.model.MandatoryConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.MavenRepositoryLocation;
import com.worldline.appprofiles.wizard.ui.model.OptionalConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.SelectionConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.facade.BaseProperties;
import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IGlobalConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.ISelectionConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IValueConfigurator;

public class ApplicationCreationJob extends WorkspaceJob {

	public static final String PARENT_ARTIFACT_ID = "parentArtifactId";
	private ApplicationWizardOutput applicationWizardOutput;

	public ApplicationCreationJob(ApplicationWizardOutput applicationWizardOutput) {
		super(ApplicationCreationJobMessages.JOB_NAME.value());
		this.applicationWizardOutput = applicationWizardOutput;
		setPriority(Job.BUILD);
		setUser(true);
	}

	/**
	 * All the archetypes that need to be materialized, by module
	 */
	private Map<ApplicationModule, ArchetypeApplication> archetypeApplications = new LinkedHashMap<ApplicationModule, ArchetypeApplication>();

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

		monitor.beginTask(ApplicationCreationJobMessages.TASK_NAME.value(), 9);
		monitor.subTask(ApplicationCreationJobMessages.SUBTASK1_NAME.value());
		archetypeApplications.clear();

		Map<String, String> selectedProjectAsProperties = new HashMap<String, String>();

		ApplicationProfile selectedApplicationProfile = applicationWizardOutput.getSelectedApplicationProfile();
		// 1-Loop once on selected project, to list all of them
		for (ApplicationModule module : selectedApplicationProfile.getAllModules()) {
			if (module.isMandatory() || applicationWizardOutput.getSelectedModules().contains(module)) {

				if (module.getPackageNameSuffix() != null && module.getPackageNameSuffix().length() > 0) {
					selectedProjectAsProperties.put(module.getPackageNameSuffix().substring(1).toLowerCase()
							+ "Project", "true");
				}
			}
		}

		// 2- loop again on same projects, and configure them
		// (we need to set in each of them a property telling the list of
		// projects):
		for (ApplicationModule module : selectedApplicationProfile.getAllModules()) {
			if (module.isMandatory() || applicationWizardOutput.getSelectedModules().contains(module)) {

				if (module.getLocation() instanceof MavenRepositoryLocation) {
					MavenRepositoryLocation location = (MavenRepositoryLocation) module.getLocation();

					Archetype archetype = new Archetype(location.getArtifactId(), location.getGroupId(),
							location.getVersion(), location.getRepository(), null);

					Map<String, String> properties = new HashMap<String, String>();
					properties.putAll(this.loadProperties(module));
					properties.putAll(location.getProperties());
					properties.putAll(selectedProjectAsProperties);

					IPath projectPath = applicationWizardOutput.getProjectLocation();

					if (module.getParentModule() != null) {
						String parentArtifact = module.getParentModule().getProjectNamePrefix()
								+ MavenModuleConstants.getUserArtifactId(applicationWizardOutput)
								+ module.getParentModule().getProjectNameSuffix();
						projectPath = projectPath.append(parentArtifact);

						properties.put(PARENT_ARTIFACT_ID, parentArtifact);
					}

					ArchetypeApplication archetypeApplication = new ArchetypeApplication(archetype,
							MavenModuleConstants.getUserGroupId(applicationWizardOutput), module.getProjectNamePrefix()
									+ MavenModuleConstants.getUserArtifactId(applicationWizardOutput)
									+ module.getProjectNameSuffix(),
							MavenModuleConstants.getUserVersion(applicationWizardOutput),
							MavenModuleConstants.getUserPackageName(applicationWizardOutput)
									+ module.getPackageNameSuffix(), projectPath,
							MavenModuleConstants.getUserDescription(applicationWizardOutput));

					archetypeApplication.setProperties(properties);
					// If the module is a sub-module, no need to import it, as
					// the maven import on the parent, will take them all:
					archetypeApplication.setShouldImport(module.getParentModule() == null);

					archetypeApplications.put(module, archetypeApplication);
				}
			} else {
				ILog log = Activator.getDefault().getLog();
				log.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, ProcessMessages.ERROR_LOCATION_NOTSUPPORTED
						.value(module.getId())));
			}

			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
		}

		// Set working sets in Maven2ProjectCreator:
		monitor.worked(1);
		Maven2ProjectCreator maven2ProjectCreator = new Maven2ProjectCreator(archetypeApplications.values());
		maven2ProjectCreator.setWorkingSets(applicationWizardOutput.getSelectedWorkingSets());

		// ********** Execute the mvn archetype command to materialize all the
		// archetypes ******:
		maven2ProjectCreator.run(monitor);
		boolean offlineUpdate = Boolean.parseBoolean(applicationWizardOutput.getProperty(MavenModuleConstants.UPDATE_OFFLINE));
		boolean forceSnapshots = Boolean.parseBoolean(applicationWizardOutput.getProperty(MavenModuleConstants.UPDATE_FORCE_SNAPSHOTS));
		new Maven2ProjectBuilder(offlineUpdate, forceSnapshots).scheduleProjectsBuild(this, maven2ProjectCreator.getCreatedProjects());
		monitor.subTask(ApplicationCreationJobMessages.SUBTASK2_NAME.value());
		MultiStatus configurationStatus = new MultiStatus(Activator.PLUGIN_ID, SWT.OK,
				ApplicationCreationJobMessages.CONFIG_APPLICATION_STATUS_MESSAGE.value(), null);

		// Run all the simple configuration for selected profile
		for (ApplicationConfiguration configuration : applicationWizardOutput.getSelectedApplicationProfile()
				.getConfigurations()) {
			for (AbstractConfigurationEntry configurationEntry : configuration.getConfigurationEntries()) {
				for (IProject project : maven2ProjectCreator.getCreatedProjects()) {
					IStatus status = runConfiguration(configurationEntry, project, monitor,
							loadProperties(applicationWizardOutput));
					if (status != null)
						configurationStatus.add(status);
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
				}
			}

			// Run all the combinations for the selected profile
			for (ChoiceEntriesCombination combination : configuration.getOptionalEntriesCombinations()) {
				for (IProject project : maven2ProjectCreator.getCreatedProjects()) {
					IStatus status = runCombination(combination, project, monitor,
							loadProperties(applicationWizardOutput));
					if (status != null)
						configurationStatus.add(status);
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
				}
			}
		}

		Collection<ApplicationModule> modulesToConfigure = new ArrayList<ApplicationModule>();
		modulesToConfigure.addAll(applicationWizardOutput.getSelectedModules());
		for (ApplicationModule module : applicationWizardOutput.getSelectedApplicationProfile().getModules()) {
			if (module.isMandatory()) {
				modulesToConfigure.add(module);
			}
		}

		for (ApplicationModule module : modulesToConfigure) {
			for (ApplicationConfiguration configuration : module.getConfigurations()) {
				for (IProject project : maven2ProjectCreator.getCreatedProjectFor(archetypeApplications.get(module))) {
					if (project != null) {

						// Run all the simple configuration for the project
						// behind
						// selected module
						for (AbstractConfigurationEntry configurationEntry : configuration.getConfigurationEntries()) {
							IStatus status = runConfiguration(configurationEntry, project, monitor,
									loadProperties(module));
							if (status != null)
								configurationStatus.add(status);
							if (monitor.isCanceled())
								return Status.CANCEL_STATUS;
						}

						// Run all the combinations for the project behind
						// selected
						// module
						for (ChoiceEntriesCombination combination : configuration.getOptionalEntriesCombinations()) {
							IStatus status = runCombination(combination, project, monitor, loadProperties(module));
							if (status != null)
								configurationStatus.add(status);
							if (monitor.isCanceled())
								return Status.CANCEL_STATUS;
						}
					}
				}
			}
		}

		// Execution of global configurator, if specified.
		IGlobalConfigurator globalConfigurator = applicationWizardOutput.getSelectedApplicationProfile()
				.getGlobalConfigurator();
		if (globalConfigurator != null) {
			IStatus status = globalConfigurator.configure(monitor, loadProperties(applicationWizardOutput));
			if (status != null)
				configurationStatus.add(status);
		}

		monitor.worked(1);

		return !configurationStatus.isOK() ? configurationStatus : Status.OK_STATUS;
	}

	private Map<String, String> loadProperties(ApplicationModule module) {
		Map<String, String> properties = new HashMap<String, String>();
		properties.putAll(loadProperties(applicationWizardOutput));

		ArchetypeApplication archetypeApplication = archetypeApplications.get(module);
		if (archetypeApplication != null) {
			properties.put(BaseProperties.MODULE_GROUPID, archetypeApplication.getUserGroupId());
			properties.put(BaseProperties.MODULE_ARTIFACTID, archetypeApplication.getUserArtifactId());
			properties.put(BaseProperties.MODULE_VERSION, archetypeApplication.getUserVersion());
			properties.put(BaseProperties.MODULE_LOCATION, archetypeApplication.getProjectContainerPath().toString());
			properties.put(BaseProperties.MODULE_PACKAGE, archetypeApplication.getUserPackageName());
		}

		if (module.getLocation() != null)
			properties.putAll(module.getLocation().getProperties());

		for (ApplicationConfiguration config : module.getConfigurations()) {
			for (AbstractConfigurationEntry entry : config.getConfigurationEntries()) {
				if (entry.getVariable() != null && entry.getVariable().length() > 0) {
					if (entry instanceof ChoiceConfigurationEntry) {
						properties.put(entry.getVariable(), String.valueOf(getChoiceConfigurationEntryValue(
								applicationWizardOutput, (ChoiceConfigurationEntry) entry)));
					} else if (entry instanceof ValueConfigurationEntry) {
						properties.put(
								entry.getVariable(),
								getValueConfigurationEntryValue(applicationWizardOutput,
										(ValueConfigurationEntry) entry));
					}
				}
			}
		}

		return properties;
	}

	private Map<String, String> loadProperties(ApplicationWizardOutput output) {

		Map<String, String> properties = new HashMap<String, String>();

		properties.put(BaseProperties.APPLICATION_NAME, output.getProjectName());
		properties.put(BaseProperties.APPLICATION_GROUPID, MavenModuleConstants.getUserGroupId(output));
		properties.put(BaseProperties.APPLICATION_ARTIFACTID, MavenModuleConstants.getUserArtifactId(output));
		properties.put(BaseProperties.APPLICATION_VERSION, MavenModuleConstants.getUserVersion(output));
		properties.put(BaseProperties.APPLICATION_LOCATION, output.getProjectLocation().toString());
		properties.put(BaseProperties.APPLICATION_PACKAGE, MavenModuleConstants.getUserPackageName(output));

		if (output.getSelectedApplicationProfile() == null)
			return properties;

		for (ApplicationConfiguration config : output.getSelectedApplicationProfile().getConfigurations()) {
			for (AbstractConfigurationEntry entry : config.getConfigurationEntries()) {
				if (entry.getVariable() != null && entry.getVariable().length() > 0) {
					if (entry instanceof ChoiceConfigurationEntry) {
						properties.put(entry.getVariable(), String.valueOf(getChoiceConfigurationEntryValue(
								applicationWizardOutput, (ChoiceConfigurationEntry) entry)));
					} else if (entry instanceof ValueConfigurationEntry) {
						properties.put(
								entry.getVariable(),
								getValueConfigurationEntryValue(applicationWizardOutput,
										(ValueConfigurationEntry) entry));
					}
				}
			}
		}
		return properties;
	}

	private static boolean getChoiceConfigurationEntryValue(ApplicationWizardOutput output,
			ChoiceConfigurationEntry entry) {
		return output.getChoiceSelectedConfigurations().containsKey(entry) ? output.getChoiceSelectedConfigurations()
				.get(entry) : entry.getDefaultValue();
	}

	private static String getValueConfigurationEntryValue(ApplicationWizardOutput output, ValueConfigurationEntry entry) {
		return output.getValueSelectedConfiguration().containsKey(entry) ? output.getValueSelectedConfiguration().get(
				entry) : entry.getDefaultValue();
	}

	private IStatus runConfiguration(AbstractConfigurationEntry entry, IProject project, IProgressMonitor monitor,
			Map<String, String> properties) {
		if (entry instanceof MandatoryConfigurationEntry) {
			IConfigurator configurator = ((MandatoryConfigurationEntry) entry).getConfigurator();
			return configurator != null ? configurator.configure(project, monitor, properties) : Status.OK_STATUS;
		} else if (entry instanceof OptionalConfigurationEntry) {
			OptionalConfigurationEntry optEntry = (OptionalConfigurationEntry) entry;
			boolean shouldExecute = getChoiceConfigurationEntryValue(applicationWizardOutput, optEntry);
			if (shouldExecute) {
				IConfigurator configurator = ((OptionalConfigurationEntry) entry).getConfigurator();
				return configurator != null ? configurator.configure(project, monitor, properties) : Status.OK_STATUS;
			}
			return Status.OK_STATUS;
		} else if (entry instanceof SelectionConfigurationEntry) {
			SelectionConfigurationEntry optEntry = (SelectionConfigurationEntry) entry;
			boolean shouldExecute = getChoiceConfigurationEntryValue(applicationWizardOutput, optEntry);
			ISelectionConfigurator configurator = ((SelectionConfigurationEntry) entry).getConfigurator();
			return configurator != null ? configurator.configure(project, shouldExecute, monitor, properties)
					: Status.OK_STATUS;
		} else if (entry instanceof ValueConfigurationEntry) {
			String value = getValueConfigurationEntryValue(applicationWizardOutput, (ValueConfigurationEntry) entry);
			IValueConfigurator configurator = ((ValueConfigurationEntry) entry).getConfigurator();
			return configurator != null ? configurator.configure(project, value, monitor, properties)
					: Status.OK_STATUS;
		} else {
			ILog log = Activator.getDefault().getLog();
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					ProcessMessages.ERROR_CONFIGURATION_NOTSUPPORTED.value(entry.getClass().getName()));
			log.log(status);
			return status;
		}
	}

	private IStatus runCombination(ChoiceEntriesCombination combination, IProject project, IProgressMonitor monitor,
			Map<String, String> properties) {
		boolean shouldExecute = true;
		for (Iterator<ChoiceConfigurationEntry> iterator = combination.getOptionalConfigurationEntries().iterator(); iterator
				.hasNext() && shouldExecute;) {
			ChoiceConfigurationEntry entry = iterator.next();
			shouldExecute = applicationWizardOutput.getChoiceSelectedConfigurations().containsKey(entry) ? applicationWizardOutput
					.getChoiceSelectedConfigurations().get(entry) : entry.getDefaultValue();
		}
		if (shouldExecute && combination.getConfigurator() != null) {
			return combination.getConfigurator().configure(project, monitor, properties);
		}
		return Status.OK_STATUS;
	}
}
