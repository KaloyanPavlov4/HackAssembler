import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser implements AutoCloseable{
    private final Scanner fileReader;

    private String currentCommand;

    private int line;

    public Parser(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        fileReader = new Scanner(file);
    }

    public boolean hasMoreCommands() {
        return fileReader.hasNextLine();
    }

    public int getLine() {
        return line;
    }

    public void advance(){
        String nextCommand = fileReader.nextLine().trim().replace(" ", "");
        while(nextCommand.startsWith("//") || nextCommand.isBlank()){
            nextCommand = fileReader.nextLine().trim().replace(" ", "");
        }
        currentCommand = nextCommand;
        if (commandType() != CommandType.L_COMMAND) line++;
    }

    public CommandType commandType(){
        if (currentCommand.startsWith("@")) return CommandType.A_COMMAND;
        if (currentCommand.startsWith("(")) return CommandType.L_COMMAND;
        return CommandType.C_COMMAND;
    }

    public String symbol() throws UnsupportedOperationException {
        if (commandType() == CommandType.C_COMMAND) throw new UnsupportedOperationException("C Commands don't have a symbol!");
        return currentCommand.replaceAll("[()@]", "");
    }

    public String dest() throws UnsupportedOperationException {
        if(commandType() != CommandType.C_COMMAND) throw new UnsupportedOperationException("A and L Commands don't have dest!");
        return currentCommand.contains("=") ? currentCommand.split("=")[0] : null;
    }

    public String jump() throws UnsupportedOperationException {
        if(commandType() != CommandType.C_COMMAND) throw new UnsupportedOperationException("A and L Commands don't have jump!");
        return currentCommand.contains(";") ? currentCommand.split(";")[1] : null;
    }

    public String comp() {
        String toReturn = currentCommand.substring(currentCommand.indexOf("=")+1);
        return toReturn.split(";")[0];
    }

    @Override
    public void close() throws Exception {
        fileReader.close();
    }
}
