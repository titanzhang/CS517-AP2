import java.util.HashMap;
import java.util.HashSet;

public class SpellCorrector {
  // TODO(student): Feel free define private variables here to store whatever
  // you trained.
  private HashMap<String, Integer> dictionary = new HashMap<String, Integer>();

  SpellCorrector() {
    // TODO(student): Contructor to initialize your data-structure
  }

  public void train(final String word) {
    // TODO(student): Implement your own logic to model spell corrector using
    // edit distance. This method will be called for every possible known valid
    // word from corpus. Learn your model from this.
    if (word.equals(word.toUpperCase())) {
      return;
    }

    if (word.length() == 1) {
      if (!word.equalsIgnoreCase("a") && !word.equalsIgnoreCase("i")) {
        return;
      }
    }

    Integer value = dictionary.get(word.toLowerCase());
    value = (value == null)? 1: value + 1;
    dictionary.put(word.toLowerCase(), value);
  }

  public String correct(final String mispelled_word) {
    // TODO(student): Use the model learnt in above method and return correct
    // spelling for given word. Given a mispelled word, return your best
    // predicted corrected word.
    String correctString = "";
    int correctDistance = 0;
    int correctFreq = 0;

    for (String dictString: dictionary.keySet()) {
      int distance = LevDistance(dictString, mispelled_word);
      if (correctString.isEmpty()) {
        correctString = dictString;
        correctDistance = distance;
        correctFreq = dictionary.get(dictString).intValue();
      } else {
        if (distance < correctDistance) {
          correctDistance = distance;
          correctString = dictString;
          correctFreq = dictionary.get(dictString).intValue();
        } else if (distance == correctDistance) {
          if (correctFreq < dictionary.get(dictString).intValue()) {
            correctDistance = distance;
            correctString = dictString;
            correctFreq = dictionary.get(dictString).intValue();
          }
        }
      }
    }

    return correctString;
  }

  // TODO(student): Feel free to define private utility methods here.
  private int min(int i1, int i2, int i3) {
    int temp = (i1 < i2)? i1: i2;
    return (temp < i3)? temp: i3;
  }

  private int LevDistance(final String dictString, final String inputString) {
    int[][] costs = new int[inputString.length() + 1][dictString.length() + 1];
    for (int i = 0; i <= inputString.length(); i ++) {
      costs[i][0] = i;
    }

    for (int j = 0; j <= dictString.length(); j ++) {
      costs[0][j] = j;
    }

    for (int i = 1; i <= inputString.length(); i ++) {
      for (int j = 1; j <= dictString.length(); j ++) {
        int replaceCost = 0;
        if (dictString.charAt(j-1) != inputString.charAt(i-1)) {
          replaceCost = 2;
        }
        costs[i][j] = min(
          costs[i - 1][j] + 1, // delete
          costs[i][j - 1] + 1, // insert
          costs[i-1][j-1] + replaceCost // replace
        );
      }
    }

    return costs[inputString.length()][dictString.length()];
  }

}
