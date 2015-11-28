import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

/**
 * This class handles the display of a TaskBoundedMap by delegating each Task to draw itself
 *
 * TODO[Major]: Allow for drawing before linkage
 * TODO: Decide if this makes more sense as a private class
 * @author Jonathan Zwiebel
 * @version November 27th, 2015
 */
public class TaskBoundedMapDisplay implements Drawable {
    private TaskBoundedMap map_;

    /**
     * Constructs a TaskBoundedMapDisplay object that displays a single TaskBoundedMap
     * Called by a TaskBoundedMap
     * @param map
     */
    public TaskBoundedMapDisplay(TaskBoundedMap map) {
        map_ = map;
    }

    /**
     * Calls on each task to draw
     * @param target RenderTarget on which to draw
     * @param states RenderStates normally left as default
     */
    public void draw(RenderTarget target, RenderStates states) {
        if(map_.linked) {
            for(TaskBuilder builder : map_.getMapping().values()) {
                builder.task.draw(target, states);
            }
        }
    }
}
