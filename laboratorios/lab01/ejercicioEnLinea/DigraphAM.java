import java.util.ArrayList;
import java.util.ArrayList;
/**
 * Esta clase es una implementación de un digrafo usando matrices de adyacencia
 * 
 * @author Mauricio Toro, Samuel Meneses, Juan Felipe Ortiz
 * @version 1
 */
public class DigraphAM extends Graph
{
   private int[][] mate;
   
   public DigraphAM(int size)
   {
	super(size);
        mate = new int[size+1][size+1];

        for (int i = 1; i <= size; ++i) {
            mate[i][0] = i;
            mate[0][i] = i;
        }

   }
       void imprimir() {
        for (int[] ints : mate) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }
   
   public int getWeight(int source, int destination)
   {	
	return mate[source][destination];
    
    }
   
   public void addArc(int source, int destination, int weight)
   {
    if (!(source == 0 && destination == 0)) {
            mate[source][destination] = weight;
        }
   }
  
   public ArrayList<Integer> getSuccessors(int vertex)
   {
	ArrayList<Integer> np= new ArrayList<>();
        for (int i = 1; i < mate.length; i++) {
            if (mate[vertex][i] != 0) {
                np.add(i);
            }
        }
        return np;
    }
    public int getFirst() {
        return mate[1][0]; 
    
   }
}
