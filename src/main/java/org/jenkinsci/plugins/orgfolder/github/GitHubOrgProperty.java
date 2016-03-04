package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import hudson.Extension;
import hudson.model.Descriptor.FormException;
import jenkins.branch.OrganizationFolder;
import net.sf.json.JSONObject;
import org.kohsuke.github.GHUser;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.net.URL;

/**
 * Invisible {@link AbstractFolderProperty} that retains information about GitHub organization.
 *
 * @author Kohsuke Kawaguchi
 */
public class GitHubOrgProperty extends AbstractFolderProperty<OrganizationFolder> {
    private final URL url;
    private final String name;
    private final String avatar;

    GitHubOrgProperty(GHUser org) throws IOException {
        this.url = org.getHtmlUrl();
        this.name = org.getName();
        this.avatar = org.getAvatarUrl();
    }

    /**
     * Not meant to be instantiated from UI
     */
    @DataBoundConstructor
    public GitHubOrgProperty() {
        url = null;
        name = avatar = null;
    }

    /**
     * When the user submits the configuration we keep the current instance.
     */
    @Override
    public AbstractFolderProperty<?> reconfigure(StaplerRequest req, JSONObject form) throws FormException {
        return this;
    }

    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    @Extension
    public static class DescriptorImpl extends AbstractFolderPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "GitHub Organization";
        }

        /**
         * Try to hide from UI
         */
        @Override
        public boolean isApplicable(Class<? extends AbstractFolder> containerType) {
            return false;
        }

        /**
         * Try to hide from UI
         */
        @Override
        public AbstractFolderProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return null;
        }
    }
}
