package src.view.cli;

import src.view.View;

import java.util.List;
import java.util.Scanner;

/**
 * Class implementing <code>View</code> for the CLI.
 *
 * @see View
 */
public class CLI implements View {
    @Override
    public String selectPort(List<String> portList) {
        //create the scanner
        Scanner scanner = new Scanner(System.in);
        int ret = -1;

        //printing the options
        System.out.println("Select a port");
        System.out.println("-1\t--NO PORT");
        System.out.println("0\t--REFRESH");
        for(int i=0; i<portList.size(); i++){
            System.out.println((i+1) + "\t" + portList.get(i));
        }

        //reading the answer
        System.out.println("\nEnter port number: ");
        ret = scanner.nextInt()-1;

        //checking if answer was "-1" => return null
        if(ret == -2)
            return null;

        //Checking if the answer was 0 or a not valid one => returns ""
        if(ret<0||ret>=portList.size())
            return "";

        //returns the selected port.
        return portList.get(ret);
    }

    @Override
    public void error(String errorMessage) {
        System.err.println(errorMessage);
    }
}
