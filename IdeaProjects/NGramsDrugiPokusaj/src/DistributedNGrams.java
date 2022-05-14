//javac -cp $MPJ_HOME/lib/mpj.jar HelloWorld.java
// javac -cp .:$MPJ_HOME/lib/mpj.jar DistributedNGrams.java
// mpjrun.sh -np 8 DistributedNGrams

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import mpi.*;

public class DistributedNGrams {
     static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {

        //System.out.println("Insert n-gram for which you want to check the frequency: ");
        String B = "lorem ipsum";
        //System.out.println("Insert character for which you want to check frequency of \""+B+ "\" coming after it:");
        char A = 'l';
        int n = B.split(" ").length;

        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();


        if (me == 0){
            String str1 = NGram.fileToString("../10MB.txt");
            String str =  (str1.toLowerCase(Locale.ROOT)).replaceAll("\\p{Punct}","");
            ArrayList<ArrayList<String>> chunks = ParallelNGrams.getChunks(str, size-1, n);

            for (int i=1; i<size; i++) {
                String chunk = String.join(" ", chunks.get(i-1));
                MPI.COMM_WORLD.Send(chunk.toCharArray(), 0, chunk.toCharArray().length, MPI.CHAR, i, 99);
            }
            int chance = 0;
            ArrayList<HashMap<String,Integer>> result = new ArrayList<HashMap<String,Integer>>();
            int sum = 0;
            int numOfB = 0;
            for (int i=1; i<size; i++){
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
            System.out.println("Total ngrams starting with "+A+" is: "+sum);
            System.out.println("Total num of "+B+" is: "+numOfB);
            System.out.println("Freq: "+ Math.round((((double)numOfB / (double) sum) * 100.0)*100.0)/100.0+"%");
            //System.out.println("The real chance of having \""+B+"\" after '"+A+ "' is: " +frequency(ngrams, B, A)+"%");
        }else {
            mpi.Status status = MPI.COMM_WORLD.Probe(0, 99);
            char[] chunk = new char[status.count];
            MPI.COMM_WORLD.Recv(chunk, 0, status.count, MPI.CHAR, 0,99);
            HashMap<String, Integer> ngrams = NGram.ngrams(n, new String(chunk));
            //MPI.COMM_WORLD.Send(ngrams, 0, 0, MPI.OBJECT, 0, 98);
            int numOfB[] = new int[1];
            numOfB[0] = ngrams.get(B);
            //System.out.println(numOfB[0]);
            int sum[] = new int [1];
            for (String i: ngrams.keySet()) {


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

        }
    }
}