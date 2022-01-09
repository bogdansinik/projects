
import java.io.*;
import java.util.*;

public class NGram {

    private static HashMap<String,Integer> text = new HashMap<String,Integer>();
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
    public static<K> void incrementValue(Map<K, Integer> map, K key) {
        // get the value of the specified key
        Integer count = map.get(key);

        // if the map contains no mapping for the key,
        // then initialize its value as 0
        if (count == null) {
            count = 0;
        }

        // increment the key's value by 1
        map.put(key, count + 1);
    }

public static HashMap<String,Integer> ngrams(int n, String str){
        HashMap<String,Integer> ngrams = new HashMap<String,Integer>();//List<String> ngramsList = new ArrayList<String>();
        String[] words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++) {
            String tmp = concat(words, i, i+n);
            if(ngrams.containsKey(tmp)){
                incrementValue(ngrams, tmp);
            }else{
                ngrams.put(tmp , 1);
            }
        }

        return ngrams;

    }


    public static double frequency(HashMap<String,Integer> text, String B, char A){
        if(B.charAt(0) != A) return 0.0;
        double result;
        int sum = 0;
        for (String i: text.keySet()) {
            if (i.charAt(0) == A) {
                sum += text.get(i);
                System.out.println("Printing sum: "+sum);
            }
        }
        System.out.println("Printing get(B): " + text.get(B));
        if(sum != 0 && text.get(B)!=null) {
            result = (double)text.get(B) / (double) sum;
        }
        else result = 0;
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
    public static void printMap(HashMap<String, Integer> n){
        for (String name: n.keySet()) {
            String key = name.toString();
            String value = n.get(name).toString();
            System.out.println(key + " " + value);
        }
    }
    public static void main(String[] args) throws Exception {

        System.out.println("Insert n: ");
        int n = sc.nextInt();

        String str = "Ja nisam odavde. Prodajem dusu vragu svome. Poderao sam jaknu. Prava hrana je ukusna. Prodajem dusu vragu Prodajem dusu vragu";
        text = ngrams(n,str);
        printMap(text);
        System.out.println(frequency(text, "dusu vragu", 'P'));
        sc.close();

    }
}
