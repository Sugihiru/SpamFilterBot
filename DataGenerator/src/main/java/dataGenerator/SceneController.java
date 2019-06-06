package dataGenerator;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class SceneController
{
    private HashMap<String, Parent> screenMap = new HashMap<>();
    private Scene main;

    public SceneController(Scene main) {
        this.main = main;
    }

    protected void add(String name, Parent node){
        screenMap.put(name, node);
    }

    protected void remove(String name){
        screenMap.remove(name);
    }

    protected void activate(String name){
        main.setRoot(screenMap.get(name));
    }
}
