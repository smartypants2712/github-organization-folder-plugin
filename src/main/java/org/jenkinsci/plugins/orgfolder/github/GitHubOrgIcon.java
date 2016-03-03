package org.jenkinsci.plugins.orgfolder.github;

import com.cloudbees.hudson.plugins.folder.FolderIcon;
import com.cloudbees.hudson.plugins.folder.FolderIconDescriptor;
import hudson.Extension;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.Stapler;

/**
 * @author Kohsuke Kawaguchi
 */
public class GitHubOrgIcon extends FolderIcon {
    /**
     * ID of the GitHub organization
     */
    private final String org;

    /**
     * Once resolved, URL of the avatar.
     */
    private String url;

    @DataBoundConstructor
    public GitHubOrgIcon(String org) {
        this.org = org;
    }

    public GitHubOrgIcon(String org, String url) {
        this.org = org;
        this.url = url;
    }

    @Override
    public String getImageOf(String s) {
        if (url==null) {
            // falll back to the generic github org icon
            return Stapler.getCurrentRequest().getContextPath()+ Hudson.RESOURCE_PATH+"/plugin/github-organization-folder/images/logo/"+s+".png";
        } else {
            String[] xy = s.split("x");
            if (xy.length==0)       return url;
            if (url.contains("?"))  return url+"&s="+xy[0];
            else                    return url+"?s="+xy[0];
        }
    }

    @Override
    public String getDescription() {
        return org;
    }

    @Extension
    public static class DescriptorImpl extends FolderIconDescriptor {
        @Override
        public String getDisplayName() {
            return "GitHub Organization Avatar";
        }
    }
}
