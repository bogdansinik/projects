
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
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

public static HashMap<String,Integer> ngrams(int n, ArrayList<String> str){
        HashMap<String,Integer> ngrams = new HashMap<String,Integer>();//List<String> ngramsList = new ArrayList<String>();
        for( String st : str) {
            //System.out.println(st);
            String[] words = st.split(" ");
            //System.out.println(words.length);
            for (int i = 0; i < words.length - n; i++) {
                String tmp = concat(words, i, i + n);
                if (ngrams.containsKey(tmp)) {
                    incrementValue(ngrams, tmp);
                } else {
                    ngrams.put(tmp, 1);
                }
            }
        }

        return ngrams;

    }


    public static double frequency(HashMap<String,Integer> text, String B, char A){
        if(B.charAt(0) != A) return 0.0;
        double result;
        int sum = 0;
        for (String i: text.keySet()) {


            if (!i.isEmpty() && i.charAt(0) == A) {
                sum += text.get(i);

            }
        }
        System.out.println("Printing num of ngrams starting with '"+A+"': "+sum);
        if(text.get(B) == null){
            System.out.println("Printing num of \""+B+"\": 0");
        }else {
            System.out.println("Printing num of \"" + B + "\": " + text.get(B));
        }
        if(sum != 0 && text.get(B)!=null) {
            result = Math.round((((double)text.get(B) / (double) sum) * 100.0)*100.0)/100.0;
        }
        else result = 0;
        return result;
    }




    public static ArrayList<String> fileToString(String title) throws IOException {

        ArrayList<String> everything = (ArrayList<String>) Files.readAllLines(Path.of(title));
        //System.out.println(everything.size());
//        try (BufferedReader br = new BufferedReader(new FileReader(title))) {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            everything = sb.toString();
//
//        }

        return everything;
    }
    public static void printMap(HashMap<String, Integer> n){
        for (String name: n.keySet()) {
            String key = name.toString();
            String value = n.get(name).toString();
            System.out.println(key + " " + value);
        }
    }


    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list
                = new LinkedList<Map.Entry<String, Integer> >(
                hm.entrySet());

        // Sort the list using lambda expression
        Collections.sort(
                list,
                (i1,
                 i2) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp
                = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }




    public static void main(String[] args) throws Exception {

        //System.out.println("Insert n-gram for which you want to check the frequency: ");
        //String B = sc.nextLine();
        //System.out.println("Insert character for which you want to check frequency of \""+B+ "\" coming after it:");
        //char A = sc.next().charAt(0);
        //int n = B.split(" ").length;
        int n = 4;
        String B = "Lorem ipsum dolor sit";
        //String B = "lorem ipsum";
        char A = 'L';
        long startTimeSeq = System.currentTimeMillis();
        ArrayList<String> str = fileToString("100MB.txt");
        text = ngrams(n,str);
        //printMap(sortByValue(text));
        System.out.println("The real chance of having \""+B+"\" after '"+A+ "' is: " +frequency(text, B, A)+"%");
        long stopTimeSeq = System.currentTimeMillis();
        System.out.println("SEQ: "+(stopTimeSeq - startTimeSeq));
        sc.close();

    }
}
