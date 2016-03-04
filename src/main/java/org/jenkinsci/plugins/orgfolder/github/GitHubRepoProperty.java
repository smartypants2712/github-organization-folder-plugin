package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import com.cloudbees.hudson.plugins.folder.AbstractFolderProperty;
import com.cloudbees.hudson.plugins.folder.AbstractFolderPropertyDescriptor;
import hudson.Extension;
import hudson.model.Descriptor.FormException;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.kohsuke.github.GHRepository;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.net.URL;

/**
 * Invisible {@link AbstractFolderProperty} that retains information about GitHub repository.
 *
 * @author Kohsuke Kawaguchi
 */
public class GitHubRepoProperty extends AbstractFolderProperty<WorkflowMultiBranchProject> {
    private final URL url;
    private final String description;
    private final String homepage;

    GitHubRepoProperty(GHRepository repo) {
        this.url = repo.getHtmlUrl();
        this.description = repo.getDescription();
        this.homepage = repo.getHomepage();
    }

    /**
     * Not meant to be instantiated from UI
     */
    @DataBoundConstructor
    public GitHubRepoProperty() {
        url = null;
        description = homepage = null;
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

    public String getDescription() {
        return description;
    }

    public String getHomepage() {
        return homepage;
    }

    @Extension
    public static class DescriptorImpl extends AbstractFolderPropertyDescriptor {
        @Override
        public String getDisplayName() {
            return "GitHub Repository";
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
