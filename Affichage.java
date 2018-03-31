import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.Reader;

public class Affichage{
    public void test() throws IOException {
    Terminal terminal = TerminalBuilder.builder()
            .jna(true)
            .system(true)
            .build();

// raw mode means we get keypresses rather than line buffered input
        terminal.enterRawMode();
    Reader reader = terminal.reader();
    System.out.println(reader.read());
    int read = reader.read();
        while (read != 13) {
            String sortie = this.arrowTest(read);
            if (sortie.length()>0)
                    System.out.println(sortie);
        read = reader.read();
    }
        reader.close();
        terminal.close();
    }
    public String arrowTest(int code){
        switch (code){
            case 65: return "UP";
            case 68: return "LEFT";
            case 67: return "RIGHT";
            case 66: return "Execute Order 66 (DOWN)";
            default: return ""+code;
        }
    }
}
