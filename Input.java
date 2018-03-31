import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.Reader;

public class Input {
    public Reader reader;
    public Terminal terminal;
    public int curX;
    public int curY;

    public Input() throws IOException {
        this.curX = 0;
        this.curY = 0;
        this.terminal = TerminalBuilder.builder()
                .jna(true)
                .system(true)
                .build();

// raw mode means we get keypresses rather than line buffered input
        this.terminal.enterRawMode();
        this.reader = terminal.reader();
    }

    public void close() throws IOException {
        this.reader.close();
        this.terminal.close();
    }

    public String getKeyCode() {
        try {
            int code = reader.read();
            while (code != 13) { //32=SPACE
                switch (code) {
                    case 65:
                        return "DOWN";
                    case 68:
                        return "LEFT";
                    case 67:
                        return "RIGHT";
                    case 66:
                        return "UP";
                        case 122:
                        return "DOWN";
                    case 113:
                        return "LEFT";
                    case 100:
                        return "RIGHT";
                    case 115:
                        return "UP";
                    case 27: break; //pour enlever les codes envoyés avant les flèches (sur linux)
                    case 91: break;
                    case 32: return "SPACE";
                    default:
                        System.out.println("Use arrow keys >");

                }
                code = reader.read();
            }
            return "ENTER";
        } catch (IOException e) {
            System.out.println("Erreur interne");
            return "ERROR";
        }
    }
    public int[] getPos(int taille, String key){
        switch (key){
            case "UP":
                if (this.curY<taille-1)
                    this.curY +=1;
                break;
            case "RIGHT":
                if(this.curX<taille-1)
                    curX+=1;
                break;
            case "LEFT":
                if(curX>0)
                    this.curX-=1;
                break;
            case "DOWN":
                if(curY>0)
                    this.curY-=1;
                break;
        }
        int[] pos = {this.curX, this.curY};
        System.out.println(curX+" "+curY);
        return pos;
    }
}
