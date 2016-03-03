package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.FolderProperty;
import com.cloudbees.hudson.plugins.folder.FolderPropertyDescriptor;
import hudson.Extension;
import hudson.model.Descriptor.FormException;
import hudson.model.ReconfigurableDescribable;
import jenkins.branch.OrganizationFolder;
import jenkins.util.Timer;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Kohsuke Kawaguchi
 */
public class GitHubOrgTheme extends FolderProperty {
    @DataBoundConstructor
    public GitHubOrgTheme() {
    }

    @Override
    public AbstractFolderProperty<?> reconfigure(StaplerRequest req, JSONObject form) throws FormException {
        return this;
    }

    // HACK
    @Override
    protected void setOwner(final AbstractFolder owner) {
        Timer.get().schedule(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                MainLogic.get().maybeApply(owner);
                return null;
            }
        }, 1, TimeUnit.SECONDS);
    }

    @Extension
    public static class DescriptorImpl extends FolderPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "GitHub Org Folder UI customization";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractFolder> containerType) {
            return OrganizationFolder.class.isAssignableFrom(containerType);
        }
    }
}
