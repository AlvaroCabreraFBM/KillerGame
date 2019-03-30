package killergame;

import java.awt.geom.Area;
import java.util.ArrayList;

public class KillerRules {

    // Methods
    public static boolean testCollision(VisualObject object, ArrayList<VisualObject> objects) {

        boolean result = false;

        for (int inc = 0; inc < objects.size(); inc++) {
            try {
                VisualObject objectToTest = objects.get(inc);
                if (!object.equals(objectToTest)) {
                    Area area = object.getArea();
                    area.intersect(objectToTest.getArea());
                    if (!area.isEmpty()) {
                        result = true;
                        objectToTest.collision();
                    }
                }
            } catch (NullPointerException error) {
            }
        }

        return result;
    }

}
