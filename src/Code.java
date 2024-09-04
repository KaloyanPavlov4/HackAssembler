import java.util.Map;

public class Code {
    //All the different possibilities for the comp part of a C command and their binary variants
    private final Map<String, String> comp = Map.ofEntries(Map.entry("0", "0101010"), Map.entry("1", "0111111"), Map.entry("-1","0111010"),
            Map.entry("D", "0001100"), Map.entry("A", "0110000"), Map.entry("!D", "0001101"), Map.entry("!A", "110001"),
            Map.entry("-D", "0001111"), Map.entry("-A", "0110011"), Map.entry("D+1", "0011111"), Map.entry("A+1","0110111"),
            Map.entry("D-1","0001110"),Map.entry("A-1","0110010"), Map.entry("D+A","0000010"), Map.entry("D-A","0010011"),
            Map.entry("A-D","0000111"), Map.entry("D&A","0000000"), Map.entry("D|A","0010101"), Map.entry("M","1110000"),
            Map.entry("!M","1110001"), Map.entry("-M","1110011"), Map.entry("M+1","1110111"), Map.entry("M-1","1110010"),
            Map.entry("D+M","1000010"), Map.entry("D-M","1010011"), Map.entry("M-D","1000111"), Map.entry("D&M","1000000"),
            Map.entry("D|M", "1010101"));

    //All the different possibilities for the dest part of a C command and their binary variants except for null as Map.ofEntries does not support null key (null check in method)
    private final Map<String, String> dest = Map.ofEntries(Map.entry("M","001"), Map.entry("D","010"), Map.entry("MD","011"),
            Map.entry("A","100"), Map.entry("AM","101"), Map.entry("AD","110"), Map.entry("AMD","111"));

    //All the different possibilities for the jump part of a C command and their binary variants except for null as Map.ofEntries does not support null key (null check in method)
    private final Map<String, String> jump = Map.ofEntries(Map.entry("JGT","001"), Map.entry("JEQ","010"),Map.entry("JGE","011"),
            Map.entry("JLT","100"),Map.entry("JNE","101"),Map.entry("JLE","110"),Map.entry("JMP","111"));

    private String comp(String comp){
        return this.comp.get(comp);
    }

    private String dest(String dest){
        if(dest == null) return "000";
        return this.dest.get(dest);
    }

    private String jump(String jump){
        if(jump == null) return "000";
        return this.jump.get(jump);
    }

    public String c_command(String comp, String dest, String jump){
        return "111" + comp(comp) + dest(dest) + jump(jump);
    }

    public String commandToLocation(int location){
        return Integer.toBinaryString(0x10000 | location).substring(1);
    }
}
