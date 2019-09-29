package io.jenkins.plugins.casc;

import hudson.model.Result;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries;
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class V1vTest {

    @Rule
    public JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("v1v.yml")
    public void run_shared_step() throws Exception {
        assertEquals(1, GlobalLibraries.get().getLibraries().size());
        final LibraryConfiguration library = GlobalLibraries.get().getLibraries().get(0);
        assertEquals("jenkins-pipeline-lib", library.getName());

        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "echo");
        job.setDefinition(new CpsFlowDefinition(fileContentsFromResources("log.groovy")));
        WorkflowRun build = job.scheduleBuild2(0).get();
        j.assertBuildStatus(Result.FAILURE, build);
        j.assertLogContains("ERROR: Forcing error", build);
    }

    @Test
    @ConfiguredWithCode("v1v.yml")
    public void run_shared_step_with_declarative() throws Exception {
        assertEquals(1, GlobalLibraries.get().getLibraries().size());
        final LibraryConfiguration library = GlobalLibraries.get().getLibraries().get(0);
        assertEquals("jenkins-pipeline-lib", library.getName());

        WorkflowJob job = j.jenkins.createProject(WorkflowJob.class, "something");
        job.setDefinition(new CpsFlowDefinition(fileContentsFromResources("declarativeLog.groovy")));
        WorkflowRun build = job.scheduleBuild2(0).get();
        j.assertBuildStatus(Result.FAILURE, build);
        j.assertLogContains("ERROR: Forcing error", build);
    }

    String fileContentsFromResources(String fileName) throws IOException {
        String fileContents = null;

        URL url = getClass().getResource("/" + fileName);
        if (url != null) {
            fileContents = IOUtils.toString(url, "UTF-8");
        }
        return fileContents;
    }
}
