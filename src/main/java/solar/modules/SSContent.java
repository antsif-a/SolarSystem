package solar.modules;

import arc.struct.*;
import mindustry.ctype.*;
import solar.*;

/**
 * Solar System Content
 */
public class SSContent implements ModListener {
    private Seq<ContentList> content = Seq.with(
        new SolarSystemPlanets()
    );

    @Override
    public void init() {

    }

    @Override
    public void loadContent() {
        content.each(ContentList::load);
        Location.init();
    }
}
