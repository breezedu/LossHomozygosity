package data_manipulating;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Step10_stepdown_maxTminPMultipleTest {
	
	public static void main(String[] args) throws FileNotFoundException{
		
		/**********************************************************************************************/
		//	one of the 10 datasets are put in directory
		//	routine: D:\data\ADM\1130
		//	file name: max_mixscore_1.txt
		
		String routine = "D:/data/ADM/";
		String[] files = new String[10];
		for(int i=0; i<10; i++){
			files[i] = "max_mixscore_" + (i+1) + ".txt";
		}

		
		System.out.println("get 10 data matrics from:" + routine);
		
		int[] adjPvalue = new int[229860];
		
		System.out.println("P1\tP2\tP3\tP4\tP5");
		for(int i = 0; i<1; i++){
			
			int[] pvalue = calculateAdjustPvalueOneMatrix(routine, files[i]);
			printArray(pvalue);
			
			for(int p=0; p<229860; p++){
				adjPvalue[p] += pvalue[p];
			}
		}

		//printout all adjust pvalues
		System.out.println("\nAfter checking all 10 matrics, the adjust P-values are: " );
		printArray(adjPvalue);
		
		
	}//end main();
	
	
	/**********************************************
	 * printout an array of integers
	 * @param array
	 */
	private static void printArray(int[] array) {
		// TODO Auto-generated method stub
		
		System.out.println();
		for(int i=array.length-100; i<array.length; i++){
			System.out.print(array[i] + "\t");
		}
		
	}//end printArray()



	/***************************************************************************************************
	 * calculate the adjust p-value for one dataset with 1001 adm mixscore results
	 * 
	 * @param routine
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	private static int[] calculateAdjustPvalueOneMatrix(String routine, String file) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		double[][] matrix = getDataMatrix(routine, file);
		
		//System.out.println("Data Matrix 229860*1001 done.");
				
		
		/**********************************************************************************************/		
		// pass the index of a column, get the corresponding array, sort;
		double[] unshuffled = getOneSortedArray(matrix, 0);
		Arrays.sort(unshuffled);
		
		//System.out.println("get the unshuffled ADM result: " + unshuffled[0] +" " + unshuffled[229859]);
		
		int[] adjustP = new int[229860];
		
		for(int index = 1; index <1001; index++){
			
			double[] shuffled = getOneSortedArray(matrix, index);
			
			double[] Umb = new double[229860];
			Umb[0] = shuffled[0];
		
			for(int i=1; i<229860; i++){
			
				Umb[i] = maxOfTwo(Umb[i-1], shuffled[i]);
			
			}//end for i>=0 loop;
		
			for(int i= 0; i<229860; i++){
				if( Umb[i] >= unshuffled[i])
					adjustP[i] ++;
			}
			
		}//end for index < 1001 loop;
		
	//	System.out.println("The adjust P-value of P1 is: " + adjustP[0]);
		
		return adjustP;
	}



	/**************************************************************************
	 * get max of two double numbers
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	private static double maxOfTwo(double m, double n) {
		// TODO Auto-generated method stub
		
		if(m > n) {
			return m;
			
		} else {
			return n;
		}
		
	}//end maxOfTwo() method; 




	/**************************************************************************************
	 * 
	 * @param matrix
	 * @param col
	 * @return
	 */
	private static double[] getOneSortedArray(double[][] matrix, int col) {
		//  col is the column index
		//	get one column from matrix, return it as a new array
		double[] array = new double[229860];
		
		for(int i=0; i<229860; i++){
			array[i] = matrix[i][col];
		}
		
		return array;
	}//end getOneSortedArray() method;


	/*******************************************************************************************************
	 * get a 229860*1001 matrix from a txt document;
	 * the first column represents ADM result from unshuffled dataset
	 * all other 1000 columns represent ADM results from shuffled datasets
	 * 
	 * @param routine
	 * @param file_name
	 * @return
	 * @throws FileNotFoundException
	 */
	private static double[][] getDataMatrix(String routine, String file_name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner readin = new Scanner(new File(routine + file_name));
		
		//read-in a 229360 * 1001 matrix, each row represents a SNP site, each column represents a adm output
		double[][] dataMatrix = new double[229860][1001];
		int row = 0; 
		while(readin.hasNextLine()){
			
			String[] currLine = readin.nextLine().split("\t"); 
			
			for(int i=0; i<currLine.length; i++){
				dataMatrix[row][i] = Double.parseDouble( currLine[i] );
			}//end for
			
			row++;
		}//end while(readin.hasNextLine());
		
		readin.close(); 
		return dataMatrix;
	}//end getDatamatrix() method; 

}//ee
