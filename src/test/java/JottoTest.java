import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A test for Jotto.  This isn't 100% complete, but it's pretty good.
 * Modified 10 feb 2025
 * @since 3.1.0
 * @author Drew A. Clinkenebeard
 */
class JottoTest {

  static final String TEST_GOOD_TXT = "TEST_GOOD.txt";
  static final String TEST_BAD_TXT = "TEST_BAD.txt";
  private static final ArrayList<String> GOOD_WORDS = new ArrayList<>() {
    {
      add("oiled");
      add("grant");
      add("adieu");
      add("salet");
    }
  };
  private static final ArrayList<String> BAD_WORDS = new ArrayList<>() {
    {
      add("fish");
      add("grants");
      add("foo");
    }
  };

  Jotto jotto = null;

  @BeforeAll
  static void overallSetup() {
    File f = new File(TEST_GOOD_TXT);
    File bad = new File(TEST_BAD_TXT);

    BufferedWriter fileWriter;
    try {
      assertTrue(f.createNewFile());
      fileWriter = new BufferedWriter(new FileWriter(TEST_GOOD_TXT));
      for (String word : GOOD_WORDS) {
        fileWriter.write(word + "\n");
      }
      f.deleteOnExit();
      fileWriter.close();

    } catch (IOException e) {
      System.out.println("Problem creating " + TEST_GOOD_TXT);
    }

    try {
      assertTrue(bad.createNewFile());
      fileWriter = new BufferedWriter((new FileWriter(TEST_BAD_TXT)));
      for (String word : BAD_WORDS) {
        fileWriter.write(String.format("%s%n", word));
      }

      fileWriter.close();
      bad.deleteOnExit();

    } catch (IOException e) {
      System.out.println("Problem creating " + TEST_BAD_TXT);
    }

  }

  @AfterAll
  static void overAllTearDown() {
    File f = new File(TEST_GOOD_TXT);
    File bad = new File(TEST_BAD_TXT);
    f.deleteOnExit();
    bad.deleteOnExit();
  
  }

  @BeforeEach
  void preTestSetup() {
    jotto = new Jotto(TEST_GOOD_TXT);
  }

  @AfterEach
  void preTestTearDown() {
    jotto = null;
  }

  @Test
  void readWords() {
    ArrayList<String> readWordResults;
    readWordResults = jotto.readWords();
    assertNotNull(readWordResults);

    for (String word : GOOD_WORDS) {
      System.out.printf("Checking %s%n", word);
      assertTrue(readWordResults.contains(word));
    }
  }

  @Test
  void showPlayedWords() {
    assertEquals("No words have been played.", jotto.showPlayedWords());
    assertTrue(jotto.pickWord());
    assertFalse(jotto.getPlayedWords().contains(BAD_WORDS.get(0)));
    String currentWord = jotto.getCurrentWord();
    assertTrue(jotto.getPlayedWords().contains(currentWord));

  }

  @Test
  void showWordList() {
    String expectedString = "Current word list:\n";
    assertTrue(jotto.showWordList().contains(expectedString));
    Random random = new Random();
    assertTrue(jotto.showWordList().contains(GOOD_WORDS.get(random.nextInt(GOOD_WORDS.size()))));
    assertFalse(jotto.showWordList().contains(BAD_WORDS.get(random.nextInt(BAD_WORDS.size()))));
  }

  @Test
  void showPlayerGuesses() {
    assertTrue(jotto.showPlayerGuesses().isEmpty());
    Random random = new Random();
    String wordToGuess = GOOD_WORDS.get(random.nextInt(GOOD_WORDS.size()));

    ByteArrayInputStream testInput = new ByteArrayInputStream("n\n".getBytes());
    jotto.addPlayerGuess(wordToGuess);
    System.setIn(testInput);
    ArrayList<String> guesses = jotto.showPlayerGuesses();
    assertFalse(guesses.isEmpty());
    assertEquals(1, guesses.size());
    assertTrue(guesses.contains(wordToGuess));

  }


  @Test
  void getLetterCount() {
    String apple = "apple";
    String grape = "GRAPE";
    String zzzzz = "zzzzz";
    jotto.setCurrentWord(apple);
    assertEquals(5, jotto.getLetterCount(apple));
    assertEquals(3, jotto.getLetterCount(grape));
    assertEquals(0, jotto.getLetterCount(zzzzz));

  }

  @Test
  void updateWordList() {

  }

  @Test
  void pickWord() {
    assertNull(jotto.getCurrentWord());
    assertTrue(jotto.pickWord());
    assertNotNull(jotto.getCurrentWord());
    while (jotto.pickWord()) {
      String currentWord = jotto.getCurrentWord();
      System.out.println("Pickword test: " + currentWord);
      assertTrue(GOOD_WORDS.contains(currentWord));
    }

  }

  @Test
  void score() {
  }

  @Test
  void addPlayerGuess() {
    Random random = new Random();
    String wordToGuess = GOOD_WORDS.get(random.nextInt(GOOD_WORDS.size()));
    assertTrue(jotto.addPlayerGuess(wordToGuess));
    assertFalse(jotto.addPlayerGuess(wordToGuess));
  }

  @Test
  void playerGuessScores() {
  }

  @Test
  void getWordList() {
  }

  @Test
  void getPlayerGuesses() {
  }

  @Test
  void getPlayedWords() {
  }

  @Test
  void getCurrentWord() {
  }

  @Test
  void getFilename() {
  }

  @Test
  void getScore() {
  }

  @Test
  void setCurrentWord() {
  }

  @Test
  void setFilename() {
  }
}
