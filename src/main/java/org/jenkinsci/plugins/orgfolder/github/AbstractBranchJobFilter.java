package org.jenkinsci.plugins.orgfolder.github;

import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.views.ViewJobFilter;
import jenkins.scm.api.SCMHead;
import org.jenkinsci.plugins.orgfolder.github.Sniffer.BranchMatch;

import java.util.List;

/**
 * Base class for {@link ViewJobFilter}s to segregate PRs and normal branches.
 *
 * @author Kohsuke Kawaguchi
 */
abstract class AbstractBranchJobFilter extends ViewJobFilter {
    public AbstractBranchJobFilter() {}

    @Override
    public List<TopLevelItem> filter(List<TopLevelItem> added, List<TopLevelItem> all, View filteringView) {
        for (TopLevelItem i : all) {
            if (added.contains(i))      continue;   // already in there

            BranchMatch b = Sniffer.matchBranch(i);
            if (b!=null) {
                SCMHead head = b.getScmBranch().getHead();
                if (shouldShow(head))
                    added.add(i);
            }
        }
        return added;
    }

    protected abstract boolean shouldShow(SCMHead head);
}

