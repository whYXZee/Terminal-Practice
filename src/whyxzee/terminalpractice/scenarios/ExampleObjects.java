package whyxzee.terminalpractice.scenarios;

public class ExampleObjects {
    public String plural = "";
    public String corresponding = "";
    public String singular = "";
    public String correspondingSingle = "";

    public ExampleObjects() {

    }

    public void randomizeObject() {
        corresponding = "positions";
        correspondingSingle = "position";
        switch (ScenarioConstants.rng.nextInt(3)) {
            case 0:
                plural = "people";
                singular = "person";
                switch (ScenarioConstants.rng.nextInt(2)) {
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
}
