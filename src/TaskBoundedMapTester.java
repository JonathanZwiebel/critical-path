import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 * Tests the TaskBoundedMap, TaskBuilder and Task classes
 */
public class TaskBoundedMapTester {
    public static void main(String[] args) {
        TaskBoundedMap map = new TaskBoundedMap();
        spaghettiTest(map);

        map.link();
        map.forwardPass();
        map.backwardPass();
        map.changeTime("Prepare Egg Sauce", 25);

        RenderWindow rw = new RenderWindow();
        VideoMode desktop = VideoMode.getDesktopMode();

        rw.create(new VideoMode(3 * desktop.width / 4, 3 * desktop.height / 4, 3 * desktop.bitsPerPixel / 4), "Task Bounded Map Display Test");
        //rw.create(new VideoMode(desktop.width, desktop.height, desktop.bitsPerPixel), "Task Bounded Map Display Test",  RenderWindow.FULLSCREEN);
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

    public static void customTest(TaskBoundedMap project) {
        project.put("A", 4, new String[]{}, new String[]{"B", "C"});
        project.put("B", 7, new String[]{"A"}, new String[]{"E"});
        project.put("C", 2, new String[]{"A"}, new String[]{"D"});
        project.put("D", 16, new String[]{"C"}, new String[]{"E"});
        project.put("E", 1.5f, new String[]{"B", "D"}, new String[]{});
    }

    // http://www.lindsay-sherwin.co.uk/project_framework/images/cpa_spaghetti.jpg
    public static void spaghettiTest(TaskBoundedMap project) {
        project.put("Get Ingredients", 5, new String[]{}, new String[]{"Cook Spaghetti", "Prepare Egg Sauce", "Cook Bacon"});
        project.put("Cook Spaghetti", 10, new String[]{"Get Ingredients"}, new String[]{"Drain Spaghetti"});
        project.put("Prepare Egg Sauce", 4, new String[]{"Get Ingredients"}, new String[]{"Complete Sauce"});
        project.put("Cook Bacon", 6, new String[]{"Get Ingredients"}, new String[]{"Complete Sauce"});
        project.put("Drain Spaghetti", 3, new String[]{"Cook Spaghetti"}, new String[]{"Mix Sauce and Spaghetti"});
        project.put("Complete Sauce", 5, new String[]{"Prepare Egg Sauce", "Cook Bacon"}, new String[]{"Mix Sauce and Spaghetti"});
        project.put("Mix Sauce and Spaghetti", 7, new String[]{"Drain Spaghetti", "Complete Sauce"}, new String[]{});
    }
}
