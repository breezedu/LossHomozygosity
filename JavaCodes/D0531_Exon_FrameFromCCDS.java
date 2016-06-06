package coursera_java_duke;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*****
 * 
 * @author Jeff
 *
 * The consensus coding sequence (CCDS) project: Identifying a common protein-coding gene set for the human and mouse genomes
 * Get the gene "frame" from the CCDS data file;
 * 
 * Subset the genome information according to the chromosome position, divide the original dataset into 23 sub-datasets; 
 * 
 * for each gene, get the chromosome ID, the gene name, start position, end position, and exon list;
 * Save the gene frame to 
 */
public class D0531_Exon_FrameFromCCDS {
	
	public static void main(String[] args) throws IOException{
		
		//1st readin gene_name text file from local dick.
		//D:\PhD\ 
		String routine = "D:/PhD/";
		String file_name = "CCDS_15.current.txt";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
		
		
		//2nd, inite a buffer writter:
		//save the gene names to a txt document
		File output = new File(routine + "/CCDS15_exon_frame.txt");
		BufferedWriter out_Writer = new BufferedWriter(new FileWriter(output));
		
		
		//write the first line, the title line to the CCDS_gene_frame_exons.txt file
		out_Writer.write("Chr" + "\t" + "gene_name" + "\t" + "geneStart" + "\t" + "geneEnd" + "\t" + "exon_name" + "\t" + "exonStart" + "\t" + "exonEnd" + "\n");
		
		
		
		/**************
		 * initial an ArrayList, to store all chromosome names from the CCDS.current.txt document;
		 * the first verb of each line other than title line would be the chromosome name; 
		 */
		//ArrayList<String> chrList = new ArrayList<String>();
		
		
		/*******
		 * initial an ArrayList, to store all buffer writers for each chromosome;
		 * while checking each gene's location: 
		 * 		if the chr location is already in the chrList, 
		 * 		then get the index of that "chr" string in the chrList;
		 * 		the same index in chr_writter would be the corresponding writter to write-in data to.
		 * 		
		 * 		if the chr location is not in the chrList, like chr = 12:
		 * 		create a new chr_writter, a new txt file: CCDS.chr12.gene_frame_exon.txt;
		 * 		put string "chr_12" to the chrList; 
		 * 		put chr_writter_12 to the chr_writers arrayList;
		 * 		write-in current line data to CCDS.chr12.gene_frame_exon.txt document;
		 */
		//ArrayList<BufferedWriter> chr_writers = new ArrayList<BufferedWriter>();
		
		//get the headline from CCDS.current.txt document
		String headline = read_in.nextLine();
		System.out.println("The first line: \n" + headline + "\n");
		
		
		
		
		while(read_in.hasNextLine()){
			String[] currLine = read_in.nextLine().split("\t");
		
			/*****
			 * check each line:
			 * 
			for(int j=0; j<currLine.length; j++){
				
				System.out.print("\t" + currLine[j]);
			}
				
			System.out.println();
			*/
			
			//write chr gene-name start and end position info to document
			String chr = "chr" + currLine[0];
			String geneName = currLine[2];
			String geneStart = currLine[7];
			String geneEnd = currLine[8];
			String exons = currLine[9];
			
			if(exons.length() > 2){
				exons = exons.substring(1, exons.length()-1);
				
				//System.out.println("Exon: " + exons);
				//this out_Writer will write gene-frame for each gene, no matter which chromosome it locates.
				//out_Writer.write(chr + "\t" + geneName + "\t" + start + "\t" + end + "\t" +exons + "\n");
				
				/*****************
				 * Here we have gene frames for all exons
				 * now we have to split the exon-list and get start and end positons for each exon
				 * 
				 * An example of exon list:
				 * [155208903-155208980, 155220182-155220306, 155227234-155227300, 155228383-155228481, 155236228-155236366, 155238771-155238841]
				 * 
				 * so we first delete the [ and ]; then split the number pairs by comma;
				 * then, split each number pairs by '-' to get two number-strings; 
				 * we do not need to parse the number-strings to numbers, 
				 * because we could print those numbers as exonStart and exonEnd positions directly; 
				 * 
				 */
				
				write_exons(chr, geneName, geneStart, geneEnd, exons, out_Writer);
			}
			
		
		}//end while(read_in.hasNextLine()) loop;
		
		
		
		//close read_in and out_writer
		read_in.close();
		out_Writer.close();
		
		
		//close all chr_writers in the ArrayList
		/*********
		System.out.println("There are " + chr_writers.size() + " out-writers. ");
		for(int i=0; i<chr_writers.size(); i++){
			
			chr_writers.get(i).close();
			
		}//end for in chr_writers loop;
		
		*/
		
	}//end main()
	
	
	
	
	
	/***********
	 * 
	 * @param chr
	 * @param geneName
	 * @param geneStart
	 * @param geneEnd
	 * @param exons
	 * @param Writer
	 * @throws IOException
	 */
	private static void write_exons(String chr, String geneName, String geneStart, String geneEnd, String exons, BufferedWriter Writer) throws IOException {
		// TODO Auto-generated method stub
		
		//1st, split exons into 12345-12356 number pairs;
		//remove space between pairs
		String temp = "";
		for(int i=0; i<exons.length(); i++){
			if(exons.charAt(i)!=' ') temp += exons.charAt(i);
		}
		
		exons = temp;
		
		//split according to 'c' comma;
		String[] exon_pairs = exons.split(",");
		//System.out.println("There are " + exon_pairs.length + " exon-pairs.");
		
		for(int i=0; i<exon_pairs.length; i++){
			
			String exonName = "exon" + i;
			int index = exon_pairs[i].indexOf("-");
			String exonStart = exon_pairs[i].substring(0, index);
			String exonEnd = exon_pairs[i].substring(index+1);
			
			Writer.write(chr + "\t" + geneName + "\t" + geneStart + "\t" + geneEnd + "\t" + exonName + "\t" + exonStart + "\t" + exonEnd + "\n");
			
		}//end for out writer loop; 
		
	} //end write_exons() method; 
	
	
	
}//end of everything in D0324_getGeneInfoFromCCDS class
