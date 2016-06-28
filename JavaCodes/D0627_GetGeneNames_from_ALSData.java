package data_manipulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**********
 * get all 18,000 gene names from ALS dataset;
 * 
 * routine: D:\PhD\LizDeidentified_151002\LizDeidentified_151002
 * file: gene_samp_matrix_high_LOF_het.txt
 * 
 * all names could be found in the first line, also called the title line
 * the very first one is "sample", so we ignore that one. 
 * 
 * @author Jeff
 *
 */
public class D0627_GetGeneNames_from_ALSData {
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		//create an object D0627_GetGeneNames_from_ALSData
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();
		
		//get the names[] array;
		String[] names = getNames.run();
		
		
		//check the results
		System.out.println("There are: " + names.length + " gene names in ALS dataset.");
		
		
		for(int i=1; i<names.length; i++){
			
			System.out.print(names[i] + "\t");
			if(i%200 == 0) System.out.println();
		}
		
	}//end main();

	
	/***************
	 * run() method, to get all gene names from ALS dataset;
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	String[] run() throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		//initial the routine and file name
		String routine = "D:/PhD/LizDeidentified_151002/LizDeidentified_151002/";
		String file_name = "gene_samp_matrix_high_LOF_het.txt";
				
		//create a scanner to scan the txt file
		Scanner name_scanner = new Scanner(new File(routine + file_name));
			
				
		//read in the title line; 
		String title_line = name_scanner.nextLine();
				
		//we do not need to read more lines, so close the scanner here
		name_scanner.close();
				
				
		//split the title into an array, by "\t"
		String[] names = title_line.split("\t");
		
		//remove the first elementary, "samples"
		String[] retNames = new String[names.length - 1];
		
		for(int i=1; i<names.length; i++){
			retNames[i-1] = names[i];
		}
		
		//return the array
		return retNames;
		
	} //end run() method;

}//ee
