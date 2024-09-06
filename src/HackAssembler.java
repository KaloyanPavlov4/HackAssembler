import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class HackAssembler {
    private static final String FILE_EXTENSION = ".hack";

    private static final String FILE_SPLIT_REGEX = "\\.";

    private final SymbolTable symbolTable;

    private final Code code;

    private Parser parser;

    private final String fileName;

    private final StringBuilder output;

    public HackAssembler(String fileName) throws Exception {
        output = new StringBuilder();
        symbolTable = new SymbolTable();
        code = new Code();
        this.fileName = fileName;
        parser = new Parser(fileName);
    }

    public void assemble() throws Exception {
        populateSymbolTable();
        processCommands();
        writeOutput();
    }

    private void processCommands() throws Exception {
        while (parser.hasMoreLines()) {
            parser.advance();
            switch (parser.commandType()) {
                case C_COMMAND:
                    processCCommand(parser.comp(), parser.dest(), parser.jump());
                    break;
                case A_COMMAND:
                    processACommand(parser.symbol());
                    break;
            }
        }
    }

    private void processCCommand(String comp, String dest, String jump) {
        output.append(code.c_command(comp, dest, jump));
        output.append(System.lineSeparator());
    }

    private void processACommand(String symbol) {
        if (symbolTable.contains(symbol)) {
            output.append(code.commandToLocation(symbolTable.getAddress(symbol)));
        } else {
            //If the A command symbol is not a number it's a variable that needs to be translated into its location or added to the symbol table if new
            try {
                int location = Integer.parseInt(symbol);
                output.append(code.commandToLocation(location));
            } catch (NumberFormatException e) {
                symbolTable.addEntry(symbol);
                output.append(code.commandToLocation(symbolTable.getAddress(symbol)));
            }
        }
        output.append(System.lineSeparator());
    }

    private void writeOutput() {
        try (Writer writer = new FileWriter(fileName.split(FILE_SPLIT_REGEX)[0] + FILE_EXTENSION)) {
            writer.append(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //First pass of the program that adds all the labels pointing to lines of the code used for jumps
    private void populateSymbolTable() throws Exception {
        while (parser.hasMoreLines()) {
            parser.advance();
            if (parser.commandType() == CommandType.L_COMMAND) {
                symbolTable.addEntry(parser.symbol(), parser.getLine());
            }
        }
        parser = new Parser(fileName);
    }
}
