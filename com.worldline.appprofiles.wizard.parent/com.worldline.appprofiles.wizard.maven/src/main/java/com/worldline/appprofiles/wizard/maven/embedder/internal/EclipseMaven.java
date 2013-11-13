/**
 * $Id: EclipseMaven.java,v 1.4 2009/02/27 12:44:32 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: EclipseMaven.java,v $
 * Revision 1.4  2009/02/27 12:44:32  a125788
 * MVA : Reusing status from goal execution to get real error when occurs
 *
 * Revision 1.3  2008/10/07 15:40:13  fr20349
 * FFo: Same maven embedder as m2
 *
 * Revision 1.2  2008/06/27 16:03:52  fr20349
 * FFo: change start mathod for the return true inside the if statement
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.embedder.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.maven.Activator;
import com.worldline.appprofiles.wizard.maven.embedder.MavenInterruptedException;
import com.worldline.appprofiles.wizard.maven.embedder.MavenManager;

/**
 * Default implementation of IMaven for Eclipse
 * 
 */
public class EclipseMaven {

	/**
	 * @param baseDirectory
	 * @param goal
	 * @param properties
	 * @param monitor
	 */
	public IStatus executeGoal(IPath baseDirectory, String goal, Properties properties, IProgressMonitor monitor) {

		return executeGoal(baseDirectory, goal, null, properties, monitor);
	}

	/**
	 * @param baseDirectory
	 * @param goal
	 * @param pomFile
	 *            pom on which we execute the goal if any
	 * @param properties
	 * @param monitor
	 */
	public IStatus executeGoal(IPath baseDirectory, String goal, String pomFile, Properties properties,
			IProgressMonitor monitor) {

		MavenExecutionRequest request = this.generateRequest(properties, Collections.singletonList(goal));
		request.setBaseDirectory(new File(baseDirectory.toOSString()));
		if (pomFile != null) {
			request.setPom(new File(pomFile));
		}

		EclipseMavenRequest eclipseMavenRequest = new EclipseMavenRequest("MavenRequest", this, request);
		IStatus status = eclipseMavenRequest.run(monitor);

		if (status.getSeverity() == IStatus.CANCEL)
			throw new MavenInterruptedException();
		return status;
	}

	/**
	 * @param request
	 * @return
	 */
	public MavenExecutionResult executeRequest(MavenExecutionRequest request) {

		if (Boolean.parseBoolean(System.getProperty(Activator.DEBUG_PROPERTY))) {
			StringBuilder sb = new StringBuilder("Will execute maven request (in ").append(request.getBaseDirectory())
					.append("):\n");
			for (String goal : request.getGoals()) {
				sb.append(goal).append("\n");
			}

			Properties userProperties = request.getUserProperties();
			List<String> paramNames = new ArrayList<String>(userProperties.stringPropertyNames());
			Collections.sort(paramNames);

			for (String paramName : paramNames) {
				sb.append("\t-").append(paramName).append("\t\t").append(userProperties.getProperty(paramName))
						.append("\n");
			}

			Activator.getDefault().logInfo(sb.toString());
		}
		return MavenManager.getMavenEmbedder().execute(request, new NullProgressMonitor());
	}

	/**
	 * Generate a Maven request to execute.
	 * 
	 * @param properties
	 * @param goals
	 * @return
	 */
	private MavenExecutionRequest generateRequest(Properties properties, List<String> goals) {
		MavenExecutionRequest mavenRequest = null;

		try {
			mavenRequest = MavenManager.getMavenEmbedder().createExecutionRequest(new NullProgressMonitor());

			mavenRequest.setGoals(goals);

			if (properties != null) {
				mavenRequest.getUserProperties().putAll(properties);
			}
		} catch (CoreException e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR,
							"An exception occured creating a Maven request", e));
			throw new MavenInterruptedException("An exception occured creating a Maven request");
		}

		return mavenRequest;
	}

}
