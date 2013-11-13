/**
 * $Id: EclipseMavenRequest.java,v 1.2 2008/10/07 15:40:59 fr20349 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: EclipseMavenRequest.java,v $
 * Revision 1.2  2008/10/07 15:40:59  fr20349
 * FFo: Monitoring delegate to m2
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.embedder.internal;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.worldline.appprofiles.wizard.maven.Activator;
import com.worldline.appprofiles.wizard.maven.embedder.MavenInterruptedException;
import com.worldline.appprofiles.wizard.maven.embedder.MavenMonitorHolder;

/**
 * A EclipseMavenRequest provides the mechanism for scheduling a
 * MavenExecutionRequest through the instance of the EclipseMaven
 * 
 */

public class EclipseMavenRequest extends Job {

	private EclipseMaven maven;

	private MavenExecutionRequest request;

	public EclipseMavenRequest(String name, EclipseMaven maven, MavenExecutionRequest request) {
		super(name);
		this.maven = maven;
		this.request = request;

	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		MavenMonitorHolder.setProgressMonitor(monitor);

		monitor.beginTask("Maven build", 100);
		monitor.setTaskName("Maven " + this.request.getGoals());

		try {
			MavenExecutionResult status = this.maven.executeRequest(this.request);
			if ((status.getExceptions() != null) && (status.getExceptions().size() > 0))
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
						"Errors during Maven execution", (Exception) status.getExceptions().get(0));

			return new Status(IStatus.OK, Activator.PLUGIN_ID, 0, "Success", null);
		} catch (MavenInterruptedException e) {
			return Status.CANCEL_STATUS;
		} finally {
			monitor.done();
		}
	}
}
