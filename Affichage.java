
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class Affichage{
    public static void main(String[] args) throws IOException {
        //RawConsoleInput input = new RawConsoleInput();
        String key = "";
        while(!key.equals("UP")) {
            key = getKeyCode();
            System.out.println(key);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        System.out.println("----> "+sc.nextLine());
    }
    public static String getKeyCode() {
        try {
            int code = RawConsoleInput.read(true);
            while (!(code == 13 || code == 32)) { //32=SPACE, 13=ENTER
                //System.out.println(code);
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
                    case 57416: //windows
                        return "UP";
                    case 57419:
                        return "LEFT";
                    case 57421:
                        return "RIGHT";
                    case 57424:
                        return "DOWN";
                    case 27:
                        break; //pour enlever les codes envoyés avant les flèches (sur linux)
                    case 91:
                        break;
                    case 10:
                        return "ENTER";
                    //noinspection ConstantConditions
                    case 3:
                        System.exit(0);
                    case 32:
                        return "ENTER";
                    default:
                        System.out.println("Espace ou Entrée pour valider, et pour se déplacer");
                        System.out.print("flèches ou ZQSD >");
                        System.out.println(code);

                }
                code = RawConsoleInput.read(true);
            }
            RawConsoleInput.resetConsoleMode();
            return "ENTER";
        } catch (IOException e) {
            System.out.println("Erreur interne");
            return "ERROR";
        }
    }

}
