import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NGrams {
    private static List<String> text = new ArrayList<String>();
    private static Scanner sc = new Scanner(System.in);

    public static String concat(String[] words, int pocetak, int kraj) {

        StringBuilder sb = new StringBuilder();
        for (int i = pocetak; i < kraj; i++) {
            if (i > pocetak) {
                sb.append(" " + words[i]);
            } else {
                sb.append("" + words[i]);
            }
            //sb.append((i > pocetak ? " " : "") + words[i]);
        }
        return sb.toString();

    }

    public static List<String> ngrams(int n, String str) {

        List<String> ngrams = new ArrayList<String>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++) {
            ngrams.add(concat(words, i, i+n));
        }
        return ngrams;

    }

    public static void trimList() {

        for (int i = 0; i < text.size(); i++) {
            text.get(i).trim();
        }

    }

    public static void frequency(ArrayList<String> text, char A) {

        int c1, c2;
        boolean[] visit = new boolean[text.size()];
        Arrays.fill(visit, false);
        trimList();

        int counter = 1;
        // frequency of the ngram
        for (int i = 0; i < visit.length; i++) {
            if (visit[i]) {
                continue;
            }

            counter = 1;

            for (int j = i + 1; j < visit.length; j++) {
                if (text.get(i).equals(text.get(j))) {
                    visit[j] = true;
                    counter++;
                }
            }

            System.out.printf("%s%s%s%f%s\n", "Element: ", text.get(i), " | frequency: ", (( (double) counter/text.size()) * 100), "%");
        }
        /*c1 = counter;

        // compare with the starting letter
        for (int i = 0; i < visit.length; ++i) {
            if (visit[i]) {
                continue;
            }
            counter = 1;
            for (int j = i + 1; j < visit.length; ++j) {
                if (text.get(i).charAt(0) == A) {
                    visit[j] = true;
                    counter++;
                    System.out.println("Element: " + text.get(i) + " -> Counter: " + counter + " Char: " + text.get(i).charAt(0));
                }
            }
        }
        c2 = counter;

        System.out.println("Probability: " + ((double) c1/c2 * 100) + "%");;*/

    }

    public static void printOut(int n) {

        System.out.println(n + " - gram:");
        for (int i = 0; i < text.size(); i++) {
            for (String grams : ngrams(n, text.get(i))) {
                System.out.println(grams);
            }
        }

    }

    public static void fileToList() throws Exception {

        // INSERT A TEXT FILE INTO FileReader
        FileReader fajl = new FileReader("text.txt");
        BufferedReader readfile = new BufferedReader(fajl);

        String line = readfile.readLine();

        while (line != null) {
            text.add(line);
            line = readfile.readLine();
        }

        readfile.close();

    }

    public static void main(String[] args) throws Exception {

        System.out.println("Insert n: ");
        int n = sc.nextInt();
        //long start_time = System.currentTimeMillis();
        fileToList();
        //System.out.println(getList(text).get(1));
        //System.out.println(text.get(0).charAt(0));
        printOut(n);

        ArrayList<String> new_list = new ArrayList<String>();
        for (int i = 0; i < text.size(); i++) {
            for (String str : ngrams(n, text.get(i))) {
                new_list.add(str);
            }
        }
        // System.out.println(n + "-gram");
        frequency(new_list, 'c');
        // long estimated_time = System.currentTimeMillis();
        // System.out.println("Time: " + (estimated_time - start_time));
        //frequency(text);

        sc.close();

    }

}
