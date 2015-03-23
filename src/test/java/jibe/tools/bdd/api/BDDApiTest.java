package jibe.tools.bdd.api;

import jibe.tools.bdd.core.DefaultStory;
import jibe.tools.bdd.core.StoryRunner;
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

        Execution registerOperator = new AbstractExecution() {
            @Override
            public Object execute(ExecutionContext ctx) {
                LOGGER.info("register operator");
                ctx.put("operatorId", 1);
                return null;
            }
        };

        Execution registerAdminUser = new AbstractExecution() {
            @Override
            public Object execute(ExecutionContext ctx) {
                LOGGER.info("register admin user: " + ctx.get("operatorId"));
                return null;
            }
        };

        Execution noopExecution = new AbstractExecution() {
            @Override
            public Object execute(ExecutionContext ctx) {
                return null;
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
