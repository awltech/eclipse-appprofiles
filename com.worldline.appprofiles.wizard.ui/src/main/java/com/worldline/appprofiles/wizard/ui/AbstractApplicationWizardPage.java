/**
 * $Id: AbstractApplicationWizardPage.java,v 1.1.1.1 2008/05/07 15:47:00 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: AbstractApplicationWizardPage.java,v $
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

/**
 * This abstract class implements the get/set methods in {@link IApplicationWizardPage}.
 * 
 * @author A&M
 */
public abstract class AbstractApplicationWizardPage extends WizardPage implements IApplicationWizardPage
{


    private Object pageContext;

    /**
     * Creates the wizard page with the given name, title and image descriptor.
     * 
     * @see WizardPage#WizardPage(String,String,ImageDescriptor)
     * @param pageName
     *            name for the wizard page.
     * @param title
     *            the title displayed in the page
     * @param titleImage
     *            the image used in the title.
     */
    protected AbstractApplicationWizardPage( String pageName, String title, ImageDescriptor titleImage )
    {
        super( pageName, title, titleImage );
    }

    /**
     * Creates the wizard page with the given name.
     * 
     * @see WizardPage#WizardPage(String)
     * @param pageName
     *            name for the wizard page.
     */
    protected AbstractApplicationWizardPage( String pageName )
    {
        super( pageName );
    }

    public Object getConfig()
    {
        return pageContext;
    }

    public void setConfig( Object context )
    {
        this.pageContext = context;
    }

    /**
     * Removes use of cached previous page and always asks the wizard.
     */
    @Override
    public IWizardPage getPreviousPage()
    {
        IWizard wizard = getWizard();
        if ( wizard == null )
        {
            return null;
        }

        return wizard.getPreviousPage( this );
    }
}