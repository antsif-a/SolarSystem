package solar.modules;

import arc.*;
import arc.input.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import solar.*;

import java.lang.reflect.*;

import static mindustry.Vars.renderer;
import static mindustry.ui.dialogs.PlanetDialog.Mode.select;

/**
 * Solar System UI
 */
public class SSUI implements ModListener {
    @Override
    public void init() {
        Vars.ui.planet.shown(this::setupPlanetDialog);
    }

    private void setupPlanetDialog() {
        // aaaaaaaaaaaaaaaaa
        PlanetDialog dialog = Vars.ui.planet;
        dialog.zoom = dialog.planets.zoom = 1f;
        dialog.selectAlpha = 1f;
        Vars.ui.minimapfrag.hide();

        dialog.clearChildren();

        dialog.margin(0f);

        dialog.stack(
                new Element(){
                    {
                        //add listener to the background rect, so it doesn't get unnecessary touch input
                        addListener(new ElementGestureListener(){
                            @Override
                            public void tap(InputEvent event, float x, float y, int count, KeyCode button){
                                if(dialog.newPresets.any()) return;

                                if(dialog.hovered != null && dialog.selected == dialog.hovered && count == 2){
                                    // dialog.playSelected();
                                    // anuke why
                                    try {
                                        Method m = PlanetDialog.class.getDeclaredMethod("playSelected");
                                        m.setAccessible(true);
                                        m.invoke(dialog);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }

                                boolean canSelect = false;
                                if (dialog.hovered != null) {
                                    try {
                                        // :(
                                        Method m = PlanetDialog.class.getDeclaredMethod("canSelect", Sector.class);
                                        m.setAccessible(true);
                                        canSelect = (boolean) m.invoke(dialog, dialog.hovered);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(dialog.hovered != null && (/*dialog.canSelect(dialog.hovered)*/canSelect || PlanetDialog.debugSelect)){
                                    dialog.selected = dialog.hovered;
                                }

                                if(dialog.selected != null){
                                    // dialog.updateSelected();
                                    try {
                                        // >:|
                                        Method m = PlanetDialog.class.getDeclaredMethod("updateSelected");
                                        m.setAccessible(true);
                                        m.invoke(dialog);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void draw(){
                        dialog.planets.orbitAlpha = dialog.selectAlpha;
                        dialog.planets.render(dialog);
                        if(Core.scene.getDialog() == dialog){
                            Core.scene.setScrollFocus(dialog);
                        }
                    }
                },
                //info text
                new Table(t -> {
                    t.touchable = Touchable.disabled;
                    t.top();
                    t.label(() -> dialog.mode == select ? "@sectors.select" : "").style(Styles.outlineLabel).color(Pal.accent);
                }),
                dialog.buttons,
                //planet selection
                //edited to update location
                new Table(t -> {
                    t.right();
                    // :eyes:
                    t.update(() -> {
                        t.clearChildren();
                        if(Vars.content.planets().count(p -> p.accessible) > 1){
                            t.table(Styles.black6, pt -> {
                                pt.add("@planets").color(Pal.accent);
                                pt.row();
                                pt.image().growX().height(4f).pad(6f).color(Pal.accent);
                                pt.row();
                                // for-i is bad
                                for(Planet planet : SolarSystem.logic.getLocation().getPlanets()){
                                    if(planet.accessible){
                                        pt.button(planet.localizedName, Styles.clearTogglet, () -> {
                                            dialog.selected = null;
                                            dialog.launchSector = null;
                                            renderer.planets.planet = planet;
                                        }).width(200).height(40).growX().update(bb -> bb.setChecked(renderer.planets.planet == planet));
                                        pt.row();
                                    }
                                }
                            });
                        }
                    });
                }),
                // location selection
                new Table(t -> {
                    t.left();
                    t.table(Styles.black6, pt -> {
                        pt.add("@location").color(Pal.accent);
                        pt.row();
                        pt.image().growX().height(4f).pad(6f).color(Pal.accent);
                        pt.row();
                        Location.each(l -> {
                            pt.button(l.localizedName, Styles.clearTogglet, () -> {
                                dialog.selected = null;
                                dialog.launchSector = null;
                                SolarSystem.logic.setLocation(l);
                                Planet planet = l.getPlanets().copy().filter(p -> p.accessible).first();
                                if (planet != null) {
                                    Vars.renderer.planets.planet = planet;
                                }
                                dialog.fire(new VisibilityEvent(false)); // re-render planet selection
                            }).width(200).height(40).growX().update(b -> b.setChecked(Location.getLocation() == l));

                            pt.row();
                        });
                    });
                })).grow();

        // hope it works
    }

    // private void setupPlanetDialog() {
    //     PlanetDialog dialog = Vars.ui.planet;
    //     Stack stack = (Stack) dialog.getChildren().first();
    //
    //     stack.add(new Table(t -> {
    //         t.left();
    //         t.table(Styles.black6, pt -> {
    //             pt.add("@location").color(Pal.accent);
    //             pt.row();
    //             pt.image().growX().height(4f).pad(6f).color(Pal.accent);
    //             pt.row();
    //
    //             Location.each(l -> {
    //                 pt.button(l.localizedName, Styles.clearTogglet, () -> {
    //                     dialog.selected = null;
    //                     dialog.launchSector = null;
    //                     SolarSystem.logic.setLocation(l);
    //                     dialog.fire(new VisibilityEvent(false)); // re-render planet selection
    //                     // Vars.renderer.planets.planet = l.getPlanets().first();
    //                 }).width(200).height(40)
    //                   .growX()
    //                   .update(b -> b.setChecked(Location.getLocation() == l));
    //
    //                 pt.row();
    //             });
    //         });
    //     }));
    // }
}
