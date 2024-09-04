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
        //Ignores whitespace
        String nextCommand = fileReader.nextLine().trim().replace(" ", "");
        //Ignores commented lines and empty lines
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
        //Checks if the command has a dest part and returns null if not
        return currentCommand.contains("=") ? currentCommand.split("=")[0] : null;
    }

    public String jump() throws UnsupportedOperationException {
        if(commandType() != CommandType.C_COMMAND) throw new UnsupportedOperationException("A and L Commands don't have jump!");
        //Checks if the command has a jump part and returns null if not
        return currentCommand.contains(";") ? currentCommand.split(";")[1] : null;
    }

    /*Because not all commands have a dest or jump
    this method returns the middle comp part no matter if they are present or not and also avoids if checks*/
    public String comp() {
        //-1+1 if dest part is not present which returns the whole string, else gets everything after the dest part
        String toReturn = currentCommand.substring(currentCommand.indexOf("=")+1);
        //Gets everything before the beginning of the jump part signaled by ;
        return toReturn.split(";")[0];
    }

    @Override
    public void close() throws Exception {
        fileReader.close();
    }
}
