package data_manipulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class D0712_calculate_P2g_all_Genes_ALS {
	
	public static void main(String[] args) throws IOException{
		
		//create a calculate Pi_2|g from ALS data object
		D0712_calculate_P2g_all_Genes_ALS calculate_allGene_Pi2g = new D0712_calculate_P2g_all_Genes_ALS();
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		
		//get all genes names from getNames.run();
		String[] geneNames = getNames.run();
		
		for(int i=15000; i<geneNames.length; i++){

			calculate_allGene_Pi2g.run(geneNames[i]);
			
		}
		
	}//end main();
	
	
	/**************************************************
	 * run() method;
	 * pass by a gene name
	 * calculate the Pi_2|g for that gene
	 * 
	 * equations based on the slice
	 * 
	 * @param gene_name
	 * @throws IOException
	 */
	public void run(String gene_name) throws IOException{
		
		/**************
		 * Step 1, read in Hom and Het data from two different datasets
		 * D:/PhD/LizDeidentified_151002LizDeidentified_151002/gene_samp_matrix_high_LOF_het.txt
		 * and 
		 * D:/PhD/LizDeidentified_151002LizDeidentified_151002/gene_samp_matrix_high_LOF_hom.txt
		 * 
		 * BUT !!! we have to flop these two documents, because het.txt represents hom data and hom.txt represents het data
		 * 
		 */
		System.out.println("step 1, reading hom and het data.");
		
		String route = "D:/PhD/LizDeidentified_151002/LizDeidentified_151002/";
		String hom_data = "gene_samp_matrix_high_LOF_het.txt";
		String het_data = "gene_samp_matrix_high_LOF_hom.txt";
		
		Scanner hom_reader = new Scanner(new File(route + hom_data));
		Scanner het_reader = new Scanner(new File(route + het_data));		
		
		//get the index of TTN gene
		int ttn_hom_index = 0;
		int ttn_het_index = 0;
		
		String[] title_line_hom = hom_reader.nextLine().split("\t");
		String[] title_line_het = het_reader.nextLine().split("\t");
		
		
		//in the LizDeidentified txt documents, both hom and het documents have exactly the same index;
		//so here we assigned ttn_hom and ttn_het index the same value, i;
		for(int i=0; i<title_line_hom.length; i++){
			
			if( title_line_hom[i].equals( gene_name ) ) ttn_hom_index = i;
			
			if( title_line_het[i].equals( gene_name ) ) ttn_het_index = i;
		}
		
		System.out.println("The indic of " + gene_name + " are: hom = " + ttn_hom_index + ", het = " + ttn_het_index); 
		
		
		//initial two ArrayLists, to store het or hom numbers, n1 and n2;
		ArrayList<Integer> hom_list = new ArrayList<Integer>();
		ArrayList<Integer> het_list = new ArrayList<Integer>();		
		
		while(hom_reader.hasNextLine() || het_reader.hasNextLine()){
			
			//scan each line, split them by "\t";
			String[] line_hom = hom_reader.nextLine().split("\t");
			String[] line_het = het_reader.nextLine().split("\t");
			
			//parse count string into count integer
			int hom_count = Integer.parseInt( line_hom[ttn_hom_index]);
			int het_count = Integer.parseInt( line_het[ttn_hom_index]);
			
			//add count integers to corresponds arrayLists;
			hom_list.add(hom_count);
			het_list.add(het_count);
		
		}//end while loop;
		
		System.out.println("Compare lengths of two arrayList, hom: " + hom_list.size() + ", het: " + het_list.size());
		
		
		//close scanners
		hom_reader.close();
		het_reader.close(); 
		
		
		
		/***************
		 * Step 2: calculate \Pi_2|g for ttn gene, 
		 * based on hom_list and het_list; 
		 * 
		 * initial another ArrayList, ttn_pai2g
		 * 
		 * if n1 <2 & n2 <1, ttn_pai2g = 0;
		 * if n2 > 0, ttn_pai2g = 1;
		 * else, ttn_pai2g = 1 - (0.5)^(n1 - 1);
		 */
		System.out.println("Step 2, calculate Pai2g for current gene.");
		
		ArrayList<Double> gene_pai2g = new ArrayList<Double>();
		
		for(int i=0; i<hom_list.size(); i++){
			
			int hom_count = hom_list.get(i);
			int het_count = het_list.get(i);
			
			double current_pai2g = 0;
			
			if(hom_count > 0) {
				
				System.out.println("get one n2.");
				current_pai2g = 1;
			
			} else if( het_count < 2 ) {
				
				current_pai2g = 0;
				
			} else {
				
				current_pai2g = 1 - Math.pow(0.5, het_count -1);
			}
			
			gene_pai2g.add( current_pai2g);
			
		}//end for i<ttn_hom_list.size() loop;
		
		
		
		
		/***********************
		 * Step 3
		 * write these ttn_pai2g data into a txt document;
		 */
		
		System.out.println("Step 3: write current gene's pai2g to a txt document.");
		File output = new File("D:/PhD/Pai2g_allGenes/" + gene_name + "_Pai2g_observed_test.txt");
		BufferedWriter outWriter = new BufferedWriter(new FileWriter(output));
		
		//write title line: individual and ttn
		outWriter.write("indi" + "\t" + gene_name + "\n");
		
		for(int i=0; i<gene_pai2g.size(); i++){
			
			System.out.print( gene_pai2g.get(i) + " ");
			outWriter.write(i + "\t" + gene_pai2g.get(i) + "\n");
		}
		
		outWriter.close();
		
		
		/************************
		 * end
		 * 
		 */
		
		System.out.println("\nEnd of gene " + gene_name + "\n");
		
	}//end of run()
	

}//ee
