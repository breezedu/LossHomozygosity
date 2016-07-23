package data_manipulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**********
 * All gene regions and exon regions are stored in 
 * "https://raw.githubusercontent.com/breezedu/LossHomozygosity/master/CCDS_exon_frames/0605_CCDS15_exon_frame.txt"
 * 
 * Locally, the txt document could be read from folder D:\PhD\CCDS_exon_frames\Hs37.1\CCDS.current.txt
 * D:\PhD\CCDS_exon_frames\Hs37.1
 * Here we create a method, pass a hashmap and a gene name to the method.
 * The method would read all exons listed in CCDS.current.txt with build Hs37.1, 
 * 
 * Pick all exons followed by a given gene's name, create exon objects correspondly.
 * put all these exon-objects into an ArrayList
 * 
 * take the gene name and the ArrayList as key and value, put them into the hashmap
 * Return the HashMap
 * 
 * Print all exons into one txt docume.
 * 
 * @author Jeff
 *
 */
public class D0722_Extract_exonHash_from_CCDSHs371 {

	
	/********
	 * The main() method; 
	 * create a new extract exon Hash from CCDS object, 
	 * call run() method to get all exons, and update the HashMap<geneName, Exon_ArrayList>;
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		//create a new object of Extracting Exon Hash from CCDS document;
		D0722_Extract_exonHash_from_CCDSHs371 get_exons = new D0722_Extract_exonHash_from_CCDSHs371();
		
		// D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		
		//Initial a HashMap;
		HashMap<String, ArrayList<Exon_objects>> exonHash = new HashMap<String, ArrayList<Exon_objects>>();
		
		//call run() method, to get all exons, and update the HashMap;
		get_exons.run( exonHash );
		
		
		/***
		 * the gene names from ALS
		 *
		String[] geneList = getNames.run();
		
		for( int i=0; i<geneList.length; i++){
			
			String gene_name = geneList[i];
			
			get_exons.run(gene_name, exonHash);
			
			System.out.println(gene_name + " done.");
			
		}
		*/
				
	} //end of main();

	
	
	private void run(HashMap<String, ArrayList<Exon_objects>> exonHash) throws IOException {
		// TODO Auto-generated method stub
		//1st, find the routine to the CCDS15_exon_frame.txt document;
		String routine = "D:/PhD/CCDS_exon_frames/Hs37.1/";
		String file_name = "CCDS.current.txt";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
				
		//3rd, get and write title line
		// #chromosome	nc_accession	gene	gene_id	ccds_id	ccds_status	cds_strand	cds_from	cds_to	cds_locations	match_type
		String title_line = read_in.nextLine();
		System.out.println("Title line: " + title_line);
				

		
		//check the CCDS document line by line:
		int totalGenes = 0; 
		while(read_in.hasNextLine()){

			int totalExons = 0;
			
			String currLine = read_in.nextLine();
			
			String[] exons = currLine.split("\t");

			String geneName = exons[2];
			String Exons = exons[9];
			
			//A very important step: split the exons
			// [934438-934811, 934905-934992, 935071-935166, 935245-935352]
			ArrayList<Exon_objects> exonList = spliteExons(Exons);
			
			
			totalExons = exonList.size();
			
			if(exonHash.containsKey(geneName)){
				
				//merge current exonList with the exist exonList in the hashmap;
				
			} else {
				
				exonHash.put(geneName, exonList); 
				
				totalGenes++;
			}
			
			//printout each gene's exon number;
			System.out.println("There are " + totalExons + " exons on gene " + geneName + ", based on different transcripts. ");
			
		}//end while loop;
		
		System.out.println("There are " + totalGenes + " genes.");
		
		read_in.close();
	
	}//end of run() method;


	/*********
	 * Split exons: 
	 * [934438-934811, 934905-934992, 935071-935166, 935245-935352]
	 * @param exons
	 * @return
	 */
	private ArrayList<Exon_objects> spliteExons(String exons) {
		// TODO Auto-generated method stub
		
		ArrayList<Exon_objects> exonList = new ArrayList<Exon_objects>();
		
		if(exons.length() > 2){
			
			//1st, remove the '[' and ']'
			exons = exons.substring(1, exons.length()-1);
			
			String[] exonsPair = exons.split(", ");
		}
		
		return exonList;
	}
	
}// ee
