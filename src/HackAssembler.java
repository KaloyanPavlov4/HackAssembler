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
        while (parser.hasMoreCommands()) {
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

    private void populateSymbolTable() throws Exception {
        while (parser.hasMoreCommands()) {
            parser.advance();
            if (parser.commandType() == CommandType.L_COMMAND) {
                symbolTable.addEntry(parser.symbol(), parser.getLine());
            }
        }
        parser = new Parser(fileName);
    }
}
