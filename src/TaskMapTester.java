/**
 * Tests the TaskMap, TaskLinker and Task classes
 */
public class TaskMapTester {
    public static void main(String[] args) {
        TaskMap project = new TaskMap();
        spaghettiTest(project);

        project.link();
        project.forwardPass();
        project.backwarPass();

        for(TaskLinker linker : project.mapping.values()) {
            System.out.print(linker.task);
        }
    }

    public static void customTest(TaskMap project) {
        project.put("A", 4, new String[]{}, new String[]{"B", "C"});
        project.put("B", 7, new String[]{"A"}, new String[]{"E"});
        project.put("C", 2, new String[]{"A"}, new String[]{"D"});
        project.put("D", 16, new String[]{"C"}, new String[]{"E"});
        project.put("E", 1.5f, new String[]{"B", "D"}, new String[]{});
    }

    // http://www.lindsay-sherwin.co.uk/project_framework/images/cpa_spaghetti.jpg
    public static void spaghettiTest(TaskMap project) {
        project.put("Get Ingredients", 5, new String[]{}, new String[]{"Cook Spaghetti", "Prepare Egg Sauce", "Cook Bacon"});
        project.put("Cook Spaghetti", 10, new String[]{"Get Ingredients"}, new String[]{"Drain Spaghetti"});
        project.put("Prepare Egg Sauce", 4, new String[]{"Get Ingredients"}, new String[]{"Complete Sauce"});
        project.put("Cook Bacon", 6, new String[]{"Get Ingredients"}, new String[]{"Complete Space"});
        project.put("Drain Spaghetti", 3, new String[]{"Cook Spaghetti"}, new String[]{"Mix Sauce and Spaghetti"});
        project.put("Complete Sauce", 3, new String[]{"Prepare Egg Sauce", "Cook Bacon"}, new String[]{"Mix Sauce and Spaghetti"});
        project.put("Mix Sauce and Spaghetti", 3, new String[]{"Drain Spaghetti", "Complete Sauce"}, new String[]{});
    }
}
