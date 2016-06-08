package data_manipulation;

import java.util.ArrayList;

/*************************
 * There are two ways to calculate the probability of haveing both copies of a gene affected
 * 
 * 1st, 
 * [1 - (1-p1)(1-p2)(1-p3)]^2 
 * 
 * 2nd, 
 * Pr(0, 0, 0) -> (1-p1)^2 * (1-p2)^2 * (1-p3)^2
 * Pr(0, 0, 1) -> (1-p1)^2 * (1-p2)^2 * 2p3(1-p3)
 * Pr(0, 0, 2) -> (1-p1)^2 * (1-p2)^2 * 2(p3)^2 
 * *                   *         *        *
 * Pr(2, 2, 2) -> (p1)^2 * (p2)^2 * (p3)^2
 * 
 * This code is trying to verify whether these two methods would give the same result or not.
 * 
 * Take the first three variants of TTN gene for example:
 * 	(p1 = 9.242e-06; p2 = 8.315e-06; p3 = 1.657e-05;)
 * 
 * @author Jeff
 *
 */
public class D0608_verify_homozysity_equations {
	
	
	public static void main(String[] args){
		
		double p[] = {9.242e-03, 8.315e-03, 1.657e-02};
		
		for(int i=0; i<3; i++){
			System.out.println("p[" + i+ "]= " + p[i]);
		}
		
		//try method 1;
		double method1_pro = 1;
		
		for(int i=0; i<3; i++){
			
			method1_pro *= (1 - p[i]);
			
		}
		
		method1_pro = 1 - method1_pro;
		method1_pro = method1_pro * method1_pro;
		
		System.out.println("\n According to method one, \n The probability of having homozygous would be: " + method1_pro);
		
		
		
		//try method 2;
		System.out.println("\n \n Try method 2. \n");
		//1st, get a matrix of all possible conditions: 000, 001, 002, 010, 011, 020 ------222
		ArrayList<ArrayList<Integer>> combination = getAllCombinations(3);
		
		//printout combnation arrayList
		//printArrayListofArrayList(combination);
		
		//2nd, calculate the probability for each situation
		double total_pro = calculate_Probability_method2(combination, p);
		
		System.out.println("\n \n The total probability of having homozygous would be: " + total_pro);
		
		
		//3rd, remove arrayLists with any '2' in the combination; 
		
		ArrayList<ArrayList<Integer>> homo_List = remove_No2List(combination);
		
		double method2_pro = calculate_Probability_method2(homo_List, p);
		
		System.out.println("\n According to method TWO, \n The probability of having homozygous would be: " + method2_pro/total_pro);
		
	}//end of main();

	
	
	
	
	
	
	private static ArrayList<ArrayList<Integer>> remove_No2List( ArrayList<ArrayList<Integer>> combination) {
		// TODO Auto-generated method stub
		
		ArrayList<ArrayList<Integer>> retList = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0; i<combination.size(); i++){
			
			ArrayList<Integer> tempList = combination.get(i);
			
			boolean with2 = false;
			
			for(int j=0; j<tempList.size(); j++){
				
				if(tempList.get(j) == 2) with2 = true;
			}
			
			if(with2 == true)
				retList.add(tempList);
		}
		
		printArrayListofArrayList(retList);
		
		return retList;
	}


	private static double calculate_Probability_method2( ArrayList<ArrayList<Integer>> combList, double[] p) {
		// TODO Auto-generated method stub
		
		double method2_pro = 0;
		
		for(int i=0; i<combList.size(); i++){
			
			method2_pro += calculate_OneSituation(combList.get(i), p);
		}
		
		System.out.println("Current total probability: " + method2_pro);
		return method2_pro;
		
	}//end of calculate_Probability_method2() method; 

	
	

	private static double calculate_OneSituation(ArrayList<Integer> arrayList,	double[] p) {
		// TODO Auto-generated method stub
		System.out.print("p1=" + p[0] + " p2=" + p[1] + " p3=" + p[2] + " ");
		
		double Probability = 1;
		double currPro = 0;
		
		for(int i=0; i<3; i++){
			System.out.print(" " + arrayList.get(i) +", ");
			
			if(arrayList.get(i) == 0){
				System.out.print("get0");
				currPro = ( (1 - p[i]) * (1 - p[i]) ); 
			
			} else if (arrayList.get(i) == 1) {
				System.out.print("get1");				
				currPro = ( 2 * p[i] * ( 1 - p[i]));
				
			} else if (arrayList.get(i) == 2) {
				System.out.print("get2");
				currPro = ( p[i] * p[i]);
			}
			
			System.out.print(" (" + currPro + ") ");
			
			Probability *= currPro;
		} //end for loop; 
		
		System.out.println(" current Probability: " + currPro);
		
		return Probability;
	}


	private static void printArrayListofArrayList( ArrayList<ArrayList<Integer>> combination) {
		// TODO Auto-generated method stub
		
		for(int i=0; i<combination.size(); i++){
			
			for(int j=0; j<combination.get(i).size(); j++){
				
				System.out.print(" " + combination.get(i).get(j)); 
			}
			
			System.out.println();
		}
		
	}


	/***************
	 * get the combination matrix of m level;
	 * @param m
	 * @return
	 */
	private static ArrayList<ArrayList<Integer>> getAllCombinations(int m) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Integer>> combList = new ArrayList<ArrayList<Integer>>();
		if(m<1)	return combList;
		
		for(int i=0; i<3; i++){
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			tempList.add(i);
			combList.add(tempList);
		}
		//printArrayListofArrayList(combList);
		
		//add combinations for only one elementary 0, 1, and 2.
		for(int i=1; i<m; i++){
			
			combList = add_One_more_level(combList);
			//printArrayListofArrayList(combList);
		}
				
		return combList;
	}//end of getAllCombinations() method;


	private static ArrayList<ArrayList<Integer>> add_One_more_level( ArrayList<ArrayList<Integer>> combList) {
		// TODO Auto-generated method stub

		
		ArrayList<ArrayList<Integer>> newList = new ArrayList<ArrayList<Integer>>();
		
		for( int i=0; i<combList.size(); i++){
			ArrayList<Integer> tempList1 = new ArrayList<Integer>(combList.get(i));
			ArrayList<Integer> tempList2 = new ArrayList<Integer>(combList.get(i));
			ArrayList<Integer> tempList3 = new ArrayList<Integer>(combList.get(i));
			
			tempList1.add(0);
			tempList2.add(1);
			tempList3.add(2);
			
			newList.add(tempList1);
			newList.add(tempList2);
			newList.add(tempList3);
		}
		
		
		return newList;
	}//end add_One_more_level() method; 
	
	
	
}//ee
