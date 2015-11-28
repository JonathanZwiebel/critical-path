package mains;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import taskmap.TaskMap;

/**
 * Tests the taskmap.TaskMap, taskmap.TaskBuilder and taskmap.Task classes
 */
public class TaskMapTest {
    public static void main(String[] args) {
        TaskMap map = new TaskMap();
        spaghettiTest(map);
        map.crit();

        RenderWindow rw = new RenderWindow();
        VideoMode desktop = VideoMode.getDesktopMode();

        rw.create(new VideoMode(3 * desktop.width / 4, 3 * desktop.height / 4, 3 * desktop.bitsPerPixel / 4), "taskmap.Task Bounded Map Display Test");
        //rw.create(new VideoMode(desktop.width, desktop.height, desktop.bitsPerPixel), "taskmap.Task Bounded Map Display Test",  RenderWindow.FULLSCREEN);
        rw.setFramerateLimit(30);


        while(rw.isOpen()) {
            rw.clear(Color.BLACK);
            map.draw(rw, RenderStates.DEFAULT);
            rw.display();

            for(Event event : rw.pollEvents()) {
                switch(event.type) {
                    case CLOSED:
                        rw.close();
                        break;
                    case KEY_RELEASED:
                        if(event.asKeyEvent().key == Keyboard.Key.Q) {
                            rw.close();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // http://www.lindsay-sherwin.co.uk/project_framework/images/cpa_spaghetti.jpg
    public static void spaghettiTest(TaskMap project) {
        project.put("Get Ingredients", 5);
        project.put("Cook Spaghetti", 10);
        project.put("Prepare Egg Sauce", 4);
        project.put("Cook Bacon", 6);
        project.put("Drain Spaghetti", 3);
        project.put("Complete Sauce", 5);
        project.put("Mix Sauce and Spaghetti", 7);

        project.link("Get Ingredients", "Cook Spaghetti");
        project.link("Get Ingredients", "Prepare Egg Sauce");
        project.link("Get Ingredients", "Cook Bacon");
        project.link("Cook Spaghetti", "Drain Spaghetti");
        project.link("Prepare Egg Sauce", "Complete Sauce");
        project.link("Cook Bacon", "Complete Sauce");
        project.link("Drain Spaghetti", "Mix Sauce and Spaghetti");
        project.link("Complete Sauce", "Mix Sauce and Spaghetti");
    }
}
