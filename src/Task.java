import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class models a single dependency-based task with a constant completion time
 */
public class Task {
    // TODO: Rename future
    // TODO: Probably don't use HashMap and ArrayLists together
    private HashMap<Task, Float> dependencies_start_map_, future_end_map_;
    public ArrayList<Task> dependencies, future;
    public float early_start, late_start, early_end, late_end, time;
    public float float_time;
    private boolean forward_passed_, backward_passed;

    public Task(float time, ArrayList dependencies, ArrayList future) {
        assert time >= 0;

        forward_passed_ = false;
        backward_passed = false;
        this.dependencies = dependencies;
        this.future = future;
        this.time = time;
    }

    public void forward_notify(Task notifier, float early_start) {
        assert early_start >= 0;

        dependencies_start_map_.put(notifier, early_start);
    }

    public void forward_pass() {
        assert dependencies_start_map_.size() == dependencies.size();

        early_start = (float) dependencies_start_map_.values().toArray()[0];
        assert early_start >= 0;

        early_end = early_start + time;

        for(Task t : future) {
            t.forward_notify(this, early_end);
        }
    }
}
