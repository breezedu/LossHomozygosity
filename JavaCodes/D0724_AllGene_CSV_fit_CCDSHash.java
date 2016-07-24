package data_manipulation;

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
 * 
 * @author Jeff
 *
 */
public class D0724_AllGene_CSV_fit_CCDSHash {
	
	
	public static void main(String[] args) throws IOException{
		
		//Create an object of CSV-fit-CCDS
		D0724_AllGene_CSV_fit_CCDSHash CSV2CCDS = new D0724_AllGene_CSV_fit_CCDSHash();
		
		//create an object of gene-gene-names-from-ALS
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		
		//create an exonHash object, to get exon-Hashmap from CCDS-37.3
		D0723_Extract_exonHash_from_CCDSHs373 CCDSHash = new D0723_Extract_exonHash_from_CCDSHs373();
		
		//Initial a HashMap; //call run() method, to get all exons, and update the HashMap;
		HashMap<String, ArrayList<Exon_objects>> exonHash = CCDSHash.run();
		
		
		


		String[] geneName = getNames.run();
		
		for(int i=0; i<500; i++){
			
			System.out.print("GENE: " + geneName[i] + "\t" );
			
			//create an arrayList to store all exon-objects;
			ArrayList<Exon_objects> exonList = exonHash.get(geneName[i]);
			
			if(exonHash.containsKey(geneName[i]) && exonList.size() > 0){
				
				//if the file geneName[i].CSV exist, call CSV2CCDS.run() method;
				File variantCSV = new File("D:/PhD/ExAC_datasets/All/" + geneName[i] + ".CSV");
				if( variantCSV.exists() ){
					
					CSV2CCDS.run(geneName[i], exonList);
					
				} else {
					
					System.out.println(" There are " + exonList.size() + " exons on gene " + geneName[i]);
					
					System.out.println(geneName[i] + "\t" + " Pai2|g" + "\t" + "0.0");
					
				}				
				
			} //end if geneName[i] in HashMap && it's exonList is not empty;
		}//end for i<500 loop
		
		
	}// end main(); 
	
	
	public void run(String geneName, ArrayList<Exon_objects> exonList) throws IOException{
		
		
		//ArrayList<Exon_objects> exonList = new ArrayList<Exon_objects>();
		System.out.println("\n There are " + exonList.size() + " exons on gene: " + geneName ); 
		
		//2nd, read-in TTN exac variants of LoF from D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV
		Scanner variants_reader = new Scanner(new File("D:/PhD/ExAC_datasets/All/" + geneName + ".CSV"));
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
		//System.out.println("\t variants reader, " + geneName + ".CSV title line done. " );
		
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
		File output = new File("D:/PhD/ExAC_datasets/All_variants_on_exons/" + geneName + "_variants_OnExons.txt");
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
				VariantsOnExons ++; 
				alleleFreq_list.add( Double.parseDouble(variants[index_af]));
				
				outWriter.write(currLine + "\n");
			}
		}
		
		
		System.out.println("\tThere are " + VariantsOnExons + " variants on exons.");
		
		
		//3rd, calculate the probability of homozygous:
		double homo = 1;
		double hit_one_gene = 1;
		
		for(int i=0; i<alleleFreq_list.size(); i++){
			
			hit_one_gene *= (1 - alleleFreq_list.get(i));
		}
		
		hit_one_gene = 1 - hit_one_gene;
		
		homo = hit_one_gene * hit_one_gene;
		
		//System.out.println( "The probability of getting homozygos on " );
		
		System.out.println( geneName + "\t" + "Pai2|g" + "\t" + homo);
		
		/*****************************************************************************
		 * The output of this java code:
		 * 
		 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
		 * TTN_exon_frame_CCDS15.txt done title line done.
		 * 
		 * There are 1290 exons in current gene: TTN
		 * 
		 * variants reader, exac_TTN_LoF.CSV title line done.
		 * 
		 * For each line of data, the variant position index: 1, Allele-Frequency index: 14
		 * There are 414 variants on exons.
		 * The probability of getting homozygos TTN gene: 3.6328594930727866E-5
		 * 
		 *****************************************************************************/
		
		//close all file readers
		variants_reader.close();
		outWriter.close();
	}//end run(geneName)

	
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
