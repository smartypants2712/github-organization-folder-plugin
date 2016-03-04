package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.hudson.plugins.folder.computed.ComputedFolder;
import com.cloudbees.hudson.plugins.folder.computed.FolderComputation;
import hudson.Extension;
import hudson.model.AbstractItem;
import hudson.util.AlternativeUiTextProvider;

/**
 * @author Kohsuke Kawaguchi
 */
@Extension
public class AlternativeUiTextProviderImpl extends AlternativeUiTextProvider {
    @Override
    public <T> String getText(Message<T> text, T context) {
        if (text==AbstractItem.PRONOUN) {
            AbstractItem i = AbstractItem.PRONOUN.cast(context);
            if (Sniffer.matchRepo(i)!=null) {
                return "Repository";
            }
            if (Sniffer.matchBranch(i)!=null) {
                return "Branch";
            }
        }
        if (text== FolderComputation.DISPLAY_NAME) {
            ComputedFolder<?> f = FolderComputation.DISPLAY_NAME.cast(context).getParent();
            if (Sniffer.matchOrg(f)!=null) {
                return "Re-scan GitHub Org";
            }
        }
        return null;
    }
}
