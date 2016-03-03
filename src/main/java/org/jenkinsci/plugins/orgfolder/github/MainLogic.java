package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.Extension;
import hudson.model.Item;
import jenkins.branch.OrganizationFolder;
import jenkins.model.Jenkins;
import jenkins.scm.api.SCMNavigator;
import org.jenkinsci.plugins.github_branch_source.Connector;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.logging.Level;
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
    public void setup(OrganizationFolder of) throws IOException {
        if (of.getProperties().get(GitHubOrgTheme.class)==null) {
            of.addProperty(new GitHubOrgTheme());
        }
    }

    public void maybeApply(Item item) {
        if (item instanceof OrganizationFolder) {
            OrganizationFolder of = (OrganizationFolder)item;
            if (of.getNavigators().size()>0) {
                SCMNavigator n = of.getNavigators().get(0);
                if (n instanceof GitHubSCMNavigator) {
                    try {
                        apply(of, (GitHubSCMNavigator)n);
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Failed to apply GitHub Org Folder theme",e);
                    }
                }
            }
        }
    }

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
