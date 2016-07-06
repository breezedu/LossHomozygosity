package data_manipulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**********
 * All gene regions and exon regions are stored in 
 * "https://raw.githubusercontent.com/breezedu/LossHomozygosity/master/CCDS_exon_frames/0605_CCDS15_exon_frame.txt"
 * 
 * Locally, the txt document could be read from folder D:\PhD\CCDS_exon_frames\0605_CCDS15_exon_frame.txt
 * 
 * Here we create a method, pass a gene name (string) to the method.
 * The method would read all exons listed in CCDS15_exon_frame.txt, 
 * Pick all exons followed by TTN gene name
 * Print all exons into one txt document TTN_exon_frame_CCDS15.txt.
 * 
 * @author Jeff
 *
 */
public class D0706_Extract_exonframes_from_CCDSdataset {

	public static void main(String[] args) throws IOException{
		
		D0706_Extract_exonframes_from_CCDSdataset get_exons = new D0706_Extract_exonframes_from_CCDSdataset();
		
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		
		
		//the gene names from ALS
		String[] geneList = getNames.run();
		
		for( int i=0; i<geneList.length; i++){
			
			String gene_name = geneList[i];
			
			get_exons.run(gene_name);
			
			System.out.println(gene_name + " done.");
			
		}

				
	} //end of main();

	
	
	private void run(String geneName) throws IOException {
		// TODO Auto-generated method stub
		//1st, find the routine to the CCDS15_exon_frame.txt document;
		String routine = "D:/PhD/CCDS_exon_frames/";
		String file_name = "CCDS15_exon_frame.txt";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
		
		//2nd, inite a buffer writter:
		//save the gene names to a txt document
		File output = new File(routine + geneName + "exon_frame_CCDS15.txt");
		BufferedWriter out_writer = new BufferedWriter(new FileWriter(output));
		
		
		//3rd, get and write title line
		String title_line = read_in.nextLine();
		System.out.println("Title line: " + title_line);
		out_writer.write(title_line + "\n");
		
		
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
