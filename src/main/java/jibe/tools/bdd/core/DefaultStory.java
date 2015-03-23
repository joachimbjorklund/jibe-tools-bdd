package jibe.tools.bdd.core;

import jibe.tools.bdd.api.DescriptiveType;
import jibe.tools.bdd.api.Scenario;
import jibe.tools.bdd.api.Story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DefaultStory implements Story {
    private final String description;
    private final List<Scenario> scenarios = new ArrayList<>();
    private String inOrderTo;
    private String asA;
    private String iWantTo;

    public DefaultStory(String description) {
        this.description = description;
    }

    @Override
    public List<Scenario> scenarios() {
        return Collections.unmodifiableList(scenarios);
    }

    @Override
    public Scenario scenario(String description) {
        return add(new DefaultScenario(description));
    }

    @Override
    public Story inOrderTo(String inOrderTo) {
        this.inOrderTo = inOrderTo;
        return this;
    }

    @Override
    public Story asA(String asA) {
        this.asA = asA;
        return this;
    }

    @Override
    public Story iWantTo(String iWantTo) {
        this.iWantTo = iWantTo;
        return this;
    }

    private Scenario add(Scenario scenario) {
        scenarios.add(scenario);
        return scenario;
    }

    @Override
    public DescriptiveType descriptiveType() {
        return DescriptiveType.Story;
    }

    @Override
    public String describe() {
        String documentation = String.format("Story: %s\nIn order to: %s\nAs a: %s\nI want to: %s\n", description, inOrderTo, asA, iWantTo);
        StringBuilder sb = new StringBuilder(documentation);
        for (Scenario scenario : scenarios) {
            sb.append("\n");
            sb.append(scenario.describe());
        }
        return sb.toString();
    }
}
