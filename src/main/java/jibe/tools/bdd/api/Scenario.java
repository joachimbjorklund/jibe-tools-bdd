package jibe.tools.bdd.api;

import java.util.List;

/**
 *
 */
public interface Scenario extends Descriptive {
    public List<ExecutionsHolder> executionHolders();

    Scenario given(String description);

    Scenario given(String description, Execution... execution);

    Scenario given(ExecutionsHolder... executionsHolder);

    Scenario and();

    Scenario when(String description, Execution... executions);

    Scenario when(ExecutionsHolder... executionsHolder);

    Scenario then(String description, Execution... executions);

    Scenario then(ExecutionsHolder... executionsHolder);

    Scenario eventually(String description, Execution... executions);

    Scenario eventually(ExecutionsHolder... executionsHolder);
}
