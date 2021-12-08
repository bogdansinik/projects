public class Nesto {
    public static void main(String [] args) {
        String s = "Nesto lijepo je napisano ovdje i sta sad raditi sa ovim tekstom to tek treba da vidimo. Ne mogu nista ljepse da smislim tako da to je sto je.";
        char [] c = s.toCharArray();
        int counter = 0;
        for(int i=0; i<c.length; i++){
            if(c[i] == 'a'){
                counter++;
            }
        }
        System.out.println("Number of a in string = " + counter);
    }
}
