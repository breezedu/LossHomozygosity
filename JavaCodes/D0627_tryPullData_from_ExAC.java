package data_manipulation;

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
 * 
 * @author Jeff
 *
 */
public class D0627_tryPullData_from_ExAC {
	
	public static void main(String[] args) throws InterruptedException {
		
		//the chromedriver.exe has already been placed in the PATH folder
		//System.setProperty("webdriver.chrome.driver", "C:/Users/Downloads/chromedriver.exe");
		
		  WebDriver driver = new ChromeDriver();
		  
		  driver.get("http://exac.broadinstitute.org/");
		  Thread.sleep(5000);  // Let the user actually see something!
		  
		  //get the query box by ID: home-searchbox-input
		  WebElement searchBox = driver.findElement(By.id("home-searchbox-input"));
		  
		  
		  //submit the query 
		  searchBox.sendKeys("SAMD11");
		  searchBox.submit();
		  
		  //in the new page, click the LoF function button
		  WebElement lof_button = driver.findElement(By.id("consequence_lof_variant_button"));
		  lof_button.click();
		  
		  
		  //trigger the Export table to CSV button
		  WebElement ExportCSV_button = driver.findElement(By.id("export_to_csv"));
		  ExportCSV_button.click(); 
		  
		  Thread.sleep(50000);  // Let the user actually see something!
		  driver.quit();
		
		} //end main()

}//ee 
