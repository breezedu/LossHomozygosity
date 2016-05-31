package coursera_java_duke;

public class D0420_SAMD11_Pai2g {
	
	/***************
	 * Take one gene, SAMD11 for example:
	 * 
	 * Get all the mutation site within gene SAMD11, also delete those not on any Exons; 
	 * get the Allele count over population size AC/60,706;
	 * since the real number is too small, so get the log(AC/60,706)
	 * 
	 */
	
	
	/**********
	 * 1st
	 * From CCDS dataset, get gene frame/regions for each gene, 
	 * save them to a txt document with the same name as the gene name plus gene_frame;
	 * 
	 * i.e. for gene TNN the gene frame txt document would be TNN_gene_frame.txt
	 * 
	 * ************************************************************************
	 * format:
	 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
	 * 1	SAMD11		925941		944152	exon0		925941		926012
	 * 1	SAMD11		925941		944152	exon1	 	930154		930335
	 * 1	SAMD11		925941		944152	exon2	 	931038		931088
	 * 1	SAMD11		925941		944152	exon3	 	935771		935895
	 * 1	SAMD11		925941		944152	exon4	 	939039		939128
	 * 1	SAMD11		925941		944152	exon5	 	939274		939459
	 * 1	SAMD11		925941		944152	exon6	 	941143		941305
	 * 1	SAMD11		925941		944152	exon7	 	942135		942250
	 * 1	SAMD11		925941		944152	exon8	 	942409		942487
	 * 1	SAMD11		925941		944152	exon9	 	942558		943057
	 * 1	SAMD11		925941		944152	exon10	 	943252		943376
	 * 1	SAMD11		925941		944152	exon11	 	943697		943807
	 * 1	SAMD11		925941		944152	exon12	 	943907		944152
	 * ************************************************************************
	 * 
	 */
	
	
	
	
	
	
	
	
	/***************
	 * 2nd
	 * From ExAC dataset, get gene variants for each position,
	 * save them to a txt document with the same name as the gene name plus variants;
	 * 
	 *  i.e. For all genes, we will name the variants txt document ExAC_variants.txt
	 *  ***************************************************************
	 *  format:
	 *  CHROM	Position	AC	PolyPhen
	 *  1		934388		5	N/A
	 *  1		934388		5	N/A
	 *  1		934444		1	benign
	 *  1		934444		1	N/A
	 *  1		934444		1	N/A
	 *  1		934447		1	benign
	 *  1		934447		1	N/A
	 *  ****************************************************************  
	 *  
	 *  Data from ExAc are considered as EXPECTED data in our model;
	 *  
	 */
	
	
	
	
	
	
	
	/******************
	 * 3rd
	 * Get target genes names from ALS dataset;
	 * 
	 * Store all genens names to an ArrayList; 
	 * for each gene, get the frame (regions) of each Exon from the CCDS dataset
	 * get all qualify variants within that gene&exon regions
	 * put all this variants into an arrayList
	 * 
	 * 
	 * 
	 * Data from ALS are considered as OBSERVED data in our model;
	 * 
	 */
	

}
