package de.jbellmann.maven.plugins.markdown;

import java.io.File;
import java.io.FileFilter;

/**
 * @author  Joerg Bellmann
 */
class MarkdownFilesFilter implements FileFilter {

    public boolean accept(final File file) {
        if (!file.isFile()) {
            return false;
        }

        if (file.getName().endsWith(Suffixes.MD) || file.getName().endsWith(Suffixes.MARKDOWN)) {
            return true;
        }

        return false;
    }

}
