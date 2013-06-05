package com.gogomaya.tests.validation;

import java.util.List;

import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { CommonSpringConfiguration.class})
public class ValidationStory extends JUnitStory {

    @Autowired
    private ApplicationContext context;

    public ValidationStory() {
    }

    @Override
    public Configuration configuration() {
        StoryLoader storyLoader = new LoadFromRelativeFile(getClass().getResource("/"));

        StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder()
            .withFailureTrace(true)
            .withFailureTraceCompression(true)
            .withReporters(new ConsoleOutput());

        return new MostUsefulConfiguration().useFailureStrategy(new FailingUponPendingStep())
                .useStoryLoader(storyLoader)
                .useStoryReporterBuilder(storyReporterBuilder);
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        return new SpringStepsFactory(configuration(), context).createCandidateSteps();
    }

}
