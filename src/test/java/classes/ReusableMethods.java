package classes;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ReusableMethods 
{
	//Properties
	private RemoteWebDriver driver;
	private ExtentReports er;
	private ExtentTest et;
	//Constructor methods
	public ReusableMethods(String bn)
	{
		//cross browser in local host
		if(bn.equalsIgnoreCase("chrome"))
		{
			WebDriverManager.chromedriver().setup();
			driver=new ChromeDriver(); 
		}
		else if(bn.equalsIgnoreCase("firefox"))
		{
			WebDriverManager.firefoxdriver().setup();
			driver=new FirefoxDriver(); 
		}
		else if(bn.equalsIgnoreCase("opera"))
		{
			WebDriverManager.operadriver().setup();
			driver=new OperaDriver(); 
		}
		else if(bn.equalsIgnoreCase("edge"))
		{
			WebDriverManager.edgedriver().setup();
			driver=new EdgeDriver(); 
		}
		else if(bn.equalsIgnoreCase("ie"))
		{
			//Set IE browser zoom level to 100%
			WebDriverManager.iedriver().setup();
			driver=new InternetExplorerDriver();
		}
		else
		{
			System.out.println("Wrong browser name");
			System.exit(0); //stop execution forcibly
		}
		//Create object for extent reports to save results in a "html" file
		er=new ExtentReports("target//gmailtestresults.html",false); //append results
		et=er.startTest("Gmail site login testing in Localhost");
	}
	public ReusableMethods(String bn, String version, String os)
	{
		//Give Cloud(Sauce Labs) details
		String USERNAME="batch_250_251";
		String ACCESSKEY="584a1b24-dbf0-4d6d-850a-9c6be854fcf2";
		String uri="http://"+USERNAME+":"+ACCESSKEY+"@ondemand.saucelabs.com:80/wd/hub";
		//Desired Capabilities to give required Test environment details
		try
		{
			DesiredCapabilities dc=new DesiredCapabilities();
			dc.setBrowserName(bn);
			dc.setCapability("version",version); //Browser version
			if(os.equalsIgnoreCase("windows"))
			{
				dc.setCapability("platform",Platform.WIN10); //OS
			}
			else if(os.equalsIgnoreCase("mac"))
			{
				dc.setCapability("platform",Platform.MAC); //OS
			}
			else if(os.equalsIgnoreCase("linux"))
			{
				dc.setCapability("platform",Platform.LINUX); //OS
			}
			else
			{
				System.out.println("Wrong OS name");
				System.exit(0); //stop execution forcibly
			}
			URL u=new URL(uri);
			driver=new RemoteWebDriver(u,dc);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			System.exit(0); //stop execution forcibly
		}
		//Create object for extent reports to save results in a "html" file
		er=new ExtentReports("target//gmailtestresults.html",false); //append results
		et=er.startTest("Gmail site login testing in Cloud");
	}
	//Operational methods
	public void launchSite(String url) throws Exception
	{
		Thread.sleep(5000);
		driver.get(url);
		driver.manage().window().maximize();
		Thread.sleep(5000);
	}
	public void fillAndValidateLogin(String uid, String uc, String p, String pc) 
			                                                             throws Exception
	{
		driver.findElement(By.name("identifier")).sendKeys(uid);
		driver.findElement(By.xpath("//span[text()='Next']/parent::button")).click();
		Thread.sleep(5000);
		try
		{
			if(uc.equalsIgnoreCase("blank") && driver.findElement(
					By.xpath("(//*[contains(text(),'Enter an email')])[2]")).isDisplayed())
			{
					et.log(LogStatus.PASS,"Blank userid test passed");
			}
			else if(uc.equalsIgnoreCase("invalid") && driver.findElement(By.xpath(
					"(//*[contains(text(),'find your Google Account')])[2]")).isDisplayed())
			{
					et.log(LogStatus.PASS,"Invalid userid test passed");
			}
			else if(uc.equalsIgnoreCase("valid") && 
				driver.findElement(By.name("password")).isDisplayed())
			{
				//Password testing
				driver.findElement(By.name("password")).sendKeys(p);
				driver.findElement(By.xpath("//span[text()='Next']/parent::button")).click();
				Thread.sleep(5000);
				if(pc.equalsIgnoreCase("blank") && 
						driver.findElement(By.xpath("//*[text()='Enter a password']"))
						                                              .isDisplayed())
				{
					et.log(LogStatus.PASS,"Blank Password test passed");
				}
				else if(pc.equalsIgnoreCase("invalid") && 
						driver.findElement(By.xpath(
						"//*[contains(text(),'Wrong password') or "
						+ "contains(text(),'Your password was changed')]")).isDisplayed())
				{
					et.log(LogStatus.PASS,"Invalid password test passed");
				}
				else if(uc.equalsIgnoreCase("valid") && 
					driver.findElement(By.xpath("//*[text()='Compose']")).isDisplayed())
				{
					et.log(LogStatus.PASS,"Login test passed for valid credentials");
				}
				else  //password testing else
				{
					//Take screenshot
					et.log(LogStatus.FAIL,
							"Password test failed"+et.addScreenCapture(screenshot()));
				}
			}
			else  //userid testing else
			{
				//Take screenshot
				et.log(LogStatus.FAIL,"Userid test failed and try to watch "
				                                     +et.addScreenCapture(screenshot()));
			
			}
		}
		catch(Exception ex)
		{
			//Take screenshot
			et.log(LogStatus.FAIL,ex.getMessage()+et.addScreenCapture(screenshot()));
		}
	}	
	public void closeSite()
	{
		driver.close();
		er.flush(); //save results
		er.endTest(et);
	}
	private String screenshot() throws Exception
	{
		SimpleDateFormat sf=new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
		Date dt=new Date();
		String fn=sf.format(dt)+".png";
		File dest=new File(fn);
		File src=driver.getScreenshotAs(OutputType.FILE);
		FileHandler.copy(src,dest);
		return(dest.getAbsolutePath());
	}
}
