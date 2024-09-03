import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    private final Scanner fileReader;
    private String currentCommand;
    public Parser(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        fileReader = new Scanner(file);
    }

    public boolean hasMoreCommands() {
        return fileReader.hasNextLine();
    }

    public void advance(){
        String nextCommand = fileReader.nextLine();
        while(nextCommand.startsWith("//") || nextCommand.isBlank()){
            nextCommand = fileReader.nextLine();
        }
        currentCommand = nextCommand;
    }

    public CommandType commandType(){
        if (currentCommand.startsWith("@")) return CommandType.A_COMMAND;
        if (currentCommand.startsWith("(")) return CommandType.L_COMMAND;
        return CommandType.C_COMMAND;
    }

    public String symbol() throws Exception {
        if (commandType() == CommandType.C_COMMAND) throw new Exception("C Commands don't have a symbol!");
        return currentCommand.replaceAll("[()@]", "");
    }

    public String dest() throws Exception {
        if(commandType() != CommandType.C_COMMAND) throw new Exception("A and L Commands don't have dest!");
        if(currentCommand.contains("=")) return currentCommand.split("=")[0];
        return null;
    }

    public String jump() throws Exception {
        if(commandType() != CommandType.C_COMMAND) throw new Exception("A and L Commands don't have jump!");
        if(currentCommand.contains(";")) return currentCommand.split(";")[1];
        return null;
    }

    public String comp() throws Exception {
        String jump = jump();
        String dest = dest();
        return currentCommand.replaceAll(String.format("%s|%s|;|=", jump, dest), "");
    }


}
