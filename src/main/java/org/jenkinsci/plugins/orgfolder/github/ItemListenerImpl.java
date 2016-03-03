package org.jenkinsci.plugins.orgfolder.github;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;
import org.jenkinsci.plugins.orgfolder.github.Sniffer.FolderMatch;
import org.jenkinsci.plugins.orgfolder.github.Sniffer.RepoMatch;

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
        maybeApply(item);
    }

    @Override
    public void onCreated(Item item) {
        maybeApply(item);
    }

    private void maybeApply(Item item) {
        try {
            FolderMatch f = Sniffer.matchFolder(item);
            if (f!=null) {
                main.applyOrg(f.folder, f.scm);
            }

            RepoMatch r = Sniffer.matchRepo(item);
            if (r!=null) {
                main.applyRepo(r.repo, r.scm);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to apply GitHub Org Folder theme to " + item.getFullName(), e);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ItemListenerImpl.class.getName());
}
