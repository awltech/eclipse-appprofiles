/**
 * $Id: MavenMonitorHolder.java,v 1.1.1.1 2008/05/07 15:47:00 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: MavenMonitorHolder.java,v $
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.embedder;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Class that holds the progress monitor for Maven executions so other classes in the same thread can inspect it and
 * react to cancellation events.
 */
public class MavenMonitorHolder
{
    private static final ThreadLocal<IProgressMonitor> progressMonitorThreadLocal = new ThreadLocal<IProgressMonitor>();

    public static void setProgressMonitor( IProgressMonitor progressMonitor )
    {
        MavenMonitorHolder.progressMonitorThreadLocal.set( progressMonitor );
    }

    public static IProgressMonitor getProgressMonitor()
    {
        return MavenMonitorHolder.progressMonitorThreadLocal.get();
    }

    public static boolean isCanceled()
    {
        IProgressMonitor progressMonitor = MavenMonitorHolder.getProgressMonitor();
        return progressMonitor == null ? false : progressMonitor.isCanceled();
    }

}
