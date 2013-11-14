/**
 * $Id: IApplicationWizardPage.java,v 1.1.1.1 2008/05/07 15:47:00 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: IApplicationWizardPage.java,v $
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui;

import org.eclipse.jface.wizard.IWizardPage;

/**
 * The <code>IApplicationWizardPage</code> interface allows creating wizard pages that can be integrated into Q wizards to
 * gather additional information.
 * 
 * <b>Note:</b> Implementors can extend {@link AbstractApplicationWizardPage} instead.
 * 
 * The implementors will receive the context and is its responsibility to populate it with user provided information. If
 * the page is not displayed (i.e. the user presses Finish before reaching this page), suitable defaults must be
 * provided.
 * 
 * This interface is intended to be implemented only by the clients of the
 * <code>org.devzuz.q.maven.wizard.archetypeExtended</code> extension point.
 * 
 * @author A&M
 */
public interface IApplicationWizardPage extends IWizardPage
{


    /**
     * Returns the page-specific configuration.
     * 
     * @return the page-specific config.
     */
    public abstract Object getConfig();

    /**
     * Sets the page-specific configuration.
     * 
     * @param context
     *            the page-specific config.
     */
    public abstract void setConfig( Object context );

}