package jibe.tools.bdd.core.core;

import jibe.tools.bdd.api.DescriptiveType;
import jibe.tools.bdd.api.Execution;
import jibe.tools.bdd.api.ExecutionsHolder;
import jibe.tools.bdd.api.Scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class DefaultScenario implements Scenario {

    private final List<ExecutionsHolder> executionHolders = new ArrayList<>();
    private String description;

    public DefaultScenario(String description) {
        this.description = description;
    }

    @Override
    public List<ExecutionsHolder> executionHolders() {
        return Collections.unmodifiableList(executionHolders);
    }

    @Override
    public Scenario given(String description) {
        return given(description, new Execution[0]);
    }

    @Override
    public Scenario given(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(this, DescriptiveType.Given, description, executions));
        return this;
    }

    @Override
    public Scenario and() {
        return this;
    }

    @Override
    public Scenario when(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(this, DescriptiveType.When, description, executions));
        return this;
    }

    @Override
    public Scenario then(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(this, DescriptiveType.Then, description, executions));
        return this;
    }

    private void add(ExecutionsHolder executionHolder) {
        executionHolders.add(executionHolder);
    }

    @Override
    public DescriptiveType descriptiveType() {
        return DescriptiveType.Scenario;
    }

    @Override
    public String describe() {
        String documentation = String.format("%s: %s\n", descriptiveType(), description);
        StringBuilder sb = new StringBuilder(documentation);
        for (ExecutionsHolder executionsHolder : executionHolders) {
            sb.append(executionsHolder.describe());
        }
        return sb.toString();
    }
}
