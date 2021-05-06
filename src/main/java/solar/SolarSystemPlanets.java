package solar;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.*;
import mindustry.type.*;

/**
 * @see <a href="https://en.wikipedia.org/wiki/List_of_Solar_System_objects_by_size">Solar System Objects by Size</a>
 * @see <a href="https://en.wikipedia.org/wiki/Astronomical_unit">Astronomical Unit</a>
 */
public class SolarSystemPlanets implements ContentList {
    public static SolarSystemPlanet sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune;

    @Override
    public void load() {
        sun = new SolarSystemPlanet("sun", null, 2) {{
            bloom = true;

            meshLoader = () -> new SunMesh(
                    this, 4,
                    5, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ff7a38"),
                    Color.valueOf("ff9638"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffe371"),
                    Color.valueOf("f4ee8e")
            );
        }};

        mercury = new SolarSystemPlanet("mercury", 1) {{
            generator = new SerpuloPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            atmosphereColor = Color.valueOf("3c1b8f");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
        }};
    }

    public static class SolarSystemPlanet extends Planet {
        private static final int sectorSize = 3;

        public SolarSystemPlanet(String name, float radius) {
            this(name, (SolarSystemPlanet) Location.solarSystem.mainPlanet, radius);
        }

        /**
         * @param name name of the planet
         * @param parent parent planet
         * @param radius planet radius
         */
        public SolarSystemPlanet(String name, SolarSystemPlanet parent, float radius) {
            super(name, parent, sectorSize, radius);

            // if (distance != 0) {
                // orbitRadius = distance * 10; todo custom orbit radius
            // }
        }

        /**
         * f i x e d   i n   v 7
         * @see <a href="https://github.com/Anuken/Mindustry/pull/5202">Anuken/Mindustry#5202</a>
         */
        @Override
        public Sector getLastSector() {
            if (sectors.isEmpty()) {
                return null;
            }

            return super.getLastSector();
        }
    }
}
