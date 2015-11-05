import java.util.ArrayList;

/**
 * Used to test the Task class's ability to perform a forward pass
 */
public class PassTester {
    public static void main(String[] args) {
        Task a = new Task("a", 5, null, null);
        ArrayList<Task> list_a = new ArrayList();
        list_a.add(a);
        Task b = new Task("b", 3, list_a, null);
        // TODO: Tasks can not actually be constructed now in a dependency tree, fix this
    }
}
