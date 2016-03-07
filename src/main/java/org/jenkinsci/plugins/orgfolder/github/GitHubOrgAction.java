package org.jenkinsci.plugins.orgfolder.github;

import hudson.model.InvisibleAction;
import jenkins.branch.OrganizationFolder;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.net.URL;

/**
 * Invisible {@link OrganizationFolder} property that
 * retains information about GitHub organization.
 *
 * @author Kohsuke Kawaguchi
 */
public class GitHubOrgAction extends InvisibleAction {
    private final URL url;
    private final String name;
    private final String avatar;

    GitHubOrgAction(GHUser org) throws IOException {
        this.url = org.getHtmlUrl();
        this.name = org.getName();
        this.avatar = org.getAvatarUrl();
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
}
