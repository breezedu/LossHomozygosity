package coursera_java_duke;

public class D0516_homework_4_fun {
	
	public static void main(String[] args){
		
		for(int i=30; i<100; i++){
			
			int sq = i*i;
			
			int F1 = sq%10;
			int F2 = sq%100/10;
			int F3 = sq%1000/100;
			int F4 = sq%10000/1000;
			
			if(F1 == F2 && F3 == F4) 
				System.out.println(" i = " + i + " SQ = " + sq + " F1=" + F1 + " F2=" + F2 + " F3=" + F3 + " F4=" + F4); 
		}
		
	}

}
