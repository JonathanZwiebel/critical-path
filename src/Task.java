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
    private boolean forward_passed_, backward_passed_;

    public Task(float time, ArrayList dependencies, ArrayList future) {
        assert time >= 0;

        forward_passed_ = false;
        backward_passed_ = false;
        this.dependencies = dependencies;
        this.future = future;
        this.time = time;
    }

    public void forward_notify(Task notifier, float early_start) {
        assert early_start >= 0;
        assert !forward_passed_ && !backward_passed_;
        dependencies_start_map_.put(notifier, early_start);
    }

    public void forward_pass() {
        assert !forward_passed_ && !backward_passed_;
        // TODO: Assert that the Tasks completely match, not just the size
        assert dependencies_start_map_.size() == dependencies.size();
        forward_passed_ = true;
        // TODO: Ensure that this is the edge value
        early_start = (float) dependencies_start_map_.values().toArray()[0];
        assert early_start >= 0;
        early_end = early_start + time;
        for(Task t : future) {
            t.forward_notify(this, early_end);
        }
    }

    public void backward_notify(Task notifier, float late_end) {
        assert forward_passed_ && !backward_passed_;
        assert late_end >= 0;
        future_end_map_.put(notifier, late_end);
    }

    public void backward_pass() {
        assert forward_passed_ && !backward_passed_;
        assert future_end_map_.size() == future.size();
        backward_passed_ = true;
        late_end = (float) future_end_map_.values().toArray()[0];
        assert late_end >= 0;
        late_start = late_end - time;
        assert late_start >= 0;
        for(Task t : dependencies) {
            t.backward_notify(this, late_start);
        }
    }
}
