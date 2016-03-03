package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.Extension;
import jenkins.branch.OrganizationFolder;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.github_branch_source.Connector;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class MainLogic {
    /**
     * Applies UI customizations to a newly created {@link OrganizationFolder}
     * with a sole {@link GitHubSCMNavigator}
     */
    public void apply(OrganizationFolder of, GitHubSCMNavigator n) throws IOException {
        GitHub hub = connect(of, n);
        GHUser u = hub.getUser(n.getRepoOwner());
        of.setIcon(new GitHubOrgIcon(n.getRepoOwner(),u.getAvatarUrl()));
    }

    GitHub connect(OrganizationFolder of, GitHubSCMNavigator n) throws IOException {
        StandardCredentials credentials = Connector.lookupScanCredentials(of, n.getApiUri(), n.getScanCredentialsId());
        return Connector.connect(n.getApiUri(), credentials);
    }

    public static MainLogic get() {
        return Jenkins.getInstance().getInjector().getInstance(MainLogic.class);
    }

    private static final Logger LOGGER = Logger.getLogger(MainLogic.class.getName());
}
