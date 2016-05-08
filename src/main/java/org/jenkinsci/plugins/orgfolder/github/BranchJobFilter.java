package org.jenkinsci.plugins.orgfolder.github;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ViewJobFilter;
import jenkins.scm.api.SCMHead;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.model.View;

/**
 * Show branch jobs excluding PRs.
 *
 * @author Kohsuke Kawaguchi
 */
public class BranchJobFilter extends AbstractBranchJobFilter {
    @DataBoundConstructor
    public BranchJobFilter() {}

    @Override
    protected boolean shouldShow(SCMHead head, View filteringView) {
        return !(head instanceof PullRequestSCMHead) && filteringView.getViewName().equals("Branches");
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ViewJobFilter> {
        @Override
        public String getDisplayName() {
            return "GitHub Branch Jobs Only";
        }
    }

}
