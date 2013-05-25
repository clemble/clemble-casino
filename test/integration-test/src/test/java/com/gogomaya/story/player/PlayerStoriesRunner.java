package com.gogomaya.story.player;

import java.util.List;

import javax.inject.Inject;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.failures.RethrowingFailure;
import org.jbehave.core.io.LoadFromRelativeFile;
import org.jbehave.core.io.StoryLoader;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.StepFinder;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.spring.integration.TestConfiguration;
import com.google.common.collect.ImmutableList;
import com.stresstest.jbehave.context.aop.StoryContextConverter;
import com.stresstest.jbehave.context.configuration.StoryContextSpringStepsFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class})
public class PlayerStoriesRunner extends JUnitStories {

    @Inject
    public ApplicationContext applicationContext;

    @Inject
    public StoryContextConverter storyContextConverter;

    @Override
    protected List<String> storyPaths() {
        return ImmutableList.<String>of("stories/player/playerCreation.story");
    }

    @Override
    public Configuration configuration() {
        StoryLoader storyLoader = new LoadFromRelativeFile(getClass().getResource("/"));

        StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder()
            .withFailureTrace(true)
            .withReporters(new ConsoleOutput());

        return new MostUsefulConfiguration()
            .usePendingStepStrategy(new FailingUponPendingStep())
            .useFailureStrategy(new RethrowingFailure())
            .useStoryLoader(storyLoader)
            .useStepFinder(new StepFinder(new StepFinder.ByLevenshteinDistance()))
            .useParameterConverters(new ParameterConverters().addConverters(storyContextConverter))
            .useStoryReporterBuilder(storyReporterBuilder);
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        List<CandidateSteps> candidateSteps = new StoryContextSpringStepsFactory(configuration(), applicationContext).createCandidateSteps();
        return candidateSteps;
    }

}
