package jibe.tools.bdd.core;

import com.google.common.collect.Lists;
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
        add(new DefaultExecutionsHolder(DescriptiveType.Given, description, executions));
        return this;
    }

    @Override
    public Scenario given(ExecutionsHolder... executionsHolders) {
        add(executionsHolders);
        return this;
    }

    @Override
    public Scenario and() {
        return this;
    }

    @Override
    public Scenario when(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(DescriptiveType.When, description, executions));
        return this;
    }

    @Override
    public Scenario when(ExecutionsHolder... executionsHolders) {
        add(executionsHolders);
        return this;
    }

    @Override
    public Scenario then(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(DescriptiveType.Then, description, executions));
        return this;
    }

    @Override
    public Scenario then(ExecutionsHolder... executionsHolders) {
        add(executionsHolders);
        return this;
    }

    @Override
    public Scenario eventually(String description, Execution... executions) {
        add(new DefaultExecutionsHolder(DescriptiveType.Eventually, description, executions));
        return this;
    }

    @Override
    public Scenario eventually(ExecutionsHolder... executionsHolders) {
        add(executionsHolders);
        return this;
    }

    private void add(ExecutionsHolder... executionHolders) {
        this.executionHolders.addAll(Lists.newArrayList(executionHolders));
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
