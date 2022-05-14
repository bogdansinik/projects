import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Start {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        String str1 = NGram.fileToString("10MB.txt");
        String str =  (str1.toLowerCase(Locale.ROOT)).replaceAll("\\p{Punct}","");
        System.out.println("Insert n-gram for which you want to check the frequency: ");
        String B = sc.nextLine();
        System.out.println("Insert character for which you want to check frequency of \""+B+ "\" coming after it:");
        char A = sc.next().charAt(0);
        int n = B.split(" ").length;

        long startTimePar = System.currentTimeMillis();
        int cores = Runtime.getRuntime().availableProcessors();
        ArrayList<ArrayList<String>> chunks = ParallelNGrams.getChunks(str, cores, n);
        HashMap<String,Integer> res = ParallelNGrams.getNGrams(chunks, cores, n);
        long stopTimePar = System.currentTimeMillis();
        System.out.println(stopTimePar - startTimePar);
        //NGram.printMap(NGram.sortByValue(res));


        long startTimeSeq = System.currentTimeMillis();
        HashMap<String,Integer> text = NGram.ngrams(n,str);
        long stopTimeSeq = System.currentTimeMillis();
        System.out.println(stopTimeSeq - startTimeSeq);
        //printMap(sortByValue(text));
        System.out.println("The real chance of having \""+B+"\" after '"+A+ "' is: " +NGram.frequency(res, B, A)+"%");
        System.out.println("The real chance of having \""+B+"\" after '"+A+ "' is: " +NGram.frequency(text, B, A)+"%");
        sc.close();

    }
}
