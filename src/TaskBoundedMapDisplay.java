import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

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
        target.draw(new RectangleShape());
    }
}
