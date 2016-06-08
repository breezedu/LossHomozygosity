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
public class D0607_Extract_TTN_exonframe_from_CCDSdataset {

	public static void main(String[] args) throws IOException{
		
		D0607_Extract_TTN_exonframe_from_CCDSdataset get_ttn_exons = new D0607_Extract_TTN_exonframe_from_CCDSdataset();
		
		String gene_name = "TTN";
		
		get_ttn_exons.run(gene_name);
		
		System.out.println("done");
				
	} //end of main();

	
	
	private void run(String geneName) throws IOException {
		// TODO Auto-generated method stub
		//1st, find the routine to the CCDS15_exon_frame.txt document;
		String routine = "D:/PhD/CCDS_exon_frames/";
		String file_name = "0605_CCDS15_exon_frame.txt";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
		
		//2nd, inite a buffer writter:
		//save the gene names to a txt document
		File output = new File(routine + "TTN_exon_frame_CCDS15.txt");
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
		
		System.out.println("There are " + totalExons + " exons on TTN gene, based on different transcripts. ");
		
		read_in.close();
		out_writer.close();
	
	}//end of run() method;
	
}// ee
