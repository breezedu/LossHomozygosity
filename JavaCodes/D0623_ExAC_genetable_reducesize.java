package data_manipulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/***************************************
 * There are too many colums in the original ExAC gene-table
 * 
 * The title line:
 * $ head -4 ExAC.r0.3.1.sites.vep.gene.table
 * CHROM   POS     REF     ALT     ID      FILTER  AC
 * AC_AFR  AC_AMR  AC_Adj  AC_EAS  AC_FIN  AC_Hemi AC_Het  
 * AC_Hom  AC_NFE  AC_OTH  AC_SAS  AC_MALE AC_FEMALE       
 * AC_CONSANGUINEOUS       AC_POPMAX       AN      AN_AFR  
 * AN_AMR  AN_Adj  AN_EAS  AN_FIN  AN_NFE  AN_OTH  AN_SAS  
 * AN_MALE AN_FEMALE       AN_CONSANGUINEOUS       AN_POPMAX       
 * Hom_AFR Hom_AMR Hom_EAS Hom_FIN Hom_NFE Hom_OTH Hom_SAS Hom_CONSANGUINEOUS      
 * clinvar_measureset_id   clinvar_conflicted      clinvar_pathogenic      
 * clinvar_mut  POPMAX   K1_RUN  K2_RUN  K3_RUN  DOUBLETON_DIST  
 * InbreedingCoeff ESP_AF_POPMAX   ESP_AF_GLOBAL   ESP_AC  KG_AF_POPMAX    
 * KG_AF_GLOBAL    KG_AC   VQSLOD  Gene    Feature SYMBOL  CANONICAL       
 * CCDS    EXON    INTRON  Consequence     HGVSc   Amino_acids     SIFT    
 * PolyPhen        LoF_info        LoF_flags       LoF_filter      LoF     
 * context ancestral       coverage_median coverage_mean   coverage_10     
 * coverage_15     coverage_20     coverage_30
 * *******************************************************
 * 
 * We need items: CHROM, POS, AC, AN, CCDS, EXON, PolyPhen, LoF_info, Lof_flags, LoF
 * 
 * @author Jeff
 *
 */

public class D0623_ExAC_genetable_reducesize {
	
	
	public static void main(String[] args) throws IOException{
		
		//1st read in ExAC gene table data file from local dick.
		//D:\PhD\ 
		String routine = "D:/PhD/ExAC_datasets/";
		String file_name = "ExAC.r0.3.1.sites.vep.gene.table";
		
		Scanner read_in = new Scanner(new File(routine + file_name));
		
		
		
		//2nd, create a buffer writer:
		//save the gene names to a txt document
		File output = new File(routine + "ExAC.Chr2_gene.reduced.table");
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
		 * From the first line, 
		 * We need items: CHROM, POS, AC, AN, CCDS, EXON, PolyPhen, LoF_info, Lof_flags, LoF
		 *  
		 */
		String first_line = read_in.nextLine();
		System.out.println("The header line: \n" + first_line);
		
		//find the index of CHROM, AC, and PolyPhen;
		int index_chrom = -1, index_pos = -1, index_AC = -1, index_AN = -1, index_CCDS = -1, 
				index_Poly = -1, index_LoF_info = -1, index_LoF_flags = -1, index_LoF = -1;
		
		String[] headers = first_line.split("\t");
		System.out.println("There are " + headers.length + " items.");
		
		for(int i=0; i<headers.length; i++){
			
			if(headers[i].equals("CHROM"))		index_chrom = i;
			
			if(headers[i].equals("POS"))		index_pos = i;
			
			if(headers[i].equals("AC")) 		index_AC = i;
			
			if(headers[i].equals("AN")) 		index_AN = i;
			
			if(headers[i].equals("CCDS")) 		index_CCDS = i;
			
			if(headers[i].equals("PolyPhen"))	index_Poly = i;
			
			if(headers[i].equals("LoF_info")) 		index_LoF_info = i;
			
			if(headers[i].equals("LoF_flags")) 		index_LoF_flags = i;
			
			if(headers[i].equals("LoF")) 		index_LoF = i;
			
		} //end for i<headers.length loop;
		
	//	System.out.print("The indice are: CHR " + index_chr +", Position " + index_position +", AC " + index_AC + ", PolyPhen " + index_Poly );
		
		
		
		//4th, get the chr, position, AC, and PolyPhen information in each line according to the index 
		out_writer.write("CHROM" + "\t" + "POS" + "\t" + "AC" + "\t" + "AN" + "\t" + "CCDS" + "PolyPhen" + 
							"\t" + "LoF_info" + "\t" + "LoF_flags" + "\t" + "LoF" + "\t" +"AF" + "\n");
		
		while(read_in.hasNextLine()){
			
			
			String currLine = read_in.nextLine();
			
			String[] varInfo = currLine.split("\t");
			
			if(varInfo[index_chrom].equals("2")){
				
				double AlleleFreq = Double.parseDouble(varInfo[index_AC]) / Double.parseDouble(varInfo[index_AN]);
				
				out_writer.write(varInfo[index_chrom] + "\t" + varInfo[index_pos] + "\t" + varInfo[index_AC] + "\t" 
							+ varInfo[index_AN] + "\t" + varInfo[index_CCDS] + "\t"+ varInfo[index_Poly] + "\t"
							+ varInfo[index_LoF_info] + "\t" + varInfo[index_LoF_flags] + "\t" + varInfo[index_LoF] + "\t" + AlleleFreq +"\n");
			}
			//Printout to have fun:
			//	System.out.println(varInfo.length + ", " + varInfo[index_chrom] + "\t" + varInfo[index_position] + "\t" + varInfo[index_AC] + "\t" + varInfo[index_Poly] + "\n");
			
		
			
		}//end while loop;
		
		
		System.out.println("There are " + headers.length + " items.");
		
		//close reader and output writer
		read_in.close();
		out_writer.close();
		
		
	}//end main()

}//end of everything;
