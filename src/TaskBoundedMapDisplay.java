import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Draws a TaskBoundedMap
 * TODO[MAJOR]: Make it draw linkage
 * TODO[MAJOR]: Make force directed
 */
public class TaskBoundedMapDisplay implements Drawable {
    private TaskBoundedMap map_;


    public TaskBoundedMapDisplay (TaskBoundedMap map) {
        map_ = map;
    }

    // TODO: Implement
    public void draw(RenderTarget target, RenderStates states) {
        System.out.println("Task Bounded Map Display draw called");
        RectangleShape rect = new RectangleShape(new Vector2f(150, 75));
        rect.setPosition(500, 500);
        rect.setFillColor(Color.MAGENTA);
        target.draw(rect);
    }
}
