import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class NGrams {
    public static float probability(String C, String B, String A){
        String bModified = B.toLowerCase(Locale.ROOT);
        String[] bSplit = bModified.split(" ");
        String aModified = A.toLowerCase(Locale.ROOT);
        int b = bSplit.length;
        //int c = C.length();
        int count1 = 0;
        int count2 = 0;
        String[] cSplit = ngrams(C,b);
        /*for(int i = 0; i < cSplit.length; i++){
            System.out.println(cSplit[i]);
        }*/
        for(int i = 0; i < cSplit.length - 1; i++) {
            if (cSplit[i].equals(bModified)) {
                count1++;
                //System.out.print(count1);
                if (cSplit[i + 1] != null) {
                    String[] tmp = cSplit[i + 1].split(" ");
                    if (tmp[b - 1].equals(aModified)) {
                        count2++;
                        //System.out.print(count2);
                    }
                }
            }
        }
        //System.out.println(count1);
        //System.out.println(count2);
        if(count1 != 0) return ((float)count2/(float) count1) * 100;
        else return 0;

    }
    public static String[] ngrams(String s, int len) {
        String sModified = s.replaceAll("\\p{Punct}", "");
        //StringBuffer sbuff= new StringBuffer(sModified);
        String[] parts = sModified.split(" ");
        String[] partsModified = new String[parts.length];
        for(int j = 0; j < parts.length; j++){
            partsModified[j] = parts[j].toLowerCase(Locale.ROOT);
        }
        String[] result = new String[partsModified.length - len + 1];
        for(int i = 0; i < partsModified.length - len + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for(int k = 0; k < len; k++) {
                if(k > 0) sb.append(' ');
                sb.append(partsModified[i+k]);
            }
            result[i] = sb.toString();
        }
        return result;
    }
    public static String fileToString(String title) throws IOException {

        String everything;
        try (BufferedReader br = new BufferedReader(new FileReader(title))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();

        }
        return everything;
    }
    public static void main(String[] args) throws IOException {
        String text = fileToString("Seminar abstract.txt");
        String ngram = "caffeine";
        String word = "is";
        float result = probability(text, ngram, word);
        System.out.println("The probability of '" +word+ "' appearing after ngram: '" +ngram+ "' is " +result+ "%");
    }
}
