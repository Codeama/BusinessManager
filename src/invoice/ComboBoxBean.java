/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoice;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;

/**
 *
 * @author BUKOLA
 */
public class ComboBoxBean {

    private SimpleObjectProperty<ComboBox<String>> actions;

    public ComboBoxBean(ComboBox<String> actions) {
        this.actions = new SimpleObjectProperty(actions);
    }
    
    public ComboBox<String> getActions() {
        return actions.get();
    }
    
    public SimpleObjectProperty<ComboBox<String>> getActionsProperty() {
        return actions;
    }

    public void setActions(ComboBox<String> actions) {
        this.actions.set(actions);
    }
    
}
