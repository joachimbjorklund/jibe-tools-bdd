package jibe.tools.bdd.core;

import com.google.common.base.Throwables;
import jibe.tools.bdd.api.Assertion;
import jibe.tools.bdd.api.DescriptiveType;
import jibe.tools.bdd.api.Execution;
import jibe.tools.bdd.api.ExecutionContext;
import jibe.tools.bdd.api.ExecutionsHolder;
import jibe.tools.bdd.api.Scenario;
import jibe.tools.bdd.api.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class StoryRunner {

    private static final String LAST_EXECUTION_VALUE_KEY = ".lastExecutionValue";

    private final Logger LOGGER = LoggerFactory.getLogger(StoryRunner.class);

    private final Story story;
    private final ExecutionContext executionContext;
    private final List<Execution> executed = new ArrayList<>();

    public StoryRunner(Story story) {
        this(story, DefaultExecutionContext.builder().build());
    }

    public StoryRunner(Story story, ExecutionContext executionContext) {
        this.story = story;
        this.executionContext = executionContext;
    }

    public Story getStory() {
        return story;
    }

    public void run() {
        for (Scenario scenario : story.scenarios()) {
            run(scenario);
        }
    }

    public void run(Scenario scenario) {
        Iterator<ExecutionsHolder> it = scenario.executionHolders().iterator();
        Throwable reThrow = null;
        while (it.hasNext()) {
            ExecutionsHolder executionsHolder = it.next();
            Iterator<Execution> it2 = executionsHolder.executions().iterator();
            while (it2.hasNext()) {
                try {
                    Execution execution = it2.next();
                    if ((reThrow != null) && !executionsHolder.descriptiveType().equals(DescriptiveType.Eventually)) {
                        continue;
                    }
                    execute(execution);
                } catch (Throwable e) {
                    if (reThrow == null) {
                        reThrow = e;
                    }
                }
            }
        }

        if (reThrow != null) {
            throw Throwables.propagate(reThrow);
        }
    }

    public ExecutionContext executionContext() {
        return executionContext;
    }

    private void execute(Execution execution) {
        execution.preExecute(executionContext);
        Object value = execution.execute(executionContext);
        if (execution instanceof Assertion) {
            assertExecutionValue(value, execution.getClass().getName());
        }
        if (value != null) {
            executionContext.put(execution.getClass().getName() + LAST_EXECUTION_VALUE_KEY, value);
        }
        execution.postExecute(executionContext);
        executed.add(execution);
    }

    private void assertExecutionValue(Object value, String msg) {
        if (value == null || ((value instanceof Boolean) && !(Boolean) value)) {
            throw new AssertionError(msg);
        }
    }
}
