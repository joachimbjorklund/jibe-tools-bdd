package jibe.tools.bdd.api;

import java.util.List;

/**
 *
 */
public interface Scenario extends Descriptive {
    public List<ExecutionsHolder> executionHolders();

    Scenario given(String description);

    Scenario given(String description, Execution... execution);

    Scenario and();

    Scenario when(String description, Execution... executions);

    Scenario then(String description, Execution... executions);

    Scenario eventually(String description, Execution... executions);
}
