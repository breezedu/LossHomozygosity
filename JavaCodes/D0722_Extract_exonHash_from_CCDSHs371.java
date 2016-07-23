package data_manipulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
		String title_line = read_in.nextLine();
		System.out.println("Title line: " + title_line);
				
		int totalExons = 0;
		
		while(read_in.hasNextLine()){
			
			String currLine = read_in.nextLine();
			
			String[] exons = currLine.split("\t");

			
			if(exons[1].equals(geneName)){
				
				totalExons++;
				out_writer.write(currLine + "\n");
				
			} //end if condition
			
		}//end while loop;
		
		System.out.println("There are " + totalExons + " exons on gene " + geneName + ", based on different transcripts. ");
		
		read_in.close();
		out_writer.close();
	
	}//end of run() method;
	
}// ee
