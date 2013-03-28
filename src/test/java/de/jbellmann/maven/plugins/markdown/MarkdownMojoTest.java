package de.jbellmann.maven.plugins.markdown;

import de.jbellmann.maven.plugins.markdown.MarkdownFilesFilter;
import de.jbellmann.maven.plugins.markdown.MarkdownMojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.junit.Assert;
import org.junit.Test;

public class MarkdownMojoTest {

    @Test
    public void testMojo() throws MojoExecutionException, MojoFailureException {
        MarkdownMojo mojo = new MarkdownMojo();
        mojo.markdownDirectory = getSourceDirectory();
        mojo.markdownTargetDirectory = getTargetDirectory();
        mojo.execute();
    }

    @Test
    public void testFilenameFilter() {
        File sourceDirectory = getSourceDirectory();
        File[] sourceFiles = sourceDirectory.listFiles(new MarkdownFilesFilter());
        Assert.assertFalse(sourceFiles.length == 0);
    }

    public File getTargetDirectory() {
        File file = new File(System.getProperty("user.dir"));
        File targetDirectory = new File(file, "target/markdown");
        return targetDirectory;
    }

    public File getSourceDirectory() {
        File file = new File(System.getProperty("user.dir"));
        File sourceDirectory = new File(file, "src/test/resources");
        return sourceDirectory;
    }

}
