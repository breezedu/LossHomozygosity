package data_manipulation;

import java.io.FileNotFoundException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/**********************************************
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
public class D0629_tryPullData_from_ExAC {
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		
		D0629_tryPullData_from_ExAC pullData_exac = new D0629_tryPullData_from_ExAC();
		D0627_GetGeneNames_from_ALSData getNames = new D0627_GetGeneNames_from_ALSData();

		
		//initiate 4 genes for testing 
		//String[] geneNames = {"SAMD9", "TNN", "SAMD11", "GPR160", "AGAP8", "AGAP9"}  ; //
		//Here, AGAP8 and AGAP9 do not have LoF variants;
		//gene[689], gene[1558], gene[1559], gene[1560] 1613, 2917 do not have any variants; 
		String[] geneNames = getNames.run(); 		
		
		
		//for each gene name, call pullData_exac.run() method to pull CSV variants document from ExAC
		for(int i=1557; i<geneNames.length; i++){
			
			System.out.print("#: " + i + " \t " + geneNames[i] + " start--- ");
			
			pullData_exac.run(geneNames[i]);
						
			
		}//end for i<geneNames.length loop;
		
		
		
		
		} //end main()

	
	/*****************
	 * pass by a value of string, geneName;
	 * visit ExAC website, input the geneName into searchBox,
	 * check LoF button
	 * Export LoF variants CSV document to local harddrive;
	 * 
	 * @param geneName
	 * @throws InterruptedException
	 */
	private void run(String geneName) throws InterruptedException {
		// TODO Auto-generated method stub
		
		//the chromedriver.exe has already been placed in the PATH folder
		//System.setProperty("webdriver.chrome.driver", "C:/Users/Downloads/chromedriver.exe");
				
		WebDriver driver = new ChromeDriver();
				  
		driver.get("http://exac.broadinstitute.org/");
		Thread.sleep(1000);  // Let the user actually see something! 
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
	
			Thread.sleep(1000);  // Let the user actually see something!
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
					Thread.sleep(1000);		
				
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
