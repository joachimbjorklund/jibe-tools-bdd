package jibe.tools.bdd.api;

import jibe.tools.bdd.core.core.DefaultStory;
import jibe.tools.bdd.core.core.StoryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 *
 */
public class BDDApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BDDApiTest.class);

    @Test
    public void testSimple() throws Exception {

        String storyDescription = "Webadmin authentication";

        Story story = newStory(storyDescription)
                .inOrderTo("use the webadmin")
                .asA("admin user")
                .iWantTo("have the system secured by a 2 step login procedure");

        Execution registerOperator = new Execution() {
            @Override
            public void execute(ExecutionContext executionContext) {
                LOGGER.info("register operator");
                executionContext.put("operatorId", 1);
            }
        };

        Execution registerAdminUser = new Execution() {
            @Override
            public void execute(ExecutionContext executionContext) {
                LOGGER.info("register admin user: " + executionContext.get("operatorId"));
            }
        };

        Execution noopExecution = new Execution() {
            @Override
            public void execute(ExecutionContext executionContext) {

            }
        };

        story
                .scenario("AdminUser successfully logs in to webadmin")
                .given("An operator is registred the system", registerOperator)
                .given("An admin user is registred the system", registerAdminUser)
                .and()
                .when("The admin user goes to the login page", noopExecution)
                .when("providing user credentials", noopExecution)
                .then("a mail should be sent with a one time security code")

        ;

        LOGGER.info(story.describe());

        StoryRunner storyRunner = new StoryRunner(story);
        storyRunner.run();
    }

    private Story newStory(String description) {
        return new DefaultStory(description);
    }
}
