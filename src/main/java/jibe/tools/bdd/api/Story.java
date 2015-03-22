package jibe.tools.bdd.api;

import java.util.List;

/**
 *
 */
public interface Story extends Descriptive {
    List<Scenario> scenarios();

    Scenario scenario(String description);

    Story inOrderTo(String inOrderTo);

    Story asA(String asA);

    Story iWantTo(String iWantTo);
}
