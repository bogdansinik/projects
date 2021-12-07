import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabLive {
    static int[][] graph = {{0, 3, 99, 5}, {2, 0, 99, 4}, {99, 1, 0, 5}, {99, 99, 2, 0}};
    static int n = graph.length;

    public static void main(String[] args) {
        //printMatrix(floyd(graph));

        ExecutorService ex = Executors.newFixedThreadPool(n);
        for (int i = 0; i < n; i++) {
            fmrunnable task = new fmrunnable(i);
            ex.execute(task);

        }
        ex.shutdown();
        printMatrix(graph);
    }

    public static int[][] floyd(int[][] graph1) {
        int n = graph.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    graph1[i][j] = Math.min(graph1[i][j], graph1[i][k] + graph1[k][j]);
                }
            }
        }
        return graph1;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");

            }
            System.out.println();
        }
    }
}
    class fmrunnable implements Runnable{
        int index;
        int [][] matrix = LabLive.graph;
        fmrunnable(int index){
            this.index = index;
        }
        @Override
        public void run() {
            for (int k = 0; k< matrix.length; k++){
                for (int j =0; j< matrix[0].length; j++){
                    matrix[index][j] = Math.min(matrix[index][j],matrix[index][k] + matrix[k][j]);
                }
            }
        }
    }

