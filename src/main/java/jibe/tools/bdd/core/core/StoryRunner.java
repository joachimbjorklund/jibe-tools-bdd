package jibe.tools.bdd.core.core;

import jibe.tools.bdd.api.Execution;
import jibe.tools.bdd.api.ExecutionContext;
import jibe.tools.bdd.api.ExecutionsHolder;
import jibe.tools.bdd.api.Scenario;
import jibe.tools.bdd.api.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 *
 */
public class StoryRunner implements ExecutionContext {

    private final Logger LOGGER = LoggerFactory.getLogger(StoryRunner.class);

    private final Story story;
    private final ExecutionContext executionContext;

    public StoryRunner(Story story) {
        this(story, new DefaultExecutionContext());
    }

    public StoryRunner(Story story, ExecutionContext executionContext) {
        this.story = story;
        this.executionContext = executionContext;
    }

    public void run() {
        for (Scenario scenario : story.scenarios()) {
            runScenario(scenario);
        }
    }

    private void runScenario(Scenario scenario) {
        for (ExecutionsHolder executionsHolder : scenario.executionHolders()) {
            LOGGER.info("executionsHolder: " + executionsHolder.describe());
            execute(executionsHolder.executions());
        }
    }

    private void execute(Iterable<Execution> executions) {
        for (Execution execution : executions) {
            execution.execute(this);
        }
    }

    @Override
    public Object get(String key) {
        return executionContext.get(key);
    }

    @Override
    public void put(String key, Object value) {
        executionContext.put(key, value);
    }

    private static class DefaultExecutionContext implements ExecutionContext {
        private final HashMap<String, Object> ctx = new HashMap<>();

        @Override
        public Object get(String key) {
            return ctx.get(key);
        }

        @Override
        public void put(String key, Object value) {
            ctx.put(key, value);
        }
    }
}
