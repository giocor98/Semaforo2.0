package app;

import connection.Connection;

/**
 * Class to manage the program life
 */
public class MainApp {

    /**
     * main Method to launch the program and to
     * create all the environment needed for the
     * normal program flow.
     *
     * @param args (list of arguments passed to the program).
     */
    public static void main(String[] args) {

        //foo instruction
        System.out.println("Hello World");

    }

    public static void end(){
        Connection.closeAll();
        System.exit(0);
    }
}
