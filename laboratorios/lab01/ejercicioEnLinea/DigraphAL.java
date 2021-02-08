import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;

/**
 * Esta clase es una implementación de un digrafo usando listas de adyacencia
 * 
 * @author Mauricio Toro, Samuel Meneses, Juan Felipe Ortiz
 * @version 1
 */

public class DigraphAL extends Graph
{
   private ArrayList<LinkedList<Pair<Integer,Integer>>> nodo;
   public DigraphAL(int size)
   {
    super(size);
	nodo = new ArrayList<>();
	for(int i = 0; i < size + 1; i++){
	    nodo.add(new LinkedList<>())
	} 
   }
   
   public void addArc(int source, int destination, int weight)
   {
   	nodo.get(source).add(new Pair<>(destination,weight));  
   }
   
   public int getWeight(int source, int destination)
   {
      int resultado = 0;
      for(Pair<Integer, Integer > IntegerIntegerPair : nodo.get(source)){
	 if (integerIntegerPair.getKey() == destination) resultado = integerIntegerPair.getValue();
	}
	return resultado;	
   }
  
   public ArrayList<Integer> getSuccessors(int vertice)
   {
   	ArrayList<Integer> n = new ArrayList<>();
	nodo.get(vertice).forEach(i -> n.add(i.getKey()));

	return n;
   }
}
