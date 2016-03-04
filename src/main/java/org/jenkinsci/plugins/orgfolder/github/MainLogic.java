package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.BulkChange;
import hudson.Extension;
import hudson.model.AllView;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.ListView;
import hudson.util.DescribableList;
import hudson.views.JobColumn;
import hudson.views.ListViewColumn;
import hudson.views.StatusColumn;
import hudson.views.WeatherColumn;
import jenkins.branch.Branch;
import jenkins.branch.OrganizationFolder;
import jenkins.model.Jenkins;
import jenkins.scm.api.SCMSourceOwner;
import org.jenkinsci.plugins.github_branch_source.Connector;
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.Arrays.*;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class MainLogic {
    /**
     * Applies UI customizations to a newly created {@link OrganizationFolder}
     * with a sole {@link GitHubSCMNavigator}
     */
    public void applyOrg(OrganizationFolder of, GitHubSCMNavigator scm) throws IOException {
        if (UPDATING.get().add(of)) {
            BulkChange bc = new BulkChange(of);
            try {
                GitHub hub = connect(of, scm);
                GHUser u = hub.getUser(scm.getRepoOwner());

                of.setIcon(new GitHubOrgIcon());
                of.getProperties().replace(new GitHubOrgProperty(u));
                of.replaceAction(new GitHubLink("logo",u.getHtmlUrl()));
                if (of.getDisplayNameOrNull()==null)
                    of.setDisplayName(u.getName());
                if (of.getView("Repositories")==null && of.getView("All") instanceof AllView) {
                    // need to set the default view
                    ListView lv = new ListView("Repositories");
                    lv.getColumns().replaceBy(asList(
                        new StatusColumn(),
                        new WeatherColumn(),
                        new CustomNameJobColumn(Messages.class,Messages._ListViewColumn_Repository()),
                        new RepositoryDescriptionColumn()
                    ));
                    lv.setIncludeRegex(".*");   // show all
                    of.addView(lv);
                    of.deleteView(of.getView("All"));
                    of.setPrimaryView(lv);
                }

                bc.commit();
            } finally {
                bc.abort();
                UPDATING.get().remove(of);
            }
        }
    }

    public void applyRepo(WorkflowMultiBranchProject item, GitHubSCMNavigator scm) throws IOException {
        if (UPDATING.get().add(item)) {
            GitHub hub = connect(item, scm);
            GHRepository repo = hub.getRepository(scm.getRepoOwner() + '/' + item.getName());

            BulkChange bc = new BulkChange(item);
            try {
                item.setIcon(new GitHubRepoIcon());
                item.getProperties().replace(new GitHubRepoProperty(repo));
                item.replaceAction(new GitHubLink("repo",repo.getHtmlUrl()));
                if (item.getView("Branches")==null && item.getView("All") instanceof AllView) {
                    // create initial views
                    ListView bv = new ListView("Branches");
                    DescribableList<ListViewColumn, Descriptor<ListViewColumn>> cols = bv.getColumns();
                    JobColumn name = cols.get(JobColumn.class);
                    if (name!=null)
                        cols.replace(name,new CustomNameJobColumn(Messages.class, Messages._ListViewColumn_Branch()));
                    bv.getJobFilters().add(new BranchJobFilter());

                    ListView pv = new ListView("Pull Requests");
                    cols = pv.getColumns();
                    name = cols.get(JobColumn.class);
                    if (name!=null)
                        cols.replace(name,new CustomNameJobColumn(Messages.class, Messages._ListViewColumn_PullRequest()));
                    bv.getJobFilters().add(new PullRequestJobFilter());

                    item.addView(bv);
                    item.addView(pv);
                    item.deleteView(item.getView("All"));
                    item.setPrimaryView(bv);
                }

                bc.commit();
            } finally {
                bc.abort();
                UPDATING.get().remove(item);
            }
        }
    }

    public void applyBranch(WorkflowJob branch, WorkflowMultiBranchProject repo, GitHubSCMNavigator scm) throws IOException {
        if (UPDATING.get().add(branch)) {
            Branch b = repo.getProjectFactory().getBranch(branch);
            GitHubLink repoLink = repo.getAction(GitHubLink.class);
            if (repoLink!=null) {
                BulkChange bc = new BulkChange(branch);
                try {
                    branch.replaceAction(new GitHubLink("branch",repoLink.getUrl()+"/tree/"+b.getName()));
                    bc.commit();
                } finally {
                    bc.abort();
                    UPDATING.get().remove(branch);
                }
            }
        }
    }

    GitHub connect(SCMSourceOwner of, GitHubSCMNavigator n) throws IOException {
        StandardCredentials credentials = Connector.lookupScanCredentials(of, n.getApiUri(), n.getScanCredentialsId());
        return Connector.connect(n.getApiUri(), credentials);
    }

    public static MainLogic get() {
        return Jenkins.getActiveInstance().getInjector().getInstance(MainLogic.class);
    }

    private static final Logger LOGGER = Logger.getLogger(MainLogic.class.getName());

    /**
     * Keeps track of what we are updating to avoid recursion.
     */
    private final ThreadLocal<Set<Item>> UPDATING = new ThreadLocal<Set<Item>>() {
        @Override
        protected Set<Item> initialValue() {
            return new HashSet<>();
        }
    };
}
