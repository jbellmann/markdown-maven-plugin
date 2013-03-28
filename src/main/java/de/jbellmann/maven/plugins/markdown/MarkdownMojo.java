package de.jbellmann.maven.plugins.markdown;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.rjeschke.txtmark.Processor;

import com.google.common.io.ByteStreams;

/**
 * @author  Joerg Bellmann
 * @goal    gen generate
 */
public class MarkdownMojo extends AbstractMojo {

    private static final String BEFORE_SNIPPET = "BEFORE_SNIPPET.txt";
    private static final String AFTER_SNIPPET = "AFTER_SNIPPET.txt";

    private static final String TARGET_FORMAT = "html";

    /**
     * @parameter  expression="${project.basedir}/src/markdown"
     * @required
     * @readonly
     */
    protected File markdownDirectory;

    /**
     * @parameter  default-value="${project.build.directory}/markdown"
     * @readonly   // at the moment
     */
    protected File markdownTargetDirectory;

    protected boolean failOnError = false;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!markdownDirectory.exists()) {
            getLog().info("No markdownDirectory found at " + markdownDirectory.getAbsolutePath());
            getLog().info("Skip execution");
        } else {
            if (!markdownTargetDirectory.exists()) {
                markdownTargetDirectory.mkdirs();
            }

            boolean execptionOccurred = false;

            // render
            File[] toRender = markdownDirectory.listFiles(new MarkdownFilesFilter());
            for (File sourceFile : toRender) {
                final String targetFileName = buildTargetFileName(sourceFile.getName());

                try {
                    String renderedContent = Processor.process(sourceFile);
                    final Writer writer = new FileWriter(new File(markdownTargetDirectory, targetFileName));
                    writer.write(new String(ByteStreams.toByteArray(getClass().getResourceAsStream(BEFORE_SNIPPET))));

                    //
                    writer.write(renderedContent);

                    writer.write(new String(ByteStreams.toByteArray(getClass().getResourceAsStream(AFTER_SNIPPET))));
                    writer.close();
                } catch (IOException e) {
                    execptionOccurred = true;
                    getLog().error("Exception processing File " + sourceFile.getAbsolutePath(), e);
                }
            }

            if (execptionOccurred && failOnError) {
                throw new MojoExecutionException("Errors occurred when processing files.");
            }
        }
    }

    protected String buildTargetFileName(final String name) {
        String result = name;
        if (name.endsWith(Suffixes.MD)) {
            result = name.replace(Suffixes.MD, TARGET_FORMAT);
        } else {
            result = name.replace(Suffixes.MARKDOWN, TARGET_FORMAT);
        }

        return result;
    }
}
