package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * This is a controller for the starting menu UI
 *
 * @author Moeka Nakane
 */
public class MainMenuController {

  @FXML private Label mainMenuLabel;

  @FXML private Button gameStartButton;

  /**
   * when the gameStartButton is clicked, the UI displayed to the user is switched to the canvas UI
   *
   * @param event when the gameStartButton is clicked, this event occurs.
   */
  @FXML
  private void onSwitchToCanvas(ActionEvent event) {

    // Get the clicked button and scene where the button is in
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();

    try {
      // Switch to canvas UI
      sceneButtonIsIn.setRoot(App.loadFxml("canvas"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
