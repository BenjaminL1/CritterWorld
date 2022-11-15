package parser;

import controller.Controller;
import controller.ControllerFactory;
import org.junit.jupiter.api.Test;

public class ControllerTest
{
    @Test
    public void testLoadCritter()
    {
        Controller controller = ControllerFactory.getConsoleController();
        controller.loadWorld("src\\test\\resources\\A5files\\small_world", true, false);
    }
}
