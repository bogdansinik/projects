//javac -cp $MPJ_HOME/lib/mpj.jar HelloWorld.java
// javac -cp .:$MPJ_HOME/lib/mpj.jar DistributedNGrams.java
// mpjrun.sh -np 8 DistributedNGrams

import java.io.IOException;
import java.util.*;

import mpi.*;

public class DistributedNGrams {
    static Scanner sc = new Scanner(System.in);
    public static HashMap<String,Integer> ngrams(int n, String str){
        HashMap<String,Integer> ngrams = new HashMap<String,Integer>();//List<String> ngramsList = new ArrayList<String>();
            //System.out.println(st);
            String[] words = str.split(" ");
            //System.out.println(words.length);
            for (int i = 0; i < words.length - n; i++) {
                String tmp = NGram.concat(words, i, i + n);

                if (ngrams.containsKey(tmp)) {
                    NGram.incrementValue(ngrams, tmp);
                } else {
                    ngrams.put(tmp, 1);
                }
            }


        return ngrams;

    }

    public static void main(String[] args) throws IOException {
        long startTimeDis = System.currentTimeMillis();
        //System.out.println("Insert n-gram for which you want to check the frequency: ");
        int n = 2;
        //String B = "Lorem ipsum dolor sit";
        String B  = "lorem ipsum";
        char A = 'l';

        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();


        if (me == 0) {
            //String str1 = String.valueOf(NGram.fileToString("../10MB.txt"));
            //String str =  (str1.toLowerCase(Locale.ROOT)).replaceAll("\\p{Punct}","");
            ArrayList<String> str = (ArrayList<String>) NGram.fileToString("../100MB.txt");
            ArrayList<ArrayList<String>> chunks = ParallelNGrams.getChunks(str, size - 1, n);

            for (int i = 1; i < size; i++) {
                try {
                    String chunk = String.join(" ", chunks.get(i - 1));
                    ArrayList<String> tmp = chunks.get(i - 1);
                    //ArrayList<String> chunk = chunks.get(i-1);

                    //MPI.COMM_WORLD.Send(chunk.toCharArray(), 0, chunk.toCharArray().length, MPI.CHAR, i, 99);
                    try {
                        MPI.COMM_WORLD.Send(chunk.toCharArray(), 0, chunk.toCharArray().length, MPI.CHAR, i, 99);
                    }catch (Exception e){}
                }  catch(Exception e){}
            }
            int chance = 0;
            ArrayList<HashMap<String, Integer>> result = new ArrayList<HashMap<String, Integer>>();
            int sum = 0;
            int numOfB = 0;
            for (int i = 1; i < size; i++) {
                int tempSum[] = new int[1];
                int tempNumOfB[] = new int[1];
                MPI.COMM_WORLD.Recv(tempNumOfB, 0, 1, MPI.INT, i, 98);
                MPI.COMM_WORLD.Recv(tempSum, 0, 1, MPI.INT, i, 97);
                //System.out.println(tempNumOfB[0]);
                //System.out.println(tempSum[0]);

                sum += tempSum[0];
                numOfB += tempNumOfB[0];
                //System.out.println(sum);
            }
            System.out.println("Total ngrams starting with " + A + " is: " + sum);
            System.out.println("Total num of " + B + " is: " + numOfB);
            System.out.println("Freq: " + Math.round((((double) numOfB / (double) sum) * 100.0) * 100.0) / 100.0 + "%");
            //System.out.println("The real chance of having \""+B+"\" after '"+A+ "' is: " +frequency(ngrams, B, A)+"%");
            long stopTimeDis = System.currentTimeMillis();
            System.out.println("DIS: " + (stopTimeDis - startTimeDis));
        } else {
            mpi.Status status = MPI.COMM_WORLD.Probe(0, 99);
            char[] chunk = new char[status.count];
            //char[] arr1 = Arrays.copyOfRange(chunk,0,chunk.length/4);
            //char[] arr2 = Arrays.copyOfRange(chunk,chunk.length/4+1,2*chunk.length/4);
            //char[] arr3 = Arrays.copyOfRange(chunk,2*chunk.length/4+1,3* chunk.length/4);
            //char[] arr4 =  Arrays.copyOfRange(chunk,3*chunk.length/4+1,chunk.length);

            MPI.COMM_WORLD.Recv(chunk, 0, status.count, MPI.CHAR, 0, 99);
            try {


                HashMap<String, Integer> ngrams = ngrams(n, new String(chunk));
                //HashMap<String, Integer> ngrams = ngrams(n, new String(arr1));
                //ngrams = ngrams(n, new String(arr2));
                //ngrams = ngrams(n, new String(arr3));
                //ngrams = ngrams(n, new String(arr4));
            //MPI.COMM_WORLD.Send(ngrams, 0, 0, MPI.OBJECT, 0, 98);
            int numOfB[] = new int[1];

                numOfB[0] = ngrams.get(B);

            //System.out.println(numOfB[0]);
            int sum[] = new int[1];
            for (String i : ngrams.keySet()) {


                if (!i.isEmpty() && i.charAt(0) == A) {
                    sum[0] += ngrams.get(i);

                }



            }

            //System.out.println(sum);
            //NGram.printMap(NGram.sortByValue(ngrams));
            //System.out.println(NGram.frequency(ngrams,B, A));
            //System.out.println(sum[0]);
            //System.out.println(numOfB[0]);
            MPI.COMM_WORLD.Send(numOfB, 0, 1, MPI.INT, 0, 98);
            MPI.COMM_WORLD.Send(sum, 0, 1, MPI.INT, 0, 97);
            }catch(Exception e){}
        }

    }

}
