package nz.ac.auckland.se206.ml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class DoodlePredictionTest {
  @Test
  void testModel() throws ModelException, IOException, TranslateException {
    final DoodlePrediction model = new DoodlePrediction();
    final File folder = new File(System.getProperty("user.dir") + "/src/test/resources/images");
    final File[] images = folder.listFiles();

    for (final File img : images) {
      System.out.println(img.getAbsolutePath());

      final String expected = img.getName().substring(0, img.getName().lastIndexOf('.'));
      final String actual = model.getPredictions(img, 1).get(0).getClassName();

      assertEquals(expected, actual);
    }
  }
}
