package nz.ac.auckland.se206;

import static nz.ac.auckland.se206.ml.DoodlePrediction.printPredictions;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

  @FXML private Canvas canvas;

  @FXML private Label wordLabel;

  @FXML private Label timerLabel;

  @FXML private Label nextCategoryLabel;

  @FXML private Button timerButton;

  @FXML private Button saveButton;

  @FXML private ListView<String> guessList;

  @FXML private Button penEraserButton;

  @FXML private Button clearButton;

  private ObservableList<String> items;
  private GraphicsContext graphic;
  private DoodlePrediction model;
  private String currentWord;
  private CategorySelector categorySelector;
  private String result;
  private boolean isPen;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException
   * @throws CsvException
   */
  public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {
    graphic = canvas.getGraphicsContext2D();
    model = new DoodlePrediction();

    // Select a random category (difficulty -> easy)
    categorySelector = new CategorySelector();
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);
    wordLabel.setText(randomWord);
    currentWord = randomWord;

    // disable the drawing
    canvas.setDisable(true);

    // disable the save button
    saveButton.setDisable(true);

    // disable the clear button and penEraser button
    clearButton.setDisable(true);
    penEraserButton.setDisable(true);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * This method executes when the user clicks the "Predict" button. It gets the current drawing,
   * queries the DL model and prints on the console the top 5 predictions of the DL model and the
   * elapsed time of the prediction in milliseconds.
   *
   * <p>Currently, the game screen doesn't have the predict button -> but automatically called by
   * the background thread every second.
   *
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  @FXML
  private boolean onPredict() throws TranslateException {

    final long start = System.currentTimeMillis();

    // Get the top3 guesses made by the DL model
    List<Classification> predictionResults = model.getPredictions(getCurrentSnapshot(), 3);
    printPredictions(predictionResults);

    // Check if the current category is in the list
    boolean result = isWin(predictionResults);

    // If so, the user wins
    if (result) {
      System.out.println("WIN");
    } else {
      System.out.println("LOST");
    }
    System.out.println("prediction performed in " + (System.currentTimeMillis() - start) + " ms");

    return result;
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Save the current snapshot on a bitmap file.
   *
   * <p>Not used
   *
   * @return The file of the saved image.
   * @throws IOException If the image cannot be saved.
   */
  private File saveCurrentSnapshotOnFile() throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File("tmp");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }

    // We save the image to a file in the tmp folder.
    final File imageToClassify =
        new File(tmpFolder.getName() + "/snapshot" + System.currentTimeMillis() + ".bmp");

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "bmp", imageToClassify);

    return imageToClassify;
  }

  /**
   * Gets called when the user clicks the save button.
   *
   * @param event ActionEvent that occurs when the user clicks the save button
   * @throws IOException
   */
  @FXML
  public void onSave(ActionEvent event) throws IOException {

    // Get the stage where the source of the event is at
    Button button = (Button) event.getSource();
    Stage stage = (Stage) button.getScene().getWindow();

    // Let the user choose the location of the file to be saved and the file name
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP Files", "*.bmp"));
    fileChooser.setTitle("save your drawing:)");
    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      // Save the image to a file.
      ImageIO.write(getCurrentSnapshot(), "bmp", file);
    }
  }

  /**
   * Determines whether the category is guessed correctly from the user's drawing by the DL model
   *
   * @param classifications list of classification which indicates the guesses of the DL model
   * @return if the user won .i.e. the category is guessed correctly from the user's drawing by the
   *     DL model
   */
  private boolean isWin(List<Classification> classifications) {

    // Go through the list of the guesses made by the DL model
    for (Classification classification : classifications) {

      // Remove the "_" if there is any to an empty space
      String guess = classification.getClassName().replace("_", " ");

      // If the category is in the list, the user wins
      if (guess.equals(currentWord)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Invoked when the timer start button is clicked by the user. When the button is clicked, the
   * timer counter starts counting down from 60 and updates the list of top10 guesses by the DL
   * model every second.
   *
   * <p>If the user could not get the DL model guess correctly when the time is up, the user loses.
   */
  @FXML
  private void onStartTimer() {

    // disable the timer start button
    timerButton.setDisable(true);
    // Clear the canvas
    onClear();
    // clear the next category label
    nextCategoryLabel.setText("");
    // update the current category
    wordLabel.setText(currentWord);
    // disable the save button
    saveButton.setDisable(true);
    // enable the drawing
    canvas.setDisable(false);
    // reset the timer label
    timerLabel.setStyle("-fx-text-fill: white");
    timerLabel.setText("⏳");
    // enable the drawing by setting it to the pen mode
    isPen = false;
    onSwitchPenEraser();
    // enable the clear button and penEraser button
    clearButton.setDisable(false);
    penEraserButton.setDisable(false);

    // When the user clicks the button, it says "Quick, Draw!"
    Task<Void> startSpeechTask =
        new Task<Void>() {
          @Override
          protected Void call() {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("quick, draw!");
            return null;
          }
        };

    // TimerTask every second
    Timer timer = new Timer();
    TimerTask timerTask =
        new TimerTask() {
          private int seconds = 60;

          @Override
          @FXML
          public void run() {
            seconds--;
            // run every second
            Platform.runLater(
                () -> {

                  // Display the remaining time - if its less than 10 seconds, make it red
                  if (seconds < 10) {
                    timerLabel.setStyle("-fx-text-fill: #d15015");
                  } else {
                    timerLabel.setStyle("-fx-text-fill: white");
                  }
                  timerLabel.setText(String.valueOf(seconds));

                  // get 10 guesses and display them on the UI
                  try {
                    updateGuessList();
                  } catch (TranslateException e1) {
                    e1.printStackTrace();
                  }

                  try {
                    if (onPredict()) {
                      result = "You WON!!!!";
                      endGame();
                      timer.cancel();
                    }
                  } catch (TranslateException e) {
                    e.printStackTrace();
                  }

                  if (seconds == 0) {
                    result = "You LOST....";
                    endGame();
                    timer.cancel();
                  }
                });
          }
        };
    // Run the task above every second
    timer.scheduleAtFixedRate(timerTask, 1000, 1000);

    // Get the background thread started on the speech task
    Thread startSpeechThread = new Thread(startSpeechTask);
    startSpeechThread.start();
  }

  /**
   * Updates the list of top10 guesses by the DL model every second.
   *
   * @throws TranslateException
   */
  @FXML
  private void updateGuessList() throws TranslateException {
    items = FXCollections.observableArrayList();

    // Get the DL model guesses
    List<Classification> predictionResults = model.getPredictions(getCurrentSnapshot(), 10);
    for (Classification guess : predictionResults) {
      String guessName = guess.getClassName();
      items.add(guessName);
    }

    // Display the guesses on the UI
    guessList.setItems(items);
  }

  /** Invoked either when the user gets the DL model guess correctly or when the time is up. */
  @FXML
  private void endGame() {

    // Display the result in red
    timerLabel.setStyle("-fx-text-fill: #d15015");
    timerLabel.setText(result);

    // Disable the pen/eraser
    canvas.setOnMouseDragged(e -> {});

    // Disable the clear button and penEraser button
    clearButton.setDisable(true);
    penEraserButton.setDisable(true);

    // disable the drawing
    canvas.setDisable(true);

    // Text to speech the result
    Task<Void> resultSpeechTask =
        new Task<Void>() {
          @Override
          protected Void call() {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak(result);
            return null;
          }
        };
    Thread resultSpeechThread = new Thread(resultSpeechTask);
    resultSpeechThread.start();

    // enable the save drawing button
    saveButton.setDisable(false);

    // display a next category
    String randomWord = categorySelector.getRandomCategory(Difficulty.E);
    nextCategoryLabel.setText("Next category: " + randomWord);
    currentWord = randomWord;

    // enable the timer start button
    timerButton.setDisable(false);
  }

  /** Switch between pen and eraser */
  @FXML
  private void onSwitchPenEraser() {

    final double size;

    // make it a pen
    if (!isPen) {
      size = 5.0;
      penEraserButton.setText("Eraser");

      canvas.setOnMouseDragged(
          e -> {
            // Brush size (you can change this, it should not be too small or too large).

            final double x = e.getX() - size / 2;
            final double y = e.getY() - size / 2;

            // This is the color of the brush.
            graphic.setFill(Color.BLACK);
            graphic.fillOval(x, y, size, size);
          });

      isPen = true;
    }

    // make it an eraser
    else {
      size = 15.0;
      penEraserButton.setText("Pen");

      canvas.setOnMouseDragged(
          e -> {
            // Brush size (you can change this, it should not be too small or too large).

            final double x = e.getX() - size / 2;
            final double y = e.getY() - size / 2;

            // This is the color of the brush.
            graphic.clearRect(x, y, size, size);
          });

      isPen = false;
    }
  }
}
