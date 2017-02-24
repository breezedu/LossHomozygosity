package JavaCodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**********************************************
 * Our work focus on genes in ALS dataset
 * 1st, we create two objects, getNames and pullData_exac
 * 2nd, get all gene names from ALS dataset, put them into one array list
 * 3rd, pull variants data from ExAC for each gene
 * 
 * import selenium jars
 * download selenium-java-2.53.0.zip from 
 * 		http://www.seleniumhq.org/download/
 * 
 * in eclipse
 * import all jars (selenium-java-2.53.0-srcs.jar selenium-java-2.53.0.jar AND all others)
 * into the build-PATH, as external jars. 
 * 
 * sample code from:
 * 		https://sites.google.com/a/chromium.org/chromedriver/getting-started
 * 
 * The problem is that we can not save the document in "save as" way.
 * The CSV document will be downloaded automatically into C:/Users/Jeff/Downloads/ directory;
 * 
 * @author Jeff
 *
 */
public class D20170224_tryPullData_from_ExAC {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		D20170224_tryPullData_from_ExAC pullData_exac = new D20170224_tryPullData_from_ExAC();
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();

		
		//initiate 4 genes for testing 
		//String[] geneNames = {"SAMD9", "TNN", "SAMD11", "GPR160", "AGAP8", "AGAP9"}  ; //
		//Here, AGAP8 and AGAP9 do not have LoF variants;
		//gene[689], gene[1558], gene[1559], gene[1560] 1613, 2917 do not have any variants; 
		String[] geneNames = getNames.run(); 		
		
		
		//initiate an arrayList to store all genes that could not be downloaded
		ArrayList<String> genes_without_LoF = new ArrayList<String>();
		
		
		//create a buffer writer:
		//save the genes without any LoF variants into a txt document
		File output = new File("D:/data/ExAC/0to5000_genes_without_Lof.txt");
		BufferedWriter out_writer = new BufferedWriter(new FileWriter(output));
		
		//for each gene name, call pullData_exac.run() method to pull CSV variants document from ExAC
		for(int i=0; i<5000; i++){
			
			System.out.print("#: " + i + " \t " + geneNames[i] + " start--- ");
			
			
			//cll pullData_exac.run() method, update the arrayList, genes withoug LoF variants will be put into ArrayList
			pullData_exac.run(geneNames[i], genes_without_LoF);
						
			if(genes_without_LoF.size() > 0){
				
				//write the gene into a txt document;
				out_writer.write("gene # " + i + " \t " + genes_without_LoF.get(0) + "\n" );
				
				//empty the arrayList
				genes_without_LoF.clear();
				
			}
			
			
			//rename the last CSV file downloaded with the gene_name
			pullData_exac.reName_lastCSV(geneNames[i]);
			
			}//end for i<geneNames.length loop;
		
		
		out_writer.close();
		System.out.println("\nFor the first 3000 genes. \n"
							+ "There are: " + genes_without_LoF.size() + " genes do not have LoF variants.");
		
		
		//printout genes without LoF variants
		//check the txt document: C:/Users/Jeff/Downloads/0to5000_genes_without_Lof.txt.
		
		} //end main()

	
	
	/**************
	 * 
	 * @param string
	 */
	private void reName_lastCSV(String geneName) {
		// TODO Auto-generated method stub
		
		File folder = new File("C:/Users/jeffd/Downloads");
		File[] CSV_list = folder.listFiles();
		
		
		//1st, sort all files
		Arrays.sort(CSV_list, new Comparator<File>(){
			
			public int compare(File f1, File f2){
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified() );				
				
			}
			
		});
		
		//2nd reName the last one:
		int last = CSV_list.length;
		File new_file = new File("C:/Users/jeffd/Downloads/" + CSV_list[last-1].getName() );
		new_file.renameTo( new File("C:/Users/jeffd/Downloads/" + geneName +"_LoF.CSV") );
		
	}


	/*****************
	 * pass by a value of string, geneName;
	 * visit ExAC website, input the geneName into searchBox,
	 * check LoF button
	 * Export LoF variants CSV document to local harddrive;
	 * 
	 * @param geneName
	 * @param genes_LoF 
	 * @throws InterruptedException
	 */
	private void run(String geneName, ArrayList<String> genes_LoF) throws InterruptedException {
		// TODO Auto-generated method stub
		
		//add geneName into ArrayList, removed if the LoF variants CSV document is downloaded
		genes_LoF.add(geneName);
		
		//the chromedriver.exe has already been placed in the PATH folder
		System.setProperty("webdriver.chrome.driver", "C:/Users/jeffd/Downloads/chromedriver_win32/chromedriver.exe");
				
		WebDriver driver = new ChromeDriver();
				  
		driver.get("http://exac.broadinstitute.org/");
		//Thread.sleep(1000);  // Let the user actually see something! 
		//Also, this step is very import to make sure the code will export CSV document instead of TMP docs.
			 
		//get the query box by ID: home-searchbox-input
		WebElement searchBox = driver.findElement(By.id("home-searchbox-input"));
				  
				  
		//submit the query 
		searchBox.sendKeys( geneName );
		searchBox.submit();
		
		boolean variants_displayed = isButtenPresent(driver, By.id( "consequence_lof_variant_button" ));
		
		//in the new page, check the LoF function button
		if( variants_displayed ) {
			
			WebElement lof_button = driver.findElement(By.id("consequence_lof_variant_button"));
			
			//some genes do not have any variants; so in those cases, quit the explor directly; 
	
			//Thread.sleep(1000);  // Let the user actually see something!
			//Also, this step is very import to make sure the code will export CSV document instead of TMP docs.	
			
			
			lof_button.click();
			
			//check if export_to_csv button is visiable;
			boolean lof_displayed = isButtenPresent(driver, By.id( "export_to_csv" ));
			
			if( lof_displayed ){
				
				//trigger the Export table to CSV button
				WebElement ExportCSV_button = driver.findElement(By.id("export_to_csv"));
				
				//System.out.println(ExportCSV_button + " ? ");
				//some genes may not have any LoF variants, in that case, we have to check if the button is displayed or not;
				
				if(ExportCSV_button.isDisplayed()){
					
					ExportCSV_button.click(); 
					Thread.sleep(2000);		
					
					//remove the last gene name from ArrayList; 
					genes_LoF.remove(genes_LoF.size()-1);
					
					System.out.println( " downloaded.");
					//System.out.println(" The gene " + geneName + " does not have any LoF variants.");
				
				}  else {
					
					System.out.println("gene " + geneName + " does not have LoF variants." );
				}

				
			} else {
				
				System.out.println("gene " + geneName + " does not have LoF variants." );
				
			}//end inner if export_to_csv size != 0;

			
		} else {
			
			System.out.println("gene " + geneName + " does not have variants.");
			
		}//end outter in LoF button size != 0;
				
			
			
		driver.quit();

		
	} //end run() method;


	/************
	 * Check if a web element is displayed
	 * 
	 * @param driver
	 * @param id
	 * @return
	 */
	private boolean isButtenPresent(WebDriver driver, By id) {
		// TODO Auto-generated method stub
		
		try {
			driver.findElement(id);
			return true;
		}
		catch (org.openqa.selenium.NoSuchElementException e){
			return false;
		}
	}

}//ee 
