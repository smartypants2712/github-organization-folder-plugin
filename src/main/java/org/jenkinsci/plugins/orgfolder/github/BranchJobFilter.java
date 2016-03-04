package org.jenkinsci.plugins.orgfolder.github;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ViewJobFilter;
import jenkins.scm.api.SCMHead;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Show branch jobs excluding PRs.
 *
 * @author Kohsuke Kawaguchi
 */
public class BranchJobFilter extends AbstractBranchJobFilter {
    @DataBoundConstructor
    public BranchJobFilter() {}

    @Override
    protected boolean shouldShow(SCMHead head) {
        return !(head instanceof PullRequestSCMHead);
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ViewJobFilter> {
        @Override
        public String getDisplayName() {
            return "GitHub Branch Jobs Only";
        }
    }

}
