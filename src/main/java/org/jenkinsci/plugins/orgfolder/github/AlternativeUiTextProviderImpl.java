package org.jenkinsci.plugins.orgfolder.github;

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
        return null;
    }
}
