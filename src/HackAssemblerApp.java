public class HackAssemblerApp {
    public static void main(String[] args) throws Exception {
        HackAssembler hackAssembler = new HackAssembler(args[0]);
        hackAssembler.assemble();
    }
}