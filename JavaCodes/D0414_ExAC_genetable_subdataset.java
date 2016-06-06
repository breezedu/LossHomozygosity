package coursera_java_duke;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/** d0414_ExaC_genetable_subdataset.java
 * An application with to read in ExAC gene table data, get the chr position, AC, and polyphen score colums.
 * Author: CBB B&B @ Duke
 * @author Jeff Du
 * Date: April 14, 2016
 * This version make polyphen scores into three categories: N/A, benign, possible, and probably damage.
 * */

public class D0414_ExAC_genetable_subdataset {
	
	
	public static void main(String[] args) throws IOException{
		
		//1st read in ExAC gene table data file from local dick.
		//D:\PhD\ 
		String routine = "D:/PhD/";
		String file_name = "ExAC.r0.3.1.sites.vep.gene.table";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
		
		
		
		//2nd, create a buffer writer:
		//save the gene names to a txt document
		File output = new File(routine + "/ExAC.gene.chrALL.table");
		BufferedWriter out_writer = new BufferedWriter(new FileWriter(output));
		
		
		//3rd, read in ExAC gene table data line by line
		// The first line is the header line, write it into the new output-file
		// If the first split verb is 1, 
		/**********
		 * Below, the headers in the first line:
		 * ****************************************************
		 * CHROM	POS	REF	ALT	ID	FILTER	AC	
		 * AC_AFR	AC_AMR	AC_Adj	AC_EAS	AC_FIN	AC_Hemi	AC_Het	AC_Hom	AC_NFE	AC_OTH	AC_SAS	AC_MALE	AC_FEMALE	AC_CONSANGUINEOUS	AC_POPMAX	
		 * AN	AN_AFR	AN_AMR	AN_Adj	AN_EAS	AN_FIN	AN_NFE	AN_OTH	AN_SAS	AN_MALE	AN_FEMALE	AN_CONSANGUINEOUS	AN_POPMAX	
		 * Hom_AFR	Hom_AMR	Hom_EAS	Hom_FIN	Hom_NFE	Hom_OTH	Hom_SAS	Hom_CONSANGUINEOUS	
		 * clinvar_measureset_id	clinvar_conflicted	clinvar_pathogenic	clinvar_mut	POPMAX	
		 * K1_RUN	K2_RUN	K3_RUN	DOUBLETON_DIST	InbreedingCoeff	ESP_AF_POPMAX	
		 * ESP_AF_GLOBAL	ESP_AC	KG_AF_POPMAX	KG_AF_GLOBAL	KG_AC	VQSLOD	
		 * Gene	Feature	SYMBOL	CANONICAL	CCDS	EXON	INTRON	Consequence	HGVSc	
		 * Amino_acids	SIFT	
		 * PolyPhen	
		 * LoF_info	LoF_flags	
		 * LoF_filter	LoF	context	ancestral	
		 * coverage_median	coverage_mean	
		 * coverage_10	coverage_15	coverage_20	coverage_30
		 * ****************************************************
		 * 
		 * From the first line, we need CHROM (chromosome position), AC (allele counts), and PolyPhen (PolyPhen score);
		 *  
		 */
		String first_line = read_in.nextLine();
		System.out.println("The header line: \n" + first_line);
		
		//find the index of CHROM, AC, and PolyPhen;
		int index_chr = -1, index_position = -1, index_AC = -1, index_Poly = -1;
		String[] headers = first_line.split("\t");
		
		for(int i=0; i<headers.length; i++){
			
			if(headers[i].equals("CHROM"))		index_chr = i;
			
			if(headers[i].equals("POS"))		index_position = i;
			
			if(headers[i].equals("AC")) 		index_AC = i;
			
			if(headers[i].equals("PolyPhen"))	index_Poly = i;
			
		} //end for i<headers.length loop;
		
		System.out.print("The indice are: CHR " + index_chr +", Position " + index_position +", AC " + index_AC + ", PolyPhen " + index_Poly );
		
		
		
		//4th, get the chr, position, AC, and PolyPhen information in each line according to the index 
		out_writer.write("CHROM" + "\t" + "Position" + "\t" + "AC" + "\t" + "PolyPhen" + "\n");
		
		while(read_in.hasNextLine()){
			
			
			String currLine = read_in.nextLine();
			
			String[] varInfo = currLine.split("\t");
			
			//for the PolyPhen item, it could be "N/A", "benign", or "probably_damaging";
			//but in the dataset, there are scores following, like benign(0.02), probably_damaging(0.982), etc.
			
			if(varInfo[index_Poly].contains("probably")){
				varInfo[index_Poly] = "probably";
			
			} else if(varInfo[index_Poly].contains("possibly")){
				varInfo[index_Poly] = "possibly";
				
			}
			else if(varInfo[index_Poly].contains("benign")) {
				varInfo[index_Poly] = "benign";
				
			} else {
				varInfo[index_Poly] = "N/A";
				
			}
			
			out_writer.write(varInfo[index_chr] + "\t" + varInfo[index_position] + "\t" + varInfo[index_AC] + "\t" + varInfo[index_Poly] + "\n");
			
			//Printout to have fun:
		//	System.out.println(varInfo[index_chr] + "\t" + varInfo[index_position] + "\t" + varInfo[index_AC] + "\t" + varInfo[index_Poly] + "\n");
			
		}//end while loop;
		
		System.out.println("End");
		//close reader and output writer
		read_in.close();
		out_writer.close();
		
		
	}//end main()

}//end of everything;
