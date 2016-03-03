package org.jenkinsci.plugins.orgfolder.github;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;
import jenkins.branch.OrganizationFolder;
import jenkins.scm.api.SCMNavigator;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;

import javax.inject.Inject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class ItemListenerImpl extends ItemListener {
    @Inject
    private MainLogic main;

    @Override
    public void onUpdated(Item item) {
        super.onUpdated(item);
    }

    @Override
    public void onCreated(Item item) {
        if (item instanceof OrganizationFolder) {
            OrganizationFolder of = (OrganizationFolder)item;
            if (of.getNavigators().size()>0) {
                SCMNavigator n = of.getNavigators().get(0);
                if (n instanceof GitHubSCMNavigator) {
                    try {
                        main.setup(of);
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failed to apply GitHub Org Folder theme",e);
                    }
                }
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ItemListenerImpl.class.getName());
}
