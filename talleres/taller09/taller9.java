/**
 * Clase en la cual se implementan los metodos del Taller 9
 * 
 * @author Mauricio Toro, Andres Paez
 */
public class Taller9 {

	
	 public static int levenshtein(String a, String b) {
        if(a.equalsIgnoreCase(b))
            return 0;
        
        if(a.length() == 0)
            return b.length();
        
        if(b.length() == 0)
            return a.length();
        
        int dp[][] = new int[a.length()+1][b.length()+1];
        for (int i = 0; i < a.length()+1; i++) 
            dp[i][0] = i;
        
        for (int i = 0; i < b.length()+1; i++) 
            dp[0][i] = i;
        
        for(int i = 1; i <= a.length(); i++){
            for(int j = 1; j <= b.length(); j++){
                if(a.charAt(i-1) == b.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                }else{
                    dp[i][j] = 1 + Math.min(dp[i-1][j],Math.min(dp[i][j-1],dp[i-1][j-1]));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

}