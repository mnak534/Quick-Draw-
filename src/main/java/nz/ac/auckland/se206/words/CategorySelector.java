package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Contains methods related to the cdv file containing the categories for drawing.
 *
 * @author Moeka Nakane
 */
public class CategorySelector {

  // Difficulty of categories; easy, medium and hard
  public enum Difficulty {
    E,
    M,
    H
  }

  private Map<Difficulty, List<String>> difficultyToCategories;

  /**
   * Constructor for the class.
   *
   * <p>Initialise the hashmap where key is the difficulty and value is the category
   *
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  public CategorySelector() throws IOException, CsvException, URISyntaxException {

    // Create an empty arraylist to store the categories of each difficulty, and add
    // them to the hashmap
    difficultyToCategories = new HashMap<>();
    for (Difficulty difficulty : Difficulty.values()) {
      difficultyToCategories.put(difficulty, new ArrayList<>());
    }

    // Add categories to corresponding lists
    for (String[] line : getLines()) {
      difficultyToCategories.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * Randomly returns a category of the specified difficulty
   *
   * @param difficulty difficulty of the categories
   * @return a random category of the specified difficulty
   */
  public String getRandomCategory(Difficulty difficulty) {

    List<String> categoryList = difficultyToCategories.get(difficulty);

    return categoryList.get(new Random().nextInt(categoryList.size()));
  }

  /**
   * Reads a csv file and separate each line by ","
   *
   * @return list of string arrays; each string array is a line in the original csv file
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {

    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (var fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }
}
