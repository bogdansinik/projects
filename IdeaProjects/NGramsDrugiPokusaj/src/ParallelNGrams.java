import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ParallelNGrams {
    public static  ArrayList<ArrayList<String>> getChunks(String text, int parallel, int NgramSize){
        String[] arrOfText =text.split(" ");
        int n = arrOfText.length;

        int numOfChunks = (int) Math.ceil((double) n/parallel);
        ArrayList<ArrayList<String>> chunks = new ArrayList<ArrayList<String>>(numOfChunks);
        for (int i=0; i<parallel; i++){
            if(i!=numOfChunks-1) {
                chunks.add(new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(arrOfText, i * numOfChunks, Math.min(i * numOfChunks + numOfChunks + NgramSize - 1, arrOfText.length-1)))));
            }else {
                chunks.add(new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(arrOfText, i * numOfChunks, Math.min(i * numOfChunks + numOfChunks, arrOfText.length-1)))));
            }
            }
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
}


class PartialGrams implements Runnable{
    String A;
    int n;
    HashMap<String, Integer> output;
    public PartialGrams(ArrayList<String> chunk, int n){
        this.A=String.join(" ", chunk);
        this.n = n;
    }
    @Override
    public void run() {
        this.output = NGram.ngrams(n,this.A);
    }
}
