package data_manipulation;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class D0623_test_googleRes {
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		
		
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		String[] searchString = {"TTN", "TNN", "SAMD11", "GPR160"}; 
		
		//HtmlPage page = webClient.getPage("http://exac.broadinstitute.org/");
		HtmlPage page = webClient.getPage("https://google.com/");
		
		//getting Form from the home page. tsf is the form name of google.com
		HtmlForm form = page.getHtmlElementById("tsf");
		
		//iterate programming languages to find no.of results
		for(int i=0; i<searchString.length; i++){
			
			//setting programming language name as value in search box in the search home page
			form.getInputByName("q").setValueAttribute(searchString[i]);
			
			//creating a virtual submit button
			HtmlButton submitButton = (HtmlButton)page.createElement("button");
			submitButton.setAttribute("type", "submit");
			form.appendChild(submitButton);
			
			
			//submitting the form and getting the result
			HtmlPage newPage = submitButton.click();
			
			//getting the result as text
			String pageText = newPage.asText();
			String results[] = pageText.split("\n");
			
			
			//getting the lines which contains the no.of results value.
			
			for(int j=0; j<results.length; j++){
				
				if(results[j].contains("About") && results[j].contains("results"))
					System.out.println(searchString[i] + "------" + results[j]);
				
			}//end inner loop for j<results.length;
			
		}//end outter loop for i<searchString.length;
		
		//String pageContent = page.asText(); 
		
		//System.out.println(pageContent);
		
		webClient.close();
	}//end main()
	

}//ee
