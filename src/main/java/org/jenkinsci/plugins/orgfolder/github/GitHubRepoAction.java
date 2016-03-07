package org.jenkinsci.plugins.orgfolder.github;

import hudson.model.InvisibleAction;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.kohsuke.github.GHRepository;

import java.net.URL;

/**
 * Invisible property on {@link WorkflowMultiBranchProject}
 * that retains information about GitHub repository.
 *
 * @author Kohsuke Kawaguchi
 */
public class GitHubRepoAction extends InvisibleAction {
    private final URL url;
    private final String description;
    private final String homepage;

    GitHubRepoAction(GHRepository repo) {
        this.url = repo.getHtmlUrl();
        this.description = repo.getDescription();
        this.homepage = repo.getHomepage();
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
}
