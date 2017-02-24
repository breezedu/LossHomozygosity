package JavaCodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
public class D20170224_MonteCarol_TTN_fit_CCDSHash {
	
	
	public static void main(String[] args) throws IOException{
		
		D20170224_Extract_exonHash_from_CCDSr14 CCDSHash = new D20170224_Extract_exonHash_from_CCDSr14();
		
		//Initial a HashMap; //call run() method, to get all exons, and update the HashMap;
		HashMap<String, ArrayList<Exon_objects>> exonHash = CCDSHash.run();
		
		
		
		//create an arrayList to store all exon-objects;
		ArrayList<Exon_objects> exonList = exonHash.get("TTN");
				
		System.out.println("There are " + exonList.size() + " exons in current gene: " + exonList.get(0).gene_name + ".. " + exonList.get(0).exon_name); 
		
		//2nd, read-in TTN exac variants of LoF from D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV
		String routine = "D:/PhD/ExAC_genetable_subdatasets/TTN.CSV";
		
		Scanner variants_reader = new Scanner(new File( routine ));
		/********
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
		System.out.println("For each line of data, the variant position index: " + index_pos + ", Allele-Frequency index: " + index_af +"\n");
		
		
		//initial VariantsOnExons to indicate the number of variants hit exons;
		int VariantsOnExons = 0;
		
		//initial an ArrayList to store all allele-frequencies of variants on exons;
		ArrayList<Double> alleleFreq_list = new ArrayList<Double>();
		
		
		
		
		//initial a buffer-writer to write all variants within exons;
		File output = new File("D:/PhD/TTN_variants_OnExons.txt");
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
				//check the 10% threshold:
				double allel_freq = Double.parseDouble( variants[index_af]);
				
				if(allel_freq > 0 && allel_freq < 0.1){
					
					VariantsOnExons ++; 
					alleleFreq_list.add( allel_freq );
					
					outWriter.write(currLine + "\n");
					
				} //end if allele-frequency greater than 10% threshold;

			}//end if allele hits exon condition;
			
		} //end while loop; 
		
		System.out.println("There are " + VariantsOnExons + " variants on exons.......");
		
		
		//3rd, calculate the probability of homozygous:
		double homo = 1;
		double hit_one_gene = 1;
		
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			hit_one_gene *= (1 - alleleFreq_list.get(i));
		}
		
		hit_one_gene = 1 - hit_one_gene;
		
		homo = hit_one_gene * hit_one_gene;
		
		System.out.println("The observed Pai_2|g, probability of getting homozygos TTN gene: " + homo);
		
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
		
		
		//4th, Monte Carol Integration
		//get 10M simulations:
		
		Double Pai_1M = simulate_10M_MonteCarols(alleleFreq_list, 1000000);
		
		//simulate another 10M*10 times:
		Double sum_Pai_squre = Pai_1M;
		System.out.println("\t \t The initial mean of Pai2_squre*1M is:" +  sum_Pai_squre );
		for(int i=0; i<50; i++){
			
			Double curr_pai_squre = simulate_10M_MonteCarols(alleleFreq_list, 1000000);
			sum_Pai_squre += curr_pai_squre;
			
			System.out.println("\tCurrent pai_squre*1M: " + curr_pai_squre + "\t the mean:" +  sum_Pai_squre / (i+2));
		}
		

	//	System.out.println("The sum over all Pai2g^2*Rho => Expectation: " + sumPai/circle * sumPai/circle);
		
		
	}//end main()

	
	/***********
	 * call MonteCarol() method repeatly 10M times to simulate Pai2_g
	 * the MonteCarol() method would return Pai2_g*Pai2_g
	 * Here in this method, we return the average of Pai2_g*Pai2_g of 10M simulations
	 * 
	 * Pass an arrayList of allele-frequencies and an integer of circle to loop;
	 * 
	 * @param alleleFreq_list
	 * @param circle 
	 * @return
	 */
	private static Double simulate_10M_MonteCarols(ArrayList<Double> alleleFreq_list, int circle) {
		// TODO Auto-generated method stub
		
		double sumPai = 0.0;
		//int circle = 1000000;
		
	//	System.out.println("Will repreat " + circle + " circles.");
		for(int i=0; i<circle; i++){
			
			double Pai2g2Rhgo = MonteCarol(alleleFreq_list);
			
			if(Pai2g2Rhgo > 0){
				
			//	System.out.println("\t i=" + i + "\t Pai2g^2=" + Pai2g2Rhgo);
				sumPai += Pai2g2Rhgo; 
			}
			
		} //end for 10 million loop;
		
	//	System.out.println("The sum over all Pai2g => Expectation: " + sumPai/circle);
		
		return sumPai;
	}


	private static double MonteCarol(ArrayList<Double> alleleFreq_list) {
		// TODO Auto-generated method stub
		
		//4.1 get the arrayList of qualified allele frequencies alleleFreq_list
		//4.2 get an range of random integers for each allele frequency
		int n1 = 0;
		int n2 = 0;
		
		//check every mutation sites along the gene (ttn)
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			//get allele frequence for current index i
			double af = alleleFreq_list.get(i);
			
		//	System.out.println(alleleFreq_list.get(i) + "\t  " + range + "\t [" + (range/2-5) +"-" + (range/2+5)+"]"); 
			double random1 = Math.random() ;
			double random2 = Math.random() ;
			
			int hit = 0;
			if(random1 <= af)
				hit += 1;
			
			if(random2 <= af)
				hit += 1;
			
			if(hit == 2){

				n2 ++; 
				
			} else if ( hit == 1){
				
				n1 ++;	
				
			} else {
				
			} //end if-else n1 and n2 conditions;
			
		}//end for loop; 
		
		
		//calculate Pai2g based on n1 and n2 values
		double Pai2g = 0;
		
		if(n2 > 0) {
			
			//System.out.println("get one n2.");
			Pai2g = 1;
		
		} else if( n1 < 2 ) {
			
			Pai2g = 0;
			
		} else {
			
			Pai2g = 1 - Math.pow(0.5, n1 -1);
		}
		
	//	System.out.println("The Pai2_g * Rho = " + Pai2g * Rhog); 
	//	if(Pai2g > 0) 
	//		System.out.print("n1=" + n1 +" n2=" + n2 + ", Pai2g: " + Pai2g*Pai2g);
		
		return Pai2g*Pai2g ;
	}


	/*******************
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

	
	/******************
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
