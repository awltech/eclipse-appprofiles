package com.worldline.appprofiles.wizard.maven;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.IMavenProjectImportResult;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

import com.worldline.appprofiles.wizard.maven.archetype.ArchetypeApplication;
import com.worldline.appprofiles.wizard.maven.archetype.goals.GenerateArchetypeGoal;
import com.worldline.appprofiles.wizard.maven.workarounds.FileEncoder;
import com.worldline.appprofiles.wizard.maven.workarounds.FilesRemover;

/**
 * Maven2 Project Creator. This creator will invoke Maven engine to generate
 * archetypes, from archetype applications passed as input.
 * 
 * @author mvanbesien
 * 
 */
public class Maven2ProjectCreator {

	/*
	 * Archetype application list
	 */
	private Collection<ArchetypeApplication> archetypeApplicationList = new ArrayList<ArchetypeApplication>();

	private Map<ArchetypeApplication, Collection<IProject>> createdProjects = new LinkedHashMap<ArchetypeApplication, Collection<IProject>>();

	private static final String DIR_JAVA = "src" + IPath.SEPARATOR + "main" + IPath.SEPARATOR + "java";

	private IWorkingSet[] workingSets = new IWorkingSet[0];

	/**
	 * Creates new Creator from Archetype Application List
	 * 
	 * @param archetypeApplicationList
	 *            Archetype Application List
	 */
	public Maven2ProjectCreator(Collection<ArchetypeApplication> archetypeApplicationList) {
		this.archetypeApplicationList.clear();
		this.archetypeApplicationList.addAll(archetypeApplicationList);
	}

	public void setWorkingSets(IWorkingSet[] workingSets) {
		this.workingSets = workingSets;
	}

	/**
	 * Executes Creator
	 * 
	 * @param mon
	 *            : Progress monitor
	 * @return status
	 */
	public IStatus run(IProgressMonitor mon) {

		if (MavenRepoFullInstallDialog.shouldDisplay()) {
			MavenRepoFullInstallDialog dialog = new MavenRepoFullInstallDialog();
			Display.getDefault().syncExec(dialog);
			if (dialog.getButton() != Dialog.OK)
				return Status.CANCEL_STATUS;
		}

		this.createdProjects.clear();

		try {
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_1.value());

			// 1 - Execute mvn goal:
			for (ArchetypeApplication archetypeApplication : this.archetypeApplicationList) {
				GenerateArchetypeGoal generateArchetypeGoal = new GenerateArchetypeGoal(
						archetypeApplication.getArchetype(), archetypeApplication.getProjectContainerPath(),
						archetypeApplication.getUserGroupId(), archetypeApplication.getUserArtifactId(),
						archetypeApplication.getUserVersion(), archetypeApplication.getUserPackageName(),
						archetypeApplication.getUserDescription(), archetypeApplication.getUserProperties());

				IStatus status = generateArchetypeGoal.executeGoal();

				if (status.getSeverity() == IStatus.ERROR) {
					Activator.getDefault().logError(AppProfilesWizardMaven.PROJ_CREATION_ERROR.value(),
							status.getException());
					AppProfilesWizardMavenLogger.logger.log(Level.SEVERE,
							AppProfilesWizardMaven.PROJ_CREATION_ERROR_2.value(), status.getException());
					this.openError(status);
					return Status.CANCEL_STATUS;
				}
			}
			mon.worked(1);

			// 2 - Check file encoding (still usefull?):
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_2.value());
			for (ArchetypeApplication archetypeApplication : this.archetypeApplicationList) {
				FileEncoder.checkFileEncoding(archetypeApplication.getProjectContainerPath().append(
						archetypeApplication.getUserArtifactId() + IPath.SEPARATOR
								+ IProjectDescription.DESCRIPTION_FILE_NAME));
			}
			mon.worked(1);

