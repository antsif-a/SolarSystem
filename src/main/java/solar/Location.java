package solar;

import arc.*;
import arc.func.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;

public enum Location {
    def("default", Planets.sun),
    solarSystem("solarsystem", SolarSystemPlanets.sun);

    // todo remove
    public static void init() {
        def.mainPlanet = Planets.sun;
        solarSystem.mainPlanet = SolarSystemPlanets.sun;
    }

    public String name;
    public String localizedName;
    public Planet mainPlanet;

    Location(String name, Planet mainPlanet) {
        this.name = name;
        this.localizedName = Core.bundle.get("location." + name + ".name");
        this.mainPlanet = mainPlanet;
    }

    // public void updateAccessible() {
    //     mainPlanet.accessible = false;
    //     getPlanets().each(c -> c.accessible = true);
    //     Location.each(l -> {
    //         if (l != this) {
    //             l.mainPlanet.accessible = false;
    //             l.getPlanets().each(c -> c.accessible = false);
    //         }
    //     });
    // }

    public Seq<Planet> getPlanets() {
        return mainPlanet.children;
    }

    public static Location getLocation() {
        for (Location l : values()) {
            if (l.mainPlanet == Vars.renderer.planets.solarSystem) {
                return l;
            }
        }

        return null;
    }

    public static void each(Cons<Location> cons) {
        for (Location l : values()) {
            cons.get(l);
        }
    }
}
