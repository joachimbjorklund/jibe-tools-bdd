package jibe.tools.bdd.core.core;

import com.google.common.collect.Lists;
import jibe.tools.bdd.api.DescriptiveType;
import jibe.tools.bdd.api.Execution;
import jibe.tools.bdd.api.ExecutionsHolder;
import jibe.tools.bdd.api.Scenario;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DefaultExecutionsHolder implements ExecutionsHolder {
    private final Scenario scenario;
    private final String description;
    private final List<Execution> executions;
    private final DescriptiveType descriptiveType;

    public DefaultExecutionsHolder(Scenario scenario, DescriptiveType descriptiveType, String description, Execution... executions) {
        this.scenario = scenario;
        this.descriptiveType = descriptiveType;
        this.description = description;
        this.executions = Lists.newArrayList(executions);
    }

    @Override
    public List<Execution> executions() {
        return Collections.unmodifiableList(executions);
    }

    @Override
    public DescriptiveType descriptiveType() {
        return descriptiveType;
    }

    @Override
    public String describe() {
        return String.format("%s: %s\n", descriptiveType, description);
    }
}
