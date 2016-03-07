package org.jenkinsci.plugins.orgfolder.github;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ViewJobFilter;
import jenkins.scm.api.SCMHead;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Show PRs excluding branch jobs.
 *
 * @author Kohsuke Kawaguchi
 */
public class PullRequestJobFilter extends AbstractBranchJobFilter {
    @DataBoundConstructor
    public PullRequestJobFilter() {}

    @Override
    protected boolean shouldShow(SCMHead head) {
        return head instanceof PullRequestSCMHead;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ViewJobFilter> {
        @Override
        public String getDisplayName() {
            return "GitHub Pull Request Jobs Only";
        }
    }

}
