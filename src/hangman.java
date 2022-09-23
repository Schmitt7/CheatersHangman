import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class hangman {


    public static Map<Integer, List<String>> createWordLists() throws FileNotFoundException {

        Map<Integer, List<String>> wordLists = new HashMap<>();
        String fileName = "words.txt";
        int count = 0;

        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {

                String word = scanner.nextLine();
                int key = word.length();

                if (wordLists.containsKey(key)) {
                    wordLists.get(key).add(word);
                }

                else {
                    List<String> list = new ArrayList<>();
                    list.add(word);
                    wordLists.put(key, list);
                }

            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        return wordLists;
    }

    //create word families
    public static Map<String, List<String>> createWordFamilies(List<String> wordList, Set<Character> guessed) {

        Map<String, List<String>> wordFamilies = new HashMap<>();

        for (String word : wordList) {
            String family = findWordFamily(word, guessed);

            if (wordFamilies.containsKey(family)) {
                wordFamilies.get(family).add(word);

            }
            else {
                List<String> list = new ArrayList<>();
                list.add(word);
                wordFamilies.put(word, list);
            }

        }


        return wordFamilies;
    }

    //figure out the word family for the string
    public static String findWordFamily(String word, Set<Character> guessedL) {

        String family = "";

        for (char c : word.toUpperCase(Locale.ROOT).toCharArray()) {

            if (guessedL.contains(c)) {
                family += c;
            }
            else {
                family += "_";
            }
        }


        return family;
    }

    //get best family
    public static String getBestFamily(Map<String, List<String>> wordFamilies) {
        //choose the largest family

        String bestKey = "";
        int bestkeysize = 0;

        for (String word : wordFamilies.keySet()) {
            int keysize = wordFamilies.get(word).size();
            if (keysize > bestkeysize) {
                bestKey = word;
                bestkeysize = keysize;

            }
        }

        return bestKey;
    }

    public static void game(List<String> wordList, int guesses) {

        Set<Character> guessedLetters = new HashSet<Character>();
        Map<String, List<String>> wordfamilies = createWordFamilies(wordList, guessedLetters);
        Scanner userinput = new Scanner(System.in);

        while (guesses != 0) {

            System.out.println("What letter would you like to guess m'lord? ");
            char letter = userinput.next().toUpperCase().charAt(0);

                while (!guessedLetters.add(letter)) {
                    System.out.println("You already used that letter sucka");
                    letter = userinput.next().toUpperCase().charAt(0);
                }

            String newGameState = getBestFamily(wordfamilies);
            wordfamilies = createWordFamilies(wordfamilies.get(newGameState), guessedLetters);

            System.out.println("Letters guessed: " + guessedLetters);
            System.out.println(findWordFamily(newGameState, guessedLetters));
            guesses--;

            System.out.println("You have " + guesses + " chances, dont be a loser.");

            if (guesses == 0) {
                System.out.println(wordfamilies.get(newGameState));
            }
            else if (!findWordFamily(newGameState, guessedLetters).contains("_")) {
                System.out.println("You Win! I think you cheated somehow.");
                guesses = 0;
            }

        }
    }


    public static void main(String[] args) throws FileNotFoundException {

        Scanner userinput = new Scanner(System.in);
        System.out.println("What is the size of the word?");
        int wlength = userinput.nextInt();

        System.out.println("How many guesses would you like to have?");
        int guesses = userinput.nextInt();
        Map<Integer, List<String>> wordlists = createWordLists();
        List<String> wordlist = wordlists.get(wlength);
        System.out.println(wordlist);
        game(wordlist, guesses);
        do{
            System.out.println("Play again? Y/N");
            userinput.nextLine();
            if (userinput.nextLine().equalsIgnoreCase("y")) {
                System.out.println("What is the size of the word?");
                wlength = userinput.nextInt();
                System.out.println("How many guesses would you like to have?");
                guesses = userinput.nextInt();
                wordlist = wordlists.get(wlength);
                game(wordlist, guesses);
            }else{
                break;
            }
        }while (true);

    }

}