			// 3 - Remove .touch files:
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_3.value());
			for (ArchetypeApplication archetypeApplication : this.archetypeApplicationList) {
				FilesRemover.removeTouchFiles(archetypeApplication.getProjectContainerPath().append(
						archetypeApplication.getUserArtifactId()));
			}
			mon.worked(1);

			// 4 - Maven import in workspace:
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_4.value());
			for (ArchetypeApplication archetypeApplication : archetypeApplicationList) {
				if (archetypeApplication.isShouldImport()) {
					IPath path = archetypeApplication.getProjectContainerPath().append(
							archetypeApplication.getUserArtifactId());
					Collection<IProject> projects = importExistingMavenProject(path, mon);
					if (projects != null) {
						this.createdProjects.put(archetypeApplication, projects);
					}
				}
			}
			mon.worked(1);

			// 5 - Add to working sets:
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_7.value());
			if (PlatformUI.isWorkbenchRunning()) {
				IWorkingSetManager workingSetManager = PlatformUI.getWorkbench().getWorkingSetManager();
				for (Collection<IProject> projects : this.createdProjects.values()) {
					for (IProject project : projects) {
						workingSetManager.addToWorkingSets(project, workingSets);
					}
				}
			}
			mon.worked(1);

			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_6.value());

			// 6 - Create the package here
			for (ArchetypeApplication archetypeApplication : this.createdProjects.keySet()) {
				for (IProject value : this.createdProjects.get(archetypeApplication)) {
					IPath tempFilePath = value.getLocation().append(DIR_JAVA)
							.append(archetypeApplication.getUserPackageName().replace('.', IPath.SEPARATOR))
							.append("temp");

					File tempFile = tempFilePath.toFile();

					if (!tempFile.getParentFile().exists()) {
						boolean packCreationOk = tempFile.getParentFile().mkdirs();

						if (!packCreationOk) {
							Activator.getDefault().logError(
									AppProfilesWizardMaven.PACK_CREATION_ERROR.value(value.getName(),
											archetypeApplication.getUserPackageName()));
							AppProfilesWizardMavenLogger.logger.log(Level.SEVERE,
									AppProfilesWizardMaven.PACK_CREATION_ERROR.value(value.getName(),
											archetypeApplication.getUserPackageName()));
						}
					}
				}
			}
			mon.worked(1);

			// 7 - Refresh
			mon.subTask(AppProfilesWizardMaven.PROJ_CREATION_5.value());
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, mon);

		} catch (CoreException ce) {
			Activator.getDefault().logError(AppProfilesWizardMaven.PROJ_CREATION_ERROR.value(), ce);
			AppProfilesWizardMavenLogger.logger.log(Level.SEVERE, AppProfilesWizardMaven.PROJ_CREATION_ERROR.value(),
					ce);
			return Status.CANCEL_STATUS;
		} catch (OperationCanceledException oce) {
			Activator.getDefault().logError(AppProfilesWizardMaven.PROJ_CREATION_ERROR.value(), oce);
			AppProfilesWizardMavenLogger.logger.log(Level.SEVERE, AppProfilesWizardMaven.PROJ_CREATION_ERROR.value());
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	private void openError(final IStatus status) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(new Shell(Display.getDefault()),
						AppProfilesWizardMaven.DIALOG_ERROR_TITLE.value(),
						AppProfilesWizardMaven.DIALOG_ERROR_MESSAGE.value(), status, IStatus.ERROR);
			}
		});

	}

	/**
	 * Imports the given path into the workspace as a project. Returns true if
	 * the operation succeeded, false if it failed to import due to an overlap.
	 * 
	 * @param projectPath
	 * @return
	 * @throws CoreException
	 *             if operation fails catastrophically
	 */
	private Collection<IProject> importExistingMavenProject(IPath projectPath, IProgressMonitor monitor)
			throws CoreException {

		MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		File rootFile = root.getLocation().toFile();
		LocalProjectScanner scanner = new LocalProjectScanner(rootFile, projectPath.toString(), false,
				mavenModelManager);
		try {
			scanner.run(monitor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<MavenProjectInfo> mavenProjects = new ArrayList<MavenProjectInfo>();
		for (MavenProjectInfo project : scanner.getProjects())
			extractProjects(project, mavenProjects);

		IProjectConfigurationManager projectConfigurationManager = MavenPlugin.getProjectConfigurationManager();
		ProjectImportConfiguration configuration = new ProjectImportConfiguration(new ResolverConfiguration());
		List<IMavenProjectImportResult> results = new ArrayList<IMavenProjectImportResult>();
		Collection<IProject> eclipseProjects = new ArrayList<IProject>();

		try {
			results.addAll(projectConfigurationManager.importProjects(mavenProjects, configuration, monitor));
		} catch (Exception e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, AppProfilesWizardMaven.MAVEN_ERROR_IMPORT
							.value(), e));
			for (MavenProjectInfo mavenProject : mavenProjects) {
				File[] listFiles = mavenProject.getPomFile().getParentFile().listFiles(new FilenameFilter() {

					@Override	
					public boolean accept(File dir, String name) {
						return ".project".equals(name);
					}
				});
				if (listFiles.length > 0) {
					try {
						IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(
								new Path(listFiles[0].getAbsolutePath()));
						IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(mavenProject.getModel().getArtifactId());
						if (!project.exists()) {
							project.create(description, null);
							project.open(null);
						}
						projectConfigurationManager.enableMavenNature(project, new ResolverConfiguration(), new NullProgressMonitor());
						projectConfigurationManager.addMavenBuilder(project, description, new NullProgressMonitor());
						eclipseProjects.add(project);
					} catch (CoreException ee) {
						Activator
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, AppProfilesWizardMaven.MAVEN_ERROR_IMPORT
								.value(), ee));
					}
				}
			}
		}

		for (IMavenProjectImportResult result : results)
			eclipseProjects.add(result.getProject());

		return eclipseProjects;
	}

	/**
	 * Extracts recursively the sub projects contained by the provided project.
	 * 
	 * @param project
	 */
	private void extractProjects(MavenProjectInfo project, List<MavenProjectInfo> mavenProjects) {
		mavenProjects.add(project);
		for (MavenProjectInfo subProject : project.getProjects())
			this.extractProjects(subProject, mavenProjects);
	}

	public Collection<IProject> getCreatedProjects() {
		Collection<IProject> allTheProjects = new ArrayList<IProject>();
		for (Collection<IProject> projects : createdProjects.values())
			allTheProjects.addAll(projects);
		return allTheProjects;
	}

	public Collection<IProject> getCreatedProjectFor(ArchetypeApplication application) {
		return createdProjects.containsKey(application) ? createdProjects.get(application) : new HashSet<IProject>();
	}

}
