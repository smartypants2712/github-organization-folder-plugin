package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.BulkChange;
import hudson.Extension;
import jenkins.branch.OrganizationFolder;
import jenkins.model.Jenkins;
import jenkins.scm.api.SCMSourceOwner;
import org.jenkinsci.plugins.github_branch_source.Connector;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
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
    public void applyOrg(OrganizationFolder of, GitHubSCMNavigator n) throws IOException {
        GitHub hub = connect(of, n);
        GHUser u = hub.getUser(n.getRepoOwner());

        BulkChange bc = new BulkChange(of);
        try {
            of.setIcon(new GitHubOrgIcon(n.getRepoOwner(),u.getAvatarUrl()));
            bc.commit();
        } finally {
            bc.abort();
        }
    }

    public void applyRepo(WorkflowMultiBranchProject item, GitHubSCMNavigator n) throws IOException {
//        GitHub hub = connect(item, n);
        BulkChange bc = new BulkChange(item);
        try {
            item.setIcon(new GitHubRepoIcon());
            bc.commit();
        } finally {
            bc.abort();
        }
    }

    GitHub connect(SCMSourceOwner of, GitHubSCMNavigator n) throws IOException {
        StandardCredentials credentials = Connector.lookupScanCredentials(of, n.getApiUri(), n.getScanCredentialsId());
        return Connector.connect(n.getApiUri(), credentials);
    }

    public static MainLogic get() {
        return Jenkins.getInstance().getInjector().getInstance(MainLogic.class);
    }

    private static final Logger LOGGER = Logger.getLogger(MainLogic.class.getName());

}
