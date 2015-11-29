package display;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import taskmap.TaskMap;
import taskmap.TaskBuilder;

/**
 * This class handles the display of a taskmap.TaskMap by delegating each taskmap.Task to draw itself
 *
 * TODO[Major]: Allow for drawing before linkage
 * TODO: Decide if this makes more sense as a private class
 * @author Jonathan Zwiebel
 * @version November 27th, 2015
 */
public class TaskMapDisplay implements Drawable {
    private final TaskMap map_;

    /**
     * Constructs a display.TaskMapDisplay object that displays a single taskmap.TaskMap
     * Called by a taskmap.TaskMap
     * @param map TaskMap that this display draws
     */
    public TaskMapDisplay(TaskMap map) {
        map_ = map;
    }

    /**
     * Calls on each task to draw
     * @param target RenderTarget on which to draw
     * @param states RenderStates normally left as default
     */
    public void draw(RenderTarget target, RenderStates states) {
        for(TaskBuilder builder : map_.getMapping().values()) {
            builder.task.draw(target, states);
        }
    }
}
