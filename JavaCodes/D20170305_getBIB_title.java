package data_manipulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
public class D20170305_getBIB_title {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		D20170305_getBIB_title getBib = new D20170305_getBIB_title();
				
		//create a buffer writer:
		//save the genes without any LoF variants into a txt document
		File output = new File("C:/Users/Jeff/Downloads/bibTemp.txt");
		BufferedWriter out_writer = new BufferedWriter(new FileWriter(output));
		
		System.out.println("Input the title: ");
		// 1. Create a Scanner using the InputStream available.
	    Scanner scanner = new Scanner( System.in );
	    String title = scanner.nextLine(); 
	    scanner.close(); 
		
		System.out.print("#: \t start--- ");
			
			
		//check the title in google scholar
		getBib.run(title);
				
		out_writer.close();
		
		System.out.println("\nCheck the bib txt doc.");
				
		} //end main()

	
	
	/*****************
	 * pass by a value of string, title;
	 * visit scholar.google.com, input the title into searchBox,
	 * check Result table
	 * * Click Cite button
	 * * Click BibTex button
	 * 
	 * @throws InterruptedException
	 */
	private void run(String title) throws InterruptedException {
		// TODO Auto-generated method stub
				
		//the chromedriver.exe has already been placed in the PATH folder
		System.setProperty("webdriver.chrome.driver", "C:/Users/Jeff/Downloads/chromedriver.exe");
				
		WebDriver driver = new ChromeDriver();
				  
		driver.get("https://scholar.google.com/");
		Thread.sleep(1000);  // Let the user actually see something! 
		//Also, this step is very import to make sure the code will export CSV document instead of TMP docs.
			 
		//get the query box by ID: home-searchbox-input
		WebElement searchBox = driver.findElement(By.name("q"));
				  
				  
		//submit the query 
		searchBox.sendKeys( title );
		searchBox.submit();
		
		boolean cite_displayed = isButtenPresent(driver, By.linkText("Cite"));
		
		//in the new page, check the LoF function button
		if( cite_displayed ) {
			
			WebElement cite_button = driver.findElement(By.linkText("Cite"));
			
			//some genes do not have any variants; so in those cases, quit the explor directly; 
	
			//Thread.sleep(1000);  // Let the user actually see something!
			//Also, this step is very import to make sure the code will export CSV document instead of TMP docs.	
			
			
			cite_button.click();
			Thread.sleep(1000);  // Let the user actually see something! 
			
			
			//check if export_to_csv button is visiable;
			boolean bib_displayed = isButtenPresent(driver, By.linkText("BibTeX" ));
			
			//check the bib_txt
			if( bib_displayed) {
				
				WebElement bib_button = driver.findElement(By.linkText("BibTeX"));
				
				bib_button.click(); 
				Thread.sleep(5000); //give user time to copy the bib doc. 
				
				//copy the body txt:
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "a");;
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "c");
				
			} else {
				
				System.out.println("No bibTex link displayed."); 
			}

		} else {
			
			System.out.println("Could not find Cite tag."); 
		}
			
			
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
