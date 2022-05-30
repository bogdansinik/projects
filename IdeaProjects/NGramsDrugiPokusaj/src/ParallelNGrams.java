import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ParallelNGrams {
    public static  ArrayList<ArrayList<String>> getChunks(ArrayList<String> text, int parallel, int NgramSize){
        //String[] arrOfText =text.split(" ");
        //int n = arrOfText.length;
        //ArrayList<String> tmp = text;
        int length = 0;
        for(String str: text){
            String [] tmp = str.split(" ");
            length += tmp.length;
        }
        int n = text.size();
        int numOfChunks = (int) Math.ceil((double) n/parallel);
        int totalStrings = 0;
        ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>(numOfChunks);
        for (int i=0; i<parallel; i++) {
            if (i != numOfChunks - 1) {
                //try {
                    //chunks.add(new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(arrOfText, i * numOfChunks, Math.min(i * numOfChunks + numOfChunks + NgramSize - 1, arrOfText.length-1)))));
                   List<String> tmp = text.subList(i * numOfChunks, Math.min(i * numOfChunks + numOfChunks, text.size()));
                   totalStrings += tmp.size();

                   chunks.add(new ArrayList<>(tmp));
                    //chunks.add((ArrayList<String>) text.subList(i * numOfChunks, Math.min(i * numOfChunks + numOfChunks + NgramSize, text.size())));
                //} catch (IndexOutOfBoundsException e) {
                //}
            } else {
                //try {
                    //chunks.add(new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(arrOfText, i * numOfChunks, Math.min(i * numOfChunks + numOfChunks, arrOfText.length-1)))));
                    List<String> tmp = text.subList(i * numOfChunks, Math.min(i * numOfChunks + numOfChunks, text.size()));
                    chunks.add(new ArrayList<>(tmp));
                    //chunks.add((ArrayList<String>) text.subList(i * numOfChunks, Math.min(i * numOfChunks + numOfChunks, text.size())));
                //} catch (IndexOutOfBoundsException e) {
                }
            }
        //}
        //System.out.println(totalStrings);
        return chunks;
    }


    public static HashMap<String, Integer> joinHashMaps(ArrayList<HashMap<String,Integer>> arrOfMaps) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (int i = 0; i < arrOfMaps.size(); i++) {
            HashMap<String, Integer> tmpMap = arrOfMaps.get(i);
            for (String key : arrOfMaps.get(i).keySet()) {
                if (result.containsKey(key)) {
                    result.put(key, result.get(key) + tmpMap.get(key));
                } else {
                    result.put(key, tmpMap.get(key));
                    //System.out.println();
                }
            }
        }
        return result;
    }

    public static HashMap<String, Integer> getNGrams(ArrayList<ArrayList<String>> chunks, int parallel, int n){
        PartialGrams[] tasks = new PartialGrams[parallel];
        Thread[] threads = new Thread[parallel];
        HashMap<String,Integer> res;
        //System.out.println("BROJ CHUNKOVA: "+chunks.size()+" A BROJ PROCESORA: "+parallel);
        for (int i=0; i<parallel; i++){
            tasks[i] = new PartialGrams(chunks.get(i),n);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }
        try {
            for (int i=0; i<parallel; i++){
                threads[i].join();
            }
        }catch (InterruptedException e){}

        ArrayList<HashMap<String,Integer>> temp = new ArrayList<>(parallel);
        for (int i=0; i<parallel; i++){
            temp.add(tasks[i].output);
        }
        res = joinHashMaps(temp);
        return res;
    }
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        //String str1 = String.valueOf(NGram.fileToString("10MB.txt"));
        //String str =  (str1.toLowerCase(Locale.ROOT)).replaceAll("\\p{Punct}","");
        //System.out.println("Insert n-gram for which you want to check the frequency: ");
        //String B = sc.nextLine();
        //System.out.println("Insert character for which you want to check frequency of \"" + B + "\" coming after it:");
        //char A = sc.next().charAt(0);
        //int n = B.split(" ").length;
        int n = 4;
        String B = "Lorem ipsum dolor sit";
        //String B = "lorem ipsum";
        char A = 'L';
        long startTimePar = System.currentTimeMillis();
        ArrayList<String> str = (ArrayList<String>) NGram.fileToString("100MB.txt");


        int cores = Runtime.getRuntime().availableProcessors();
        ArrayList<ArrayList<String>> chunks = ParallelNGrams.getChunks(str, cores, n);
        HashMap<String, Integer> res = ParallelNGrams.getNGrams(chunks, cores, n);
        System.out.println("The real chance of having \"" + B + "\" after '" + A + "' is: " + NGram.frequency(res, B, A) + "%");
        long stopTimePar = System.currentTimeMillis();
        System.out.println("PAR: " + (stopTimePar - startTimePar));
        sc.close();
    }
}


class PartialGrams implements Runnable{
    ArrayList<String> A;
    int n;
    HashMap<String, Integer> output;
    public PartialGrams(ArrayList<String> chunk, int n){
        this.A= chunk;
        this.n = n;
    }
    @Override
    public void run() {
        this.output = NGram.ngrams(n,this.A);
    }
}
