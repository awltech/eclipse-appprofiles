package com.worldline.appprofiles.wizard.maven;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.MavenUpdateRequest;

public class Maven2ProjectBuilder {

	private IProjectConfigurationManager projectConfigurationManager;
	private boolean offlineUpdate;
	private boolean forceSnapshots;

	public Maven2ProjectBuilder() {
		this(MavenPlugin.getProjectConfigurationManager(), false, true);
	}
	
	public Maven2ProjectBuilder(IProjectConfigurationManager projectConfigurationManager, boolean offlineUpdate, boolean forceSnapshots) {
		this.projectConfigurationManager = projectConfigurationManager;
		this.offlineUpdate = offlineUpdate;
		this.forceSnapshots = forceSnapshots;
	}

	public Maven2ProjectBuilder(boolean offlineUpdate, boolean forceSnapshots) {
		this(MavenPlugin.getProjectConfigurationManager(), offlineUpdate, forceSnapshots);
	}

	public void buildProjects(Collection<IProject> projects, IProgressMonitor monitor) {
		for (IProject project : projects) {
			MavenUpdateRequest mavenUpdateRequest = new MavenUpdateRequest(project, offlineUpdate, forceSnapshots);
			try {
				projectConfigurationManager.updateProjectConfiguration(mavenUpdateRequest, monitor);
			} catch (Exception e) {
				Activator
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, AppProfilesWizardMaven.MAVEN_ERROR_UPDATE
								.value(), e));
			}
		}

		for (IProject project : projects)
			try {
				project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
			} catch (CoreException e) {
				Activator
						.getDefault()
						.getLog()
						.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, AppProfilesWizardMaven.MAVEN_ERROR_UPDATE
								.value(), e));
			}
	}

	public void scheduleProjectsBuild(final Job job, final Collection<IProject> projects) {
		job.addJobChangeListener(new IJobChangeListener() {

			@Override
			public void sleeping(IJobChangeEvent event) {
			}

			@Override
			public void scheduled(IJobChangeEvent event) {
			}

			@Override
			public void running(IJobChangeEvent event) {
			}

			@Override
			public void done(IJobChangeEvent event) {
				new Job("Updating Created Maven Projects...") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						buildProjects(projects, monitor);
						return Status.OK_STATUS;
					}
				}.schedule();
			}

			@Override
			public void awake(IJobChangeEvent event) {
			}

			@Override
			public void aboutToRun(IJobChangeEvent event) {
			}
		});
	}

}
