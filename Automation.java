package somesh1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

// Import JXL libraries for working with Excel files
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Automation {

	public static void main(String[] args) {
        
        try{
            // Set the system property for the chromedriver executable file
            System.setProperty("webdriver.chrome.driver","C:\\webdriver\\chromedriver.exe");
            
            // Create a new instance of ChromeDriver
            WebDriver driver = new ChromeDriver();
            
            // Open the Excel file for reading
            Workbook readBook = Workbook.getWorkbook(new File("url.xls"));
            
            // Get the sheet named "URL" from the workbook
            Sheet readSheet = readBook.getSheet("URL");
            
            // Get the first cell (0,0) from the sheet and extract the URL string
            Cell cell = readSheet.getCell(0, 0);
            String url = cell.getContents().substring(3);
            
            // Open the URL in the ChromeDriver window and maximize the window
            driver.get(url);
            driver.manage().window().maximize();
            
            // Define a 2D array to store test data for each tab
            String[][] testDataSheet = { { "Tab", "TabPresnt","UrlPresent", "URL","UrlContainTabName" } };
            
            // Create a new Excel workbook for writing the test data
            WritableWorkbook workbook = Workbook.createWorkbook(new File("dataSheet.xls"));
            
            // Create a new sheet named "data" in the workbook
            WritableSheet sheet = workbook.createSheet("data", 0);
            
            // Loop through the test data array and add the column headers to the sheet
            for (int j = 0; j < testDataSheet[0].length; j++) {
                    Label label = new Label(j, 0, testDataSheet[0][j]);
                    sheet.addCell(label);
                }  
            
            // Define a 2D array containing tab names and the words to search for in their URLs
            String[][] tabArray = {
                            {"Home","english"},
                            {"Football","football"},
                            {"Busketball","busketball"},
                            {"Kricket","kriket"},
                            {"Cibersport","cibersport"}
                          };
                          
            // Loop through the tab array and call the getTabDetails method for each tab
            for(int i = 0 ; i < tabArray.length; i++){
                String[] data = getTabDetails(tabArray[i][0],tabArray[i][1],driver);
                for (int j = 0; j < data.length; j++) {
                                Label label = new Label(j, i, data[j]);
                                sheet.addCell(label);
                }
            }
            
            // Write the test data to the Excel workbook and close the workbook and ChromeDriver
            workbook.write();
            workbook.close();
            driver.close();
            
		} catch (Exception e) {
		    System.err.println("Error adding cell to sheet: " + e.getMessage());
        }

	}
    
    // Method for getting details for a specific tab
	// This method takes in three arguments: the name of the tab to be checked, a word to be searched in the URL of the tab, and a WebDriver instance to interact with the browser.
	public static String[] getTabDetails(String tabName,String containWord,WebDriver driver){
	        try {
	            // Find the tab by searching for an <a> element with the specified tabName contained in its text.
	            boolean Tab =  driver.findElement(By.xpath("//a[contains(text(),tabName)]")).isDisplayed();
	          
				// Print a message indicating that the tab is available.
				System.out.println(tabName+" Tab Avilable");
				
				// Construct an XPath expression to find the <a> element with the specified tabName contained in its text.
	            String tabUrlPath =  "//a[contains(text(),'" + tabName + "')]";
				
				// Click on the tab by finding the <a> element with the specified tabName contained in its text and clicking it.
	            driver.findElement(By.xpath(tabUrlPath)).click();
				
				// Get the current URL of the browser.
				String url = driver.getCurrentUrl();
				System.out.println(url);
				
				// Check if the URL contains the specified word. If it does, return an array of Strings containing details about the tab.
				try {
					Assert.assertTrue(url.contains(containWord));
	                String[] data = {  tabName, "TRUE", "TRUE", url,"YES"  };
					System.out.println("Yes "+ containWord +" is presnt in the url");
	                return data;
				}
				// If the URL does not contain the specified word, catch the Assertion Error and return an array of Strings containing details about the tab.
				catch(AssertionError  e) {
	                String[] data =   { tabName, "TRUE","TRUE", url,"NO" } ;
					System.err.println(containWord+" not presnt in the url "+ e.getMessage());
	                return data;
				}	
			}
			// If the tab is not found, catch the Exception and return an array of Strings containing details about the tab.
			catch (Exception e) {
	            String[] data =  { tabName, "FALSE"," - ", " - "," - " } ;
	            System.out.println(tabName+" Tab is not Present");
	            return data;
			}
	    }

}