package display;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import taskmap.Task;

/**
 * This class is to be constructed and maintained by a TaskObject. This display a single taskmap.Task.
 *
 * @author Jonathan Zwiebel
 * @version November 27th, 2015
 */
public class TaskDisplay implements Drawable {
    //TODO: Remove this
    private static int counter = 0;
    private final int id;
    private final Task task_;

    private static final int UNIT_LENGTH = 20;
    private static final int UNIT_WIDTH = 20;
    private static final int UNIT_WIDTH_SPACING = 10;

    public TaskDisplay(Task task) {
        id = counter;
        counter++;
        task_ = task;
    }

    public void draw(RenderTarget target, RenderStates state) {
        RectangleShape early_rect = new RectangleShape(new Vector2f(task_.getTime() * UNIT_LENGTH, UNIT_WIDTH));
        early_rect.setPosition(task_.getEarlyStart() * UNIT_LENGTH, id * (UNIT_WIDTH + UNIT_WIDTH_SPACING));

        if(task_.getFloatTime() == 0) {
            early_rect.setFillColor(Color.BLUE);
        }
        else {
            early_rect.setFillColor(Color.CYAN);
        }

        RectangleShape late_rect = new RectangleShape(new Vector2f(task_.getFloatTime() * UNIT_LENGTH, UNIT_WIDTH));
        late_rect.setPosition(task_.getEarlyEnd() * UNIT_LENGTH, id * (UNIT_WIDTH + UNIT_WIDTH_SPACING));
        late_rect.setFillColor(Color.GREEN);

        target.draw(early_rect);
        target.draw(late_rect);
    }
}
