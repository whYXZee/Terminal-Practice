package whyxzee.terminalpractice.scenarios;

import java.util.Random;

public class ExampleObjects {
    public String plural = "";
    public String corresponding = "";
    public String singular = "";
    public String correspondingSingle = "";

    private static final Random rng = new Random();

    public ExampleObjects() {

    }

    public void rngObjectCombinatoric() {
        corresponding = "positions";
        correspondingSingle = "position";
        switch (rng.nextInt(3)) {
            case 0:
                plural = "people";
                singular = "person";
                switch (rng.nextInt(2)) {
                    case 0:
                        corresponding = "seats";
                        correspondingSingle = "seat";
                        break;
                    case 1:
                        break;
                }
                break;
            case 1:
                plural = "dice";
                singular = "die";
                break;
            case 2:
                plural = "letters";
                singular = "letter";
                break;
        }
    }

    public void rngObjectForce() {
        corresponding = "";
        correspondingSingle = "";
        switch (rng.nextInt(3)) {
            case 0:
                plural = "dice";
                singular = " die";
                break;
            case 1:
                plural = "boxes";
                singular = " box";
                break;
            case 2:
                plural = "oranges";
                singular = "n orange";
        }
    }
}
