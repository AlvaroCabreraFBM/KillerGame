package killergame;

import java.util.ArrayList;

public class KillerAction {

    // Methods
    public static boolean moveUp(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveUp();
            result = true;
        }

        return result;

    }

    public static boolean moveUpLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveUpLeft();
            result = true;
        }

        return result;

    }

    public static boolean moveUpRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveUpRight();
            result = true;
        }

        return result;

    }

    public static boolean moveLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveLeft();
            result = true;
        }

        return result;

    }

    public static boolean moveRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveRight();
            result = true;
        }

        return result;

    }

    public static boolean moveDown(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveDown();
            result = true;
        }

        return result;

    }

    public static boolean moveDownLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveDownLeft();
            result = true;
        }

        return result;

    }

    public static boolean moveDownRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.moveDownRight();
            result = true;
        }

        return result;

    }

    public static boolean stop(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.stop();
            result = true;
        }

        return result;

    }

    public static boolean shootUp(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootUp();
            result = true;
        }

        return result;

    }

    public static boolean shootUpLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootUpLeft();
            result = true;
        }

        return result;

    }

    public static boolean shootUpRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootUpRight();
            result = true;
        }

        return result;

    }

    public static boolean shootLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootLeft();
            result = true;
        }

        return result;

    }

    public static boolean shootRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootRight();
            result = true;
        }

        return result;

    }

    public static boolean shootDown(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootDown();
            result = true;
        }

        return result;

    }

    public static boolean shootDownLeft(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootDownRight();
            result = true;
        }

        return result;

    }

    public static boolean shootDownRight(String id, ArrayList<VisualObject> objects) {

        boolean result = false;

        KillerShip ship = KillerAction.searchShip(id, objects);
        if (ship != null && !ship.isSended()) {
            ship.shootDownRight();
            result = true;
        }

        return result;

    }

    public static KillerShip searchShip(String id, ArrayList<VisualObject> objects) {

        KillerShip ship = null;

        try {
            for (int inc = 0; inc < objects.size(); inc++) {
                if (objects.get(inc) instanceof KillerShip) {
                    KillerShip shipToTest = (KillerShip) objects.get(inc);
                    if (shipToTest.getId().equals(id)) {
                        ship = shipToTest;
                    }
                }
            }
        } catch (Exception error) {

        }

        return ship;

    }

}
