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
		
		//The allele counts over population*2 gives allele frequency
		double allele_p = (double) threshold/(60706 * 2);
		double log_p = Math.log10(allele_p);
		double log_p_hat = Math.log10(1 - allele_p);
		
		System.out.println("log_p: " + log_p + " log_p_hat: " + log_p_hat); 
		
		//get all the combination results for Comb(n, x);
		/**********
		 * This is just a test code for Combinatoin() function
		 * 
			for(int i=0; i<125; i++){
				double comb_rest = Combination(700, i);
				System.out.println("Comb_700_" + i + " = " + comb_rest);
			}
		*/
		
		//calculate the chance for possible variants an individual might have
		for(int i=0; i<100; i++){
			
			double log_y = Math.log10(Combination(total, i)) + i * log_p + (total - i) * log_p_hat;
			
			System.out.println( i + " variants, probability: " + Math.exp(log_y) + ".\t log: " + log_y);
		}
		
		
	}//end of main()

	
	/********
	 * Get the combination of C(n, x)
	 * @param i
	 * @param i2
	 * @return
	 */
	private static double Combination(int n, int x) {
		// TODO Auto-generated method stub

		double result = 1;
		
		for(int i=1; i<x + 1; i++){
			
	
			result *= (n + 1 - i);
			result = result / i;
			
			//System.out.print(" \t " + result);
		} 
		
		return result;
	}

	
	
}//ee
