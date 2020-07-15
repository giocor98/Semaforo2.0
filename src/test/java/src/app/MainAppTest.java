package src.app;

import exceptions.TestException;
import org.junit.jupiter.api.Test;
import src.utils.exception.NotSuchPropertiesException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.AppProperty;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the main class. It'll test if the
 * program is able to boot in a proper way.
 */
class MainAppTest {

    @Test
    void main() {
        String[] validParameters = {
          "--gui",
          "--cli",

          "--debug",

          "--view:$view",
          "--log:$log",

          "--lang:*"
        };

        String[] validView = {
                "gui",
                "cli"
        };

        String[] validLog = {
                "trace",
                "debug",
                "warn",
                "error",
                "fatal"
        };

        //Test that the AppProperty can be built
        assertTrue(AppProperty.build());

        //Check that all the default parameters are valid
        String[] defaultParams = new String[0];
        try {
            defaultParams = AppProperty.getProperty("defaultParam.param").split(" ");
        } catch (NotSuchPropertiesException | PropertyLoadException e) {
            e.printStackTrace();
            fail();
        }
        for(String param: defaultParams){
            boolean ok = false;
            for(String validParam: validParameters){
                if(validParam.contains(":") && param.contains(":")){
                    String[] params = param.replace(":", " ").split(" ");
                    if(params[0].equals(validParam.replace(":", " ").split(" ")[0])){
                        switch(validParam.replace(":", " ").split(" ")[1]){
                            case "$view":
                                assertTrue(Arrays.asList(validView).contains(params[1]));
                                ok = true;
                                break;
                            case"$log":
                                assertTrue(Arrays.asList(validLog).contains(params[1]));
                                ok = true;
                                break;
                            case "*":
                                assertTrue(true);
                                ok = true;
                                break;
                            default:
                                ok = false;
                                fail();
                                break;
                        }
                    }
                }else if(validParam.equals(param)){
                    assertTrue(true);
                    ok = true;
                }
                if(ok)
                    break;
            }
            assertTrue(ok);
        }

        //Checks that the default value of "defaultParam.viewer" is one of the ones valid
        try {
            assertTrue(Arrays.asList(validView).contains(AppProperty.getProperty("defaultParam.viewer")));
        } catch (NotSuchPropertiesException | PropertyLoadException e) {
            System.err.println("Error retrieving defaultParam.viewer");
            e.printStackTrace();
            fail();
        }

        //Checks that the default value of "defaultParam.lang" exists
        try {
            AppProperty.getProperty("defaultParam.lang");
        } catch (NotSuchPropertiesException | PropertyLoadException e) {
            System.err.println("Error retrieving defaultParam.lang");
            e.printStackTrace();
            fail();
        }

        //Setting the MainApp.end not to stop the program but to throw an exception
        MainApp.setEndingCall(()->{
            throw new TestException();
        });



        //Asserts main will not work if filled with a random parameter for log.
        /*boolean excepted = false;
        String[] x = {"--view:foo"};
        try{
            MainApp.main(x);
        }catch (TestException e){
            excepted = true;
        }
        assertTrue(excepted);*/
    }
}