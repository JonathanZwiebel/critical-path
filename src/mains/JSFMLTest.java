package mains;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 * Created by Jonathan Zwiebel
 */
public class JSFMLTest {
    public static void main(String args[]) {
        RenderWindow rw = new RenderWindow();
        rw.create(new VideoMode(1000, 1000), "Render Window");
        rw.setFramerateLimit(30);

        Color color = Color.CYAN;

        while(rw.isOpen()) {
            rw.clear(color);

            rw.display();

            for(Event event : rw.pollEvents()) {
                switch(event.type) {
                    case CLOSED:
                        rw.close();
                        break;
                    case MOUSE_BUTTON_PRESSED:
                        color = Color.GREEN;
                        break;
                    case MOUSE_BUTTON_RELEASED:
                        color = Color.CYAN;
                        break;
                    case MOUSE_LEFT:
                        color = Color.CYAN;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
