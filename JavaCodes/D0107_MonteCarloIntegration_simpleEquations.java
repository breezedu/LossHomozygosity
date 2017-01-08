package data_manipulation;

/******************
 * Trying to write a simple script to testy Monte Carlo integration. 
 * 
 * @author Jeff
 *
 */
public class D0107_MonteCarloIntegration_simpleEquations {
	
	public static void main(String[] args){
		
		int circle = 1000000;
		
		double sum = 0.0;
		
		for(int i=0; i<circle; i++){
			
			double random = Math.random(); 
			sum += Math.pow(random, 3)/3; 
			
			if(i%10000 == 0){
				
				System.out.println("i=" + i +" current simulated result: " + sum/i);
			}
			
		}
		
		double result = 1.0/12; 
		
		System.out.println("The result: " + result +" the simulated result:" + sum/circle); 
	}

}
