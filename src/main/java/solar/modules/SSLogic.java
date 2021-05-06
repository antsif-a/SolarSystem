package solar.modules;

import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.type.*;
import solar.*;

import java.lang.reflect.*;

/**
 * Solar System Logic
 */
public class SSLogic implements ModListener {
    private Location location;

    @Override
    public void init() {
        location = Location.getLocation();
    }

    @Override
    public void update() {
        try {
            // ew reflect
            Method m = Universe.class.getDeclaredMethod("updatePlanet", Planet.class);
            m.setAccessible(true);
            m.invoke(Vars.universe, Location.solarSystem.mainPlanet);
            // see Universe#updateGlobal
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setLocation(Location l) {
        Reflect.set(Vars.renderer.planets, "solarSystem", l.mainPlanet);
        location = l;
    }

    public Location getLocation() {
        return location;
    }
}
