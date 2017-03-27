package simulationViability;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

import data_manipulation.D20170224_Extract_exonHash_from_CCDSr14;
import data_manipulation.Exon_objects;

/***************************
 * 
 * Get the CSV document from ExAC website, only download the LoF variants; 
 * 
 * Filter these variants by CCDS_15 exon frames. 
 * 
 * Check how many qualify variants left, and calculate the probability of homozygous.
 * 
 *  Example: 
 *  For an array of variants p1, p2, p3
 *  The probability of having 0 variant on one gene copy is (1-p1)*(1-p2)*(1-p3)
 *  So, the probability of having at least one variance on one copy of the gene
 *  would be 1-(1-p1)*(1-p2)*(1-p3)
 *  
 *  The probability of having at least one variant on each of two gene copies 
 *  would be [1-(1-p1)*(1-p2)*(1-p3)] * [1-(1-p1)*(1-p2)*(1-p3)].
 *  
 *  ************************************************************************************
 *  RESULT: 
 *  There are 414 variants on exons.
 *  The probability of getting homozygos TTN gene: 3.6328594930727866E-5
 *  ************************************************************************************
 * 
 * @author Jeff
 *
 */
public class D20170323_Simulate_TTN_Viability2copiesBeta0 {
	
	
	public static void main(String[] args) throws IOException{
		
		D20170224_Extract_exonHash_from_CCDSr14 CCDSHash = new D20170224_Extract_exonHash_from_CCDSr14();
		
		
		/*******************************************************************************************/
		//Initial a HashMap; //call run() method, to get all exons, and update the HashMap;
		HashMap<String, ArrayList<Exon_objects>> exonHash = CCDSHash.run();				
		
		//create an arrayList to store all exon-objects;
		ArrayList<Exon_objects> exonList = exonHash.get("TTN");
				
	//	System.out.println("There are " + exonList.size() + " exons in current gene: " + exonList.get(0).gene_name + ".. " + exonList.get(0).exon_name); 

		
		
		/******************************************************************************************/
		//2nd, read-in TTN exac variants of LoF from D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV
		String routine = "D:/PhD/ExAC_genetable_subdatasets/TTN.CSV";
		
		//the routine on desktop: 
		routine = "D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV"; 
		
		Scanner variants_reader = new Scanner(new File( routine ));
		/********************************************************************************************
		 * the first line:
		 * "Chrom","Position","RSID","Reference","Alternate","Consequence","Protein Consequence","Transcript Consequence",
		 * "Filter","Annotation","Flags","Allele Count","Allele Number","Number of Homozygotes","Allele Frequency",
		 * "Allele Count African","Allele Number African","Homozygote Count African","Allele Count East Asian",
		 * "Allele Number East Asian","Homozygote Count East Asian","Allele Count European (Non-Finnish)",
		 * "Allele Number European (Non-Finnish)","Homozygote Count European (Non-Finnish)","Allele Count Finnish",
		 * "Allele Number Finnish","Homozygote Count Finnish","Allele Count Latino","Allele Number Latino",
		 * "Homozygote Count Latino","Allele Count Other","Allele Number Other","Homozygote Count Other",
		 * "Allele Count South Asian","Allele Number South Asian","Homozygote Count South Asian"
		 * 
		 * the second line:
		 * "2","179391750",".","G","A","p.Arg35989Ter","p.Arg35989Ter","c.107965C>T","PASS","stop gained",
		 * "LC LoF","1","108206","0","0.000009242","0","9042","0","0","7868","0","0","60554","0","0",
		 * "6278","0","1","10212","0","0","802","0","0","13450","0"
		 * 
		 * Since each item is wrapped by "", and seperated by comma, so we have to delete all "s, and split the string by comma.
		 * then we keep the chrom, position, Allele Frequency. (so far only these columns) 
		 * 
		 */
		
		
		String titleLine = variants_reader.nextLine();
		titleLine = remove_quotes(titleLine);
		System.out.println("\nvariants reader, exac_TTN_LoF.CSV title line done. \n" );
		
		//get index of position and allele_frequency; 
		int index_pos = 1;
		int index_af = 14;
		
		String[] title = titleLine.split(",");
		for(int i=0; i<title.length; i++){
			
			if(title[i].equals("Position")) 			index_pos = i;
			
			if(title[i].equals("Allele Frequency")) 	index_af = i;
		}
		
		//printout the index of Position and Allele-Frequency;
		//System.out.println("For each line of data, the variant position index: " + index_pos + ", Allele-Frequency index: " + index_af +"\n");
		
		
		//initial VariantsOnExons to indicate the number of variants hit exons;
		int VariantsOnExons = 0;
		
		//initial an ArrayList to store all allele-frequencies of variants on exons;
		ArrayList<Double> alleleFreq_list = new ArrayList<Double>();
		
		
		
		
		//initial a buffer-writer to write all variants within exons;
		File output = new File("D:/PhD/QualifyTTN_variants_OnExons.txt");
		BufferedWriter outWriter = new BufferedWriter(new FileWriter(output));
		
		//write in the titleLine without any quotes.
		outWriter.write(titleLine + "\n");
		
		
		while(variants_reader.hasNextLine()){
			
			String currLine = variants_reader.nextLine(); 
			currLine = remove_quotes(currLine); 
			
			//System.out.println(currLine); 
			
			//split the line by ",";
			String[] variants = currLine.split(",");
			
			int position = Integer.parseInt( variants[index_pos] );
			
			if( check_If_hits_Exons(position, exonList) ){
				
				//System.out.println(" One hit: " + position);
				//check the 10% or 15% threshold:
				double allel_freq = Double.parseDouble( variants[index_af]);
				
				if(allel_freq > 0 && allel_freq < 0.1){
					
					VariantsOnExons ++; 
					alleleFreq_list.add( allel_freq );
					
					outWriter.write(currLine + "\n");
					
				} //end if allele-frequency greater than 10% threshold;

			}//end if allele hits exon condition;
			
		} //end while loop; 
		
		System.out.println("There are " + VariantsOnExons + " variants on exons.......");
		
		
		
		/******************************************************************************************/
		//3rd, calculate the probability of homozygous: \sum{ \pai_2|g * \rho_g }
		
		double Pai2gRho = Calculate_SumPai2gRho(alleleFreq_list); 
		
		System.out.println("The observed Pai_2|g, probability of getting homozygos TTN gene: " + Pai2gRho);
		
		/*****************************************************************************
		 * The output of this java code:
		 * 
		 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
		 * TTN_exon_frame_CCDS15.txt done title line done.
		 * 
		 * There are 309 exons on current gene: TTN
		 * 
		 * variants reader, exac_TTN_LoF.CSV title line done.
		 * 
		 * For each line of data, the variant position index: 1, Allele-Frequency index: 14
		 * 
		 * There are 309 variants on exons.
		 * The probability of getting homozygos TTN gene: 1.5553349638101947E-5
		 * 
		 *****************************************************************************/
		

		variants_reader.close();
		outWriter.close();
		
		
		
		/******************************************************************************************/
		//4th, Monte Carol Integration to simulate the P(V=1 | X) = 1/( 1 + exp(alpha + beta * I_(x=2)) 
		//get 10k simulations, output simulated n1, n2, and pai2_g into a txt document. 
		
		Random generator = new Random(1234);
		int size = 100001; 
		
		//since there are only 4 cores in my desktop, so I have to limit the use of cores to be 3, otherwise the cpu occupation would be 100%
		for(int i=1002; i<3000; i+=3){
			
			//every time, only pass 3 parallel jobs to the IntStream() method;
			//in this way, I can physically, control the usage of CPUs to be less than 100%. 
			IntStream.range(i,i+3).parallel().forEach(circle->{
		         try {
		        	 
					simulate_100K_MonteCarols(alleleFreq_list, generator, size, circle);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			
		} //end out for loop; 
		//try parallel for each circle. 

		
		//for(int circle = 0; circle <1000; circle++){
		//	simulate_10K_MonteCarols(alleleFreq_list, generator, size, circle);
		//}
	
		
	}//end main()

	
	/**************
	 * Pass an arrayList of allele frequencies
	 * Return the summation of Pai_2|g * Rho from the population data
	 * 
	 * @param alleleFreq_list
	 * @return
	 */
	private static double Calculate_SumPai2gRho(ArrayList<Double> alleleFreq_list) {
		// TODO Auto-generated method stub
		
		double pai2gRho = 1;
		double hit_one_gene = 1;
		
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			hit_one_gene *= (1 - alleleFreq_list.get(i));
		}
		
		hit_one_gene = 1 - hit_one_gene;
		
		pai2gRho = hit_one_gene * hit_one_gene;
		
		return pai2gRho;
		
	} //end Calculate_SumPai2gRho() method; 


	/***********
	 * via1 = 1 / ( 1 + e^alpha)
	 * via2 = 1 / ( 1 + e^(alpha + beta*I) )
	 * Pass an arrayList of allele-frequencies and an integer of circle to loop;
	 * 
	 * @param alleleFreq_list
	 * @param generator 
	 * @param size 
	 * @param circle
	 * @return 
	 * @return 
	 * @throws IOException 
	 */
	private static void simulate_100K_MonteCarols(ArrayList<Double> alleleFreq_list, Random generator, int size, int circle) throws IOException {
		// TODO Auto-generated method stub
		
		//create an array to record mutations happened on each site of each copy
		double[] alleles_record = new double[alleleFreq_list.size()];
		
		System.out.println(); 
		double via1 = 0.851;
		double via2 = 0.511; 
		double alpha = Math.log(1/via1 - 1); 
		double beta =  0; //Math.log( (1.0 - via2)/via2 ) - alpha; 
		
		//via1 = 1.0/(1 + Math.pow(Math.E, alpha));
		via2 = via1;
		
		File output = new File("D:/PhD/PhD/100kbeta0/simulated_n2n1_" + circle + ".txt");
		BufferedWriter outWriter = new BufferedWriter(new FileWriter(output));
		
		//write the title line
		outWriter.write("n1" + "\t" + "n2" + "\t" + "Pai2g\n");
		
		/******************************************************************************************************/
		//here size denotes sample size 100k here 
		for(int i=0; i<size; i++){
			
			String n1andn2 = MonteCarol(alleleFreq_list, generator, alpha, beta, alleles_record);
			//System.out.println("n2+n1: " + n2andn1);
			
			//write n2 and n1 into a txt file; 
			outWriter.write(n1andn2 + "\n"); 
			
		} //end for 10 million loop;
		
		// outWriter.close(); 
		System.out.println("Viability #1= " + via1 + " via#2=" + via2 + " a=" + alpha + " b=" + beta); 
		System.out.println("Simulation # " + circle + " done, there are " + alleleFreq_list.size() + " variants on the gene. \n");
		
		ArrayList<Double> allels_sim = new ArrayList<Double>();
		
		//print out recorded alleles
		// put each simulated allele frequency to the array list allels_sim; 
		for(int i=0; i<alleles_record.length; i++){
			allels_sim.add( alleles_record[i]/(size*2) );
		}
		System.out.println();
		
		double Pai2gRho_sim = Calculate_SumPai2gRho(allels_sim); 
		System.out.println("The simulated SumPai2gRho: " + Pai2gRho_sim);
		
		//write the simulated Pai2gRho to the txt document, the very last line; 
		outWriter.write("0" + "\t" + "0" +"\t" + Pai2gRho_sim + "\n");
		
		outWriter.close(); 
		
	}//end simulate_10K_MonteCarols() method; 


	/***********************************************************************
	 * MonteCarol() will simulate one gene, based on a given arraylist of variants
	 * each variant will follow Birnolli distribution, all variants would follow Binomial Dist
	 * 
	 * @param alleleFreq_list
	 * @param generator 
	 * @param alleles_record 
	 * @param beta2 
	 * @param alpha2 
	 * @return
	 */
	private static String MonteCarol(ArrayList<Double> alleleFreq_list, Random generator, double alpha, double beta, double[] alleles_record) {
		
		// create new array from old array; 
		// when the viability for current simulated phenotype is negative, we have to delete the allele counts we simulated. 
		double[] alleles_temp = new double[alleles_record.length];
				
		//4.1 get the arrayList of qualified allele frequencies alleleFreq_list
		//4.2 get an range of random integers for each allele frequency
		int n1 = 0;
		int n2 = 0; 
		int size = alleleFreq_list.size();
		
		//create two copies of the gene
		ArrayList<Integer> copy1 = new ArrayList<Integer>();
		ArrayList<Integer> copy2 = new ArrayList<Integer>();
		
		//check every mutation sites along the gene (ttn), copy #1
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			//get allele frequence for current index i
			double af = alleleFreq_list.get(i);
			
		//	System.out.println(alleleFreq_list.get(i) + "\t  " + range + "\t [" + (range/2-5) +"-" + (range/2+5)+"]"); 
		//	double random1 = Math.random() ;
		//	double random2 = Math.random() ;
			
			double random = generator.nextDouble();
			
			if(random < af){
				n1++;
				
				//update the allele record in current site; 
				alleles_temp[i] ++;
				
				copy1.add(1);
				
			} else {
				
				copy1.add(0); 
			}
						
		}//end for loop; 		
		
		//check every mutation sites along the gene (ttn), copy #2
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			//get allele frequence for current index i
			double af = alleleFreq_list.get(i);

			double random = generator.nextDouble();
			
			if(random < af) {
				n1++;
				
				alleles_temp[i] ++;
				copy2.add(1);
				
			} else {
				copy2.add(0); 
			}
						
		}//end for loop; 		
				
		//calculate Pai2g based on n1 and n2 values
		double Pai2g = 0;
				
		
		if( n1 < 2 ) {
			
			Pai2g = 0;
			
		} else {
			
			Pai2g = 1 - Math.pow(0.5, n1-1);
		}
		
		//check if there's homozyous variants
		for(int i=0; i<size; i++){
			if(copy1.get(i) > 0 && copy2.get(i) > 0){
				
				Pai2g = 1;
				n2 ++;
			} //end if
		}
		
		//check viability 1/(1 + exp(alpha + beta*Ix))				
		//alpha + beta * I = 0 + 1*n2;
		//suppose viability for no homozygous is 0.85, and viability for homozygous is 0.51. 
		//double alpha = -1.30;
		//double beta = 1.031;
		
		double power = alpha + beta*Pai2g;
		
		double viability = 1.0/(1 + Math.pow(Math.E, power)); 
		
		double random = Math.random();
		
		if(random < viability){
			String ret = n1 + "\t" + n2 + "\t" + Pai2g;
			
			//add the current alleles_temp to alleles_record;
			for(int j=0; j<alleles_temp.length; j++){
				
				alleles_record[j] += alleles_temp[j]; 
			}
			
			return ret; 
			
		} else {
			
		//	System.out.println(random + " > " + viability + ", n1=" +n1 + " n2=" + n2 + " Not viable.");
			return MonteCarol(alleleFreq_list, generator, alpha, beta, alleles_record); 
			
		}

	}//end MonteCarol() method; 


