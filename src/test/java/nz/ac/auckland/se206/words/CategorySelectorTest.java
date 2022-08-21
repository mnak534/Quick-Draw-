package nz.ac.auckland.se206.words;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CategorySelectorTest {

  @Test
  public void testCSVreader() throws IOException, CsvException, URISyntaxException {
    CategorySelector category = new CategorySelector();
    List<String[]> result = category.getLines();
    int size = result.size();

    //		for (int i = 0; i < size; i++) {
    //			String[] strs = result.get(i);
    //			System.out.println(strs[0] + " " + strs[1]);
    //		}
    assertTrue(size == 345);
  }
}
