import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

/**
 * @author Jonathan Zwiebel
 * @version November 27th, 2015
 */
public class TaskBoundedMapDisplay implements Drawable {
    private TaskBoundedMap map_;

    public TaskBoundedMapDisplay(TaskBoundedMap map) {
        map_ = map;
    }

    public void draw(RenderTarget target, RenderStates states) {
        if(map_.linked) {
            for(TaskLinker linker : map_.getMapping().values()) {
                linker.task.draw(target, states);
            }
        }
    }
}