	/***************************************************************************************
	 * check if a variant hits any exon
	 * @param position
	 * @param exonList
	 * @return
	 */
	private static boolean check_If_hits_Exons(int position, ArrayList<Exon_objects> exonList) {
		// TODO Auto-generated method stub
		/***************************************************************************************
		 * This part is not 'correct', according to the CCDS gene frames.
		 * Even for one gene like TTN, there are several different transcript regions. 
		 * 
		 * Example: 
		 * 
		 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
		 * chr2	TTN	179610311	179669368	exon0	179610311	179616765
		 * chr2	TTN	179391738	179669368	exon0	179391738	179392033
		 * chr2	TTN	179391738	179669368	exon0	179391738	179392033
		 * chr2	TTN	179391738	179669368	exon0	179391738	179392033
		 * chr2	TTN	179391738	179669368	exon0	179391738	179392033
		 * 
		 * Thus, we can not tell if a variant is out of region or not 
		 * by only checking the first exon-object's gene region.
		 * 
		 * if(position < exonList.get(0).geneStart || position >exonList.get(0).geneEnd)	{
		 *	
		 *	 	System.out.println("One variant " + position + " out of region. ");
		 *		return false;
		 *	}
		 *
		 *****************************************************************************************/
		
		boolean hits = false; 
		
		for(int i=0; i<exonList.size(); i++){
			
			if(position >= exonList.get(i).exonStart && position <= exonList.get(i).exonEnd) {
				
				hits = true; 
				//System.out.print(" " + exonList.get(i).exon_name); 
			}
		}
		
		return hits;
		
	} //end of check_If_hits_Exons() method; 

	
	/***************************************************
	 * remove all quotes within the string; 
	 * @param str
	 * @return
	 */
	private static String remove_quotes(String str) {
		// TODO Auto-generated method stub
		
		String retStr = "";
		
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i)!= '"')
				retStr += str.charAt(i);
		}
		
		return retStr;
	}//end remove_quotes() method;

	
}//ee