package coursera_java_duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/***************************
 * 
 * Get the CSV document from ExAC website, only download the LoF variants; 
 * Fit these variants into CCDS_15 exon frames. 
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
public class D0605_TTN_CSV_fit_CCDS15Frame {
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		//1st, read-in TTN exon frame from D:/PhD/CCDS_exon_frames/TTN_exon_frame_CCDS15
		Scanner exon_reader = new Scanner(new File("D:/PhD/CCDS_exon_frames/TTN_exon_frame_CCDS15.txt"));
		
		/***************
		 * the first  line: 
		 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
		 *
		 * the second line: 
		 * chr2	TTN	179610311	179669368	exon0	179610311	179616765
		 * Here we need to create an arrayList to store all exon objects within TTN, 
		 * including all these information list in the first line.
		 * 
		 */
		
		System.out.println(exon_reader.nextLine() + "\n" + exon_reader.nextLine()); 
		
		
		
		//2nd, read-in TTN exac variants of LoF from D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV
		Scanner variants_reader = new Scanner(new File("D:/PhD/TTN_pulled_from_ExAC/exac_TTN_LoF.CSV"));
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
		 */
		System.out.println("\n" + variants_reader.nextLine() + "\n" + variants_reader.nextLine());
		
		
		
		
		
		
		//close all file readers
		exon_reader.close();
		variants_reader.close();
		
	}//end main()

	
}//ee
