package JavaCodes;

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
public class D20170224_Extract_exonHash_from_CCDSr14 {

	
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
		D20170224_Extract_exonHash_from_CCDSr14 get_exons = new D20170224_Extract_exonHash_from_CCDSr14();
		
		
		//Initial a HashMap; //call run() method, to get all exons, and update the HashMap;
		HashMap<String, ArrayList<Exon_objects>> exonHash = get_exons.run();
		
		
		//some genes, like PFDN6L, do not have any exons;
		//1	NC_000001.9	PFDN6L	80098	CCDS468.1	Withdrawn	+	-	-	-	Partial
		System.out.println("The # of Exons on PFDN6: " + exonHash.get("PFDN6").size());
		
		//get all gene names with GetGeneNames_from_ALSData object;
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		String[] geneList = getNames.run();
		
		
		//check all gene names in ALS, to see if they are presented in the HashMap:
		int notInHash = 0;
		for( int i=0; i<geneList.length; i++){
			
			String gene_name = geneList[i];
			
			if( !exonHash.containsKey(geneList[i])){
				
				notInHash ++;
				System.out.println(gene_name + " not in the HashMap. ");
			}
			
			if( exonHash.get(geneList[i]).size() < 1)
				System.out.println("Gene: " + gene_name + " has no exons.");
		}
		
		//printout how many genes are not presented in the HashMap;
		System.out.println("There are " + notInHash + " genes not in the hashmap. ");
				
	} //end of main();

	
	/**************
	 * run() method
	 * read in exon frames from local CCDS.current.txt document;
	 * store <geneName, Exon_ArrayList> as key-value paire to a hashmap;
	 * return the hashmap
	 * 
	 * @return
	 * @throws IOException
	 */
	HashMap<String, ArrayList<Exon_objects>> run() throws IOException {
		// TODO Auto-generated method stub
		//1st, find the routine to the CCDS15_exon_frame.txt document;
		String routine = "D:/PhD/CCDS_exon_frames/Hs37.3/";
		String file_name = "CCDS.current.txt";
		
		//D:\PhD\CCDS_exon_frames\CCDS.r14
		routine = "D:/PhD/CCDS_exon_frames/CCDS.r14/";
		file_name = "CCDS.current_30Oct13_release14_PUBLIConly.txt";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
				
		//2nd, get and write title line
		// #chromosome	nc_accession	gene	gene_id	ccds_id	ccds_status	cds_strand	cds_from	cds_to	cds_locations	match_type
		String title_line = read_in.nextLine();
		System.out.println("CCDS title line: " + title_line);
		
		
		//3rd, initial a hashmap to store geneName and Exon-arrayList				
		HashMap<String, ArrayList<Exon_objects>> exonHash = new HashMap<String, ArrayList<Exon_objects>>();
		
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
			ArrayList<Exon_objects> exonList = spliteExons(geneName, Exons);
			
			
			totalExons = exonList.size();
			
			if(exonHash.containsKey(geneName)){
				
				//merge current exonList with the exist exonList in the hashmap;
				for(int i=0; i<exonList.size(); i++){
					
					exonHash.get(geneName).add(exonList.get(i));
				} //end for i<exonList.size() loop;
				
				
			} else {
				
				exonHash.put(geneName, exonList); 
				
				totalGenes++;
			}
			
			//printout each gene's exon number;
			System.out.println("There are " + totalExons + " exons on gene " + geneName + ", based on different transcripts. ");
			
		}//end while loop;
		
		System.out.println("There are " + totalGenes + " genes.");
		
		read_in.close();
		
		return exonHash;
	
	}//end of run() method;


	/*********
	 * Split exons: 
	 * [934438-934811, 934905-934992, 935071-935166, 935245-935352]
	 * @param exons
	 * @param exons2 
	 * @return
	 */
	private ArrayList<Exon_objects> spliteExons(String geneName, String exons) {
		// TODO Auto-generated method stub
		
		ArrayList<Exon_objects> exonList = new ArrayList<Exon_objects>();
		
		
		if(exons.length() > 2){
			
			//1st, remove the '[' and ']'
			exons = exons.substring(1, exons.length()-1);
			
			String[] exonsPair = exons.split(", ");
			//System.out.println("There are " + exonsPair.length + " exons on current gene \n" + exons);
			
			for(int i=0; i<exonsPair.length; i++){
				
				Exon_objects currExon = new Exon_objects();
				
				String[] starEnd = exonsPair[i].split("-");
				currExon.gene_name = geneName;
				currExon.exon_name = "exon" + i;
				currExon.exonStart = Integer.parseInt(starEnd[0]);
				currExon.exonEnd = Integer.parseInt(starEnd[1]);
				exonList.add(currExon);
			}
		}
		
		return exonList;
	}
	
}// ee
