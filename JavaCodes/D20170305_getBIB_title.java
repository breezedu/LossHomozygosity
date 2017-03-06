package data_manipulation;

import java.io.IOException;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/*****************************************************************************************
 * When writing a manuscript or a proposal, I don't think anybody would like to edit
 * the citations manually, so I wrote a short Java code to help 'pull' the BibTeX 
 * format citation from Google scholar directly.
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
 * The problem is that I don't know how to integrate it into Tex editor. 
 * 
 * @author Jeff
 *
 */
public class D20170305_getBIB_title {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		D20170305_getBIB_title getBib = new D20170305_getBIB_title();
				
		System.out.println("Input the title: ");
		// 1. Create a Scanner using the InputStream available.
	    Scanner scanner = new Scanner( System.in );
	    String title = scanner.nextLine(); 
	    scanner.close(); 
		
		System.out.print("#: \t start--- ");			
			
		//check the title in google scholar
		getBib.run(title);
		
		System.out.println("\nCheck the bib txt doc.");
				
		} //end main()

	
	
	/*****************
	 * pass by a value of string, title;
	 * visit scholar.google.com, input the title into searchBox,
	 * check Result table
	 * * Click Cite button
	 * * Click BibTex button
	 * * Control + a to select all text in the page
	 * * Control + c to copy the text into OS copyboard
	 * 
	 * @throws InterruptedException
	 */
	private void run(String title) throws InterruptedException {
		// TODO Auto-generated method stub
				
		//If the chromedriver.exe has already been placed in the PATH folder, then cancel this line.
		System.setProperty("webdriver.chrome.driver", "C:/Users/Jeff/Downloads/chromedriver.exe");
				
		WebDriver driver = new ChromeDriver();
		
		//open google scholar page.
		driver.get("https://scholar.google.com/");
		Thread.sleep(1000);  // Let the user actually see something! 
		//Also, this step is very import to make sure the code will export CSV document instead of TMP docs.
			 
		//get the query box by ID: q
		WebElement searchBox = driver.findElement(By.name("q"));
				  
				  
		//submit the query 
		searchBox.sendKeys( title );
		searchBox.submit();
		
		boolean cite_displayed = isButtenPresent(driver, By.linkText("Cite"));
		
		//in the new page, check the Cite button/linkText.
		if( cite_displayed ) {
			
			WebElement cite_button = driver.findElement(By.linkText("Cite"));
			
			//click the Cite button.
			cite_button.click();
			Thread.sleep(1000);  // Let the user actually see something! 
			
			
			//check if BibText button is visiable;
			boolean bib_displayed = isButtenPresent(driver, By.linkText("BibTeX" ));
			
			//check the bib_txt
			if( bib_displayed) {
				
				WebElement bib_button = driver.findElement(By.linkText("BibTeX"));
				
				//click the bibTeX button.
				bib_button.click(); 
				Thread.sleep(1000); //give user time to copy the bib doc. 
				
				//select and copy the body txt:
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "a");;
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "c");
				
			} else {
				
				System.out.println("No bibTex link displayed."); 
			}//end if-else bib_displayed condition. 
			

		} else {
			
			System.out.println("Could not find Cite tag."); 
		}//end if -else Cite displayed condition. 
			
		//quit the chrome driver. 	
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
	}//end isButtonPresent() method. 

}//ee 
