package solar;

import arc.*;
import arc.struct.*;
import mindustry.game.*;
import mindustry.mod.*;
import solar.modules.*;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Solar_System">Solar System</a>
 */
@SuppressWarnings("unused")
public class SolarSystem extends Mod {
    private final Seq<ModListener> listeners = new Seq<>();

    public static SSLogic logic;

    public SolarSystem() {
        add(new SSContent());
        add(new SSUI());
        add(logic = new SSLogic());
    }

    private void add(ModListener listener) {
        listeners.add(listener);

        Events.on(EventType.ClientLoadEvent.class, (e) -> {
            Core.app.addListener(new ApplicationListener() {
                @Override
                public void update() {
                    listener.update();
                }
            });
        });
    }

    @Override
    public void init() {
        listeners.each(ModListener::init);
    }

    @Override
    public void loadContent() {
        listeners.each(ModListener::loadContent);
    }
}
