import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ConsoleClearExample {
    public static void main(String[] args) {
        clearConsole();
        System.out.println("Console cleared!");
    }

    private static void clearConsole() {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
