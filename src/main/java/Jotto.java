/**
 * @author jai-serr-ace
 * @version 0.1.0
 * @Since 1/29/26
 **/
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.*;
import java.io.File;
//
public class Jotto {
    private static final int WORD_SIZE = 5;
    private String currentWord;
    private int score;
    private ArrayList<String> playGuesses;
    private ArrayList<String> playWords;
    private String filename;
    private ArrayList<String> wordList;
    private static final boolean DEBUG = true;

    public Jotto(String filename){
        this.wordList = new ArrayList<>();
        this.playWords = new ArrayList<>();
        this.playGuesses = new ArrayList<>();
        this.score = 0;
        this.filename = filename;
        readWords();
    }

    public boolean pickWord(){
        if(wordList.isEmpty()) return false;

        Random random = new Random();
        int rand = random.nextInt(wordList.size());
        currentWord = wordList.get(rand);

        if (playWords.contains(currentWord)) {
            if (playWords.size() == wordList.size()) {
                System.out.println("You've guessed them all!");
                return false;
            }
            return pickWord();
        }

        if(!playWords.contains(currentWord)) playWords.add(currentWord);

        if(DEBUG){
            System.out.println("Current word: " + currentWord);
        }
        return true;
    }

    public String showWordList(){
        StringBuilder myList = new StringBuilder("Current word list:\n");
        for(String word : wordList){
            myList.append(word);
            myList.append("\n");
        }
        String whatever = myList.toString();
        return whatever;
    }

    public ArrayList<String> showPlayerGuesses(){
        if (playGuesses.isEmpty()) {
            System.out.println("No guesses yet");
            return playGuesses; 
        }

        System.out.println("Current player guesses:");
        for (String word : playGuesses) System.out.println(word.toLowerCase());
        System.out.println("Would you like to add the words to the word list? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine().trim().toLowerCase();
        if (option.equals("yes") || option.equals("y")) {
            updateWordList();
            System.out.println(showWordList());
        }
        return playGuesses;
    }

    void playerGuessScores(ArrayList<String> list){
        for (String word : list) {
            System.out.println(word + " : " + getLetterCount(word));
        }
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public ArrayList<String> readWords(){
        try{
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String w = scan.nextLine().trim();
                if(!wordList.contains(w)) wordList.add(w);
            }
            scan.close();
        } catch (FileNotFoundException e){
            System.out.println("Couldn't open " + filename);
        }
        return wordList;
    }

    public void play(){
        boolean menuLoop = true;
        Scanner scanner = new Scanner(System.in);

        while(menuLoop){
            System.out.printf("Choose one of the following:" +
                    "\n1: Start the game\n" +
                    "2: See the word list\n" +
                    "3: See the chosen words\n" +
                    "4: Show Player guesses\n" +
                    "zz to exit\n");
            System.out.println("What is your choice: ");
            String option = scanner.nextLine().trim().toLowerCase();

            switch (option){
                case "1", "one":
                    if(pickWord()){
                        this.score = guess();
                        System.out.println("Your updated score is: " + this.score);
                    }
                    else{
                        showPlayerGuesses();
                    }
                    break;
                case "2", "two":
                    System.out.println(showWordList());
                    break;
                case "3", "three":
                    System.out.println(showPlayedWords());
                    break;
                case "4", "four":
                    showPlayerGuesses();
                    break;
                case "zz":
                    menuLoop = false;
                    continue;
                default:
                    System.out.println("Invalid input");
                    break;
            }
            System.out.println("Press enter to continue");
            scanner.nextLine();
        }
    }

    int guess(){
        ArrayList<String> currentGuess = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int letterCount = 0;
        int score = WORD_SIZE + 1;
        String wordGuess;

        while(true){
            System.out.println("Current score: " + score);
            System.out.println("What is your guess (q to quit):");

            wordGuess = scan.nextLine().trim().toLowerCase();

            if(wordGuess.equals("q")) {
                score = Math.min(0,score);
                break;
            }
            if (WORD_SIZE != wordGuess.length()) {
                System.out.println("Your guess must be length " + WORD_SIZE + ". You entered length " + wordGuess.length() + ".");
                continue;
            }

            if(currentGuess.contains(wordGuess)){
                System.out.println("You have already entered this word");
                continue;
            }

            addPlayerGuess(wordGuess);
            currentGuess.add(wordGuess);

            if (wordGuess.equals(currentWord)){
                System.out.println("Yey!\nYou have guess the correct word: " + wordGuess + "\nScore: " + score);
                currentGuess.add(wordGuess);
                playerGuessScores(currentGuess);
                break;
            }
            letterCount = getLetterCount(wordGuess);

            if(letterCount != WORD_SIZE){
                System.out.println(wordGuess + " has " + letterCount + " letters in common.\n"
                + "Score: " + score);
            }
            else {
                System.out.println("The word you chose is an anagram");
            }

            score--;
            playerGuessScores(currentGuess);
        }
        return score;
    }

    public ArrayList<String> getPlayedWords() {
        return playWords;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public int getLetterCount(String wordGuess) {
        int count = 0;
        if (wordGuess == null || currentWord == null) {
            return 0;
        }

        wordGuess = wordGuess.trim().toLowerCase();

        if (wordGuess.equals(currentWord)) {
            return WORD_SIZE;
        }

        ArrayList<Character> remaining = new ArrayList<>();
        for (int i = 0; i < currentWord.length(); i++) {
            char c = Character.toLowerCase(currentWord.charAt(i));
            if (!remaining.contains(c)) {
                remaining.add(c);
            }
        }

        for (int i = 0; i < wordGuess.length(); i++) {
            char g = wordGuess.charAt(i);
            int idx = remaining.indexOf(g);
            if (idx != -1) {
                remaining.remove(idx);
                count++;
            }
        }
        return count;
    }

    public String showPlayedWords(){
        StringBuilder my_list = new StringBuilder();
        if(playWords.isEmpty()){
            return "No words have been played.";
        }
        else{
            my_list.append("Current list of played words:\n");
            for(String word : playWords){
                my_list.append(word);
                my_list.append("\n");
            }
        }
        return my_list.toString();
    }

    public boolean addPlayerGuess(String guess){
        if(!playGuesses.contains(guess)){
            playGuesses.add(guess);
            return true;
        }
        return false;
    }

    void updateWordList(){
        try (FileWriter myfile = new FileWriter(filename)) {
            for (String guess : playGuesses) {
                boolean duplicate = false;
                for (String word : wordList) {
                    if (word.equals(guess)) {
                        duplicate = true;
                    }
                }

                if (!duplicate) {
                    wordList.add(guess);
                }
            }

            for (String word : wordList) {
                myfile.write(word);
                myfile.write("\n");
            }
        } catch (IOException e){
            System.out.println("Couldn't open " + filename);
        }
    }
}
