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
        Iterator<ExecutionsHolder> it = getSortedExecutionHolders(scenario).iterator();
        while (it.hasNext()) {
            ExecutionsHolder executionsHolder = it.next();
            Iterator<Execution> it2 = executionsHolder.executions().iterator();
            Execution execution = null;
            while (it2.hasNext()) {
                try {
                    execution = it2.next();
                    execute(execution);
                } catch (Exception e) {
                    execution.exception(e);
                    continueOnException(e, executionsHolder, it, it2);
                }
            }
        }
    }

    private void continueOnException(Exception reThrowException, ExecutionsHolder currentHolder, Iterator<ExecutionsHolder> executionaHolderIterator,
            Iterator<Execution> executionIterator) {
        if (currentHolder.descriptiveType().equals(DescriptiveType.Eventually)) {
            while (executionIterator.hasNext()) {
                Execution execution = null;
                try {
                    execution = executionIterator.next();
                    execute(execution);
                } catch (Exception e) {
                    execution.exception(e);
                    LOGGER.error(e.getMessage());
                }
            }
        }
        while (executionaHolderIterator.hasNext()) {
            ExecutionsHolder executionsHolder = executionaHolderIterator.next();
            if (!executionsHolder.descriptiveType().equals(DescriptiveType.Eventually)) {
                continue;
            }

            Iterator<Execution> it2 = executionsHolder.executions().iterator();
            Execution execution = null;
            while (it2.hasNext()) {
                try {
                    execution = it2.next();
                    execute(execution);
                } catch (Exception e) {
                    execution.exception(e);
                    LOGGER.error(e.getMessage());
                }
            }
        }
        throw Throwables.propagate(reThrowException);
    }

    private List<ExecutionsHolder> getSortedExecutionHolders(Scenario scenario) {
        List<ExecutionsHolder> answer = new ArrayList<>();
        List<ExecutionsHolder> eventuallys = new ArrayList<>();
        for (ExecutionsHolder eh : scenario.executionHolders()) {
            if (eh.descriptiveType().equals(DescriptiveType.Eventually)) {
                eventuallys.add(eh);
            } else {
                answer.add(eh);
            }
        }
        answer.addAll(eventuallys);
        return answer;
    }

    public ExecutionContext executionContext() {
        return executionContext;
    }

    private void execute(Execution execution) {
        LOGGER.info(execution.describe());
        execution.preExecute(executionContext);
        Object value = execution.execute(executionContext);
        if (execution instanceof Assertion) {
            assertExecutionValue(value, execution.getClass().getName());
        }
        if (value != null) {
            executionContext.put(value);
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
