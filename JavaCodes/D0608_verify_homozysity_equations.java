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
		//System.out.print("p1=" + p[0] + " p2=" + p[1] + " p3=" + p[2] + " ");
		
		double Probability = 1;
		double currPro = 0;
		
		for(int i=0; i<3; i++){
			System.out.print(" " + arrayList.get(i) +", ");
			
			if(arrayList.get(i) == 0){
			//	System.out.print("get0");
				currPro = ( (1 - p[i]) * (1 - p[i]) ); 
			
			} else if (arrayList.get(i) == 1) {
			//	System.out.print("get1");				
				currPro = ( 2 * p[i] * ( 1 - p[i]));
				
			} else if (arrayList.get(i) == 2) {
			//	System.out.print("get2");
				currPro = ( p[i] * p[i]);
			}
			
			System.out.print(" (" + currPro + ") ");
			
			Probability *= currPro;
		} //end for loop; 
		
		System.out.println(" current Probability: " + Probability);
		
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
	
	/*************************
	 * 
p[0]= 0.009242
p[1]= 0.008315
p[2]= 0.01657

 According to method one, 
 The probability of having homozygous would be: 0.0011397718084950204

 
 Try method 2. 

 0,  (0.9816014145640001)  0,  (0.983439139225)  0,  (0.9671345649)  current Probability: 0.933618758531293
 0,  (0.9816014145640001)  0,  (0.983439139225)  1,  (0.032590870200000004)  current Probability: 0.03146144174748284
 0,  (0.9816014145640001)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 2.650499220868749E-4
 0,  (0.9816014145640001)  1,  (0.01649172155)  0,  (0.9671345649)  current Probability: 0.015656261771001278
 0,  (0.9816014145640001)  1,  (0.01649172155)  1,  (0.032590870200000004)  current Probability: 5.275906928718692E-4
 0,  (0.9816014145640001)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 4.444738202458169E-6
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 6.56366772845589E-5
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 2.2118498370095304E-6
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 1.8633940290233124E-8
 1,  (0.018313170872)  0,  (0.983439139225)  0,  (0.9671345649)  current Probability: 0.017417986160790443
 1,  (0.018313170872)  0,  (0.983439139225)  1,  (0.032590870200000004)  current Probability: 5.869579546776032E-4
 1,  (0.018313170872)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 4.944883371977612E-6
 1,  (0.018313170872)  1,  (0.01649172155)  0,  (0.9671345649)  current Probability: 2.920898368473307E-4
 1,  (0.018313170872)  1,  (0.01649172155)  1,  (0.032590870200000004)  current Probability: 9.842954956753951E-6
 1,  (0.018313170872)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 8.292291451013952E-8
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 1.224545593301075E-6
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 4.1265205415736385E-8
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 3.4764266584238433E-10
 2,  (8.5414564E-5)  0,  (0.983439139225)  0,  (0.9671345649)  current Probability: 8.123932791762735E-5
 2,  (8.5414564E-5)  0,  (0.983439139225)  1,  (0.032590870200000004)  current Probability: 2.7376339212655406E-6
 2,  (8.5414564E-5)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 2.3063458545788727E-8
 2,  (8.5414564E-5)  1,  (0.01649172155)  0,  (0.9671345649)  current Probability: 1.3623378625976423E-6
 2,  (8.5414564E-5)  1,  (0.01649172155)  1,  (0.032590870200000004)  current Probability: 4.59085819697242E-8
 2,  (8.5414564E-5)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 3.867612352878853E-10
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 5.7114100382174746E-9
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 1.9246527832842918E-10
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 1.621442126995349E-12
Current total probability: 1.0000000000000002

 
 The total probability of having homozygous would be: 1.0000000000000002
 0 0 2
 0 1 2
 0 2 0
 0 2 1
 0 2 2
 1 0 2
 1 1 2
 1 2 0
 1 2 1
 1 2 2
 2 0 0
 2 0 1
 2 0 2
 2 1 0
 2 1 1
 2 1 2
 2 2 0
 2 2 1
 2 2 2
 0,  (0.9816014145640001)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 2.650499220868749E-4
 0,  (0.9816014145640001)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 4.444738202458169E-6
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 6.56366772845589E-5
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 2.2118498370095304E-6
 0,  (0.9816014145640001)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 1.8633940290233124E-8
 1,  (0.018313170872)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 4.944883371977612E-6
 1,  (0.018313170872)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 8.292291451013952E-8
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 1.224545593301075E-6
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 4.1265205415736385E-8
 1,  (0.018313170872)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 3.4764266584238433E-10
 2,  (8.5414564E-5)  0,  (0.983439139225)  0,  (0.9671345649)  current Probability: 8.123932791762735E-5
 2,  (8.5414564E-5)  0,  (0.983439139225)  1,  (0.032590870200000004)  current Probability: 2.7376339212655406E-6
 2,  (8.5414564E-5)  0,  (0.983439139225)  2,  (2.7456490000000007E-4)  current Probability: 2.3063458545788727E-8
 2,  (8.5414564E-5)  1,  (0.01649172155)  0,  (0.9671345649)  current Probability: 1.3623378625976423E-6
 2,  (8.5414564E-5)  1,  (0.01649172155)  1,  (0.032590870200000004)  current Probability: 4.59085819697242E-8
 2,  (8.5414564E-5)  1,  (0.01649172155)  2,  (2.7456490000000007E-4)  current Probability: 3.867612352878853E-10
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  0,  (0.9671345649)  current Probability: 5.7114100382174746E-9
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  1,  (0.032590870200000004)  current Probability: 1.9246527832842918E-10
 2,  (8.5414564E-5)  2,  (6.913922499999999E-5)  2,  (2.7456490000000007E-4)  current Probability: 1.621442126995349E-12
Current total probability: 4.2907035007906213E-4

 According to method TWO, 
 The probability of having homozygous would be: 4.29070350079062E-4

	 */
	
}//ee 
