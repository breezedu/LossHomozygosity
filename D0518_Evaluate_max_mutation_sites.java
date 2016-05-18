package coursera_java_duke;


/***********
 * Off all the mutation sites (829 on TTN gene), some are not 'rare' enough to be considered;
 * So we set the allele-count threshold at 10, then we got 741 qualify allele-counts
 * if we set the allele-count threshold at 5, we got 691 qualify allele-counts
 * if we set the allele-count threshold at 3, we got 629 qualify allele-counts
 * 
 * Thus, we assume allele-counts less than 5 to be 'rare'.
 * 
 * 
 * 
 * @author Jeff
 * 05-18-2016
 */
public class D0518_Evaluate_max_mutation_sites {
	
	
	public static void main(String[] args){
	
		//Initial total and threshold for our formula
		int total = 691;
		int threshold = 5;
		
		
		//get all the combination results for Comb(n, x);
		for(int i=0; i<25; i++){
			long comb_rest = Combination(50, i);
			System.out.println("Comb_50_" + i + " = " + comb_rest);
		}
		
	}//end of main()

	
	/********
	 * Get the combination of C(n, x)
	 * @param i
	 * @param i2
	 * @return
	 */
	private static long Combination(int n, int x) {
		// TODO Auto-generated method stub

		long result = 1;
		
		for(int i=1; i<x + 1; i++){
			
			result *= (n + 1 - i);
			result = result / i;
			
			//System.out.print(" \t " + result);
		} 
		System.out.println();
		
		return result;
	}

	
	
}//ee
