import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * This class is to be constructed and maintained by a TaskObject. This display a single Task.
 *
 * @author Jonathan Zwiebel
 * @version November 27th, 2015
 */
public class TaskDisplay implements Drawable {
    //TODO: Remove this
    static int counter = 0;
    private int id;
    private Task task_;

    public TaskDisplay(Task task) {
        id = counter;
        counter++;
        task_ = task;
    }

    public void draw(RenderTarget target, RenderStates state) {
        RectangleShape early_rect = new RectangleShape(new Vector2f(task_.getTime() * 20, 20));
        early_rect.setPosition(task_.getEarlyStart() * 20, id * 25);

        if(task_.getFloatTime() == 0) {
            early_rect.setFillColor(Color.BLUE);
        }
        else {
            early_rect.setFillColor(Color.CYAN);
        }

        RectangleShape late_rect = new RectangleShape(new Vector2f(task_.getFloatTime() * 20, 20));
        late_rect.setPosition(task_.getEarlyEnd() * 20, id * 25);
        late_rect.setFillColor(Color.GREEN);

        target.draw(early_rect);
        target.draw(late_rect);
    }
}
