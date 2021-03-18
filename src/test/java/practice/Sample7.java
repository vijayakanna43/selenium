package practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Sample7 
{
	public static void main(String[] args) throws Exception
	{
		//Open an existing excel file(.xlsx) in read mode
		File f=new File("target\\dummy6.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		int nouc=sh.getRow(0).getLastCellNum();
		//create result columns
		sh.getRow(0).createCell(nouc).setCellValue("actual");
		sh.getRow(0).createCell(nouc+1).setCellValue("Result");
		//Data driven(from 2nd row(index=1))
		for(int i=1;i<nour;i++)
		{
			String equation=sh.getRow(i).getCell(0).getStringCellValue();
			double expected=sh.getRow(i).getCell(1).getNumericCellValue();
			//Launch site
			WebDriverManager.chromedriver().setup();
			ChromeDriver driver=new ChromeDriver();
			driver.get("https://www.calculator.net");
			Thread.sleep(5000);
			if(equation.startsWith("cos"))
			{
				driver.findElement(By.xpath("(//span[text()='cos'])[1]")).click();
				Thread.sleep(5000);
			}
			else if(equation.startsWith("sin"))
			{
				driver.findElement(By.xpath("(//span[text()='sin'])[1]")).click();
				Thread.sleep(5000);
			}
			else
			{
				driver.findElement(By.xpath("(//span[text()='tan'])[1]")).click();
				Thread.sleep(5000);
			}
			//Separate value from equation
			String value=equation.substring(4,equation.length()-1);
			//Separate each digit in value to click on related button
			for(int j=0;j<value.length();j++)
			{
				char d=value.charAt(j);
				driver.findElement(By.xpath("(//span[text()='"+d+"'])[1]")).click();
				Thread.sleep(5000);
			}
			driver.findElement(By.xpath("(//span[text()='='])[1]")).click();
			Thread.sleep(5000);
			String temp=driver.findElement(By.id("sciOutPut")).getText();
			temp=temp.trim();
			double actual=Double.parseDouble(temp);
			driver.close();
			sh.getRow(i).createCell(nouc).setCellValue(actual);
			//Validation
			if(expected==actual)
			{
				sh.getRow(i).createCell(nouc+1).setCellValue("Test passed");
			}
			else
			{
				sh.getRow(i).createCell(nouc+1).setCellValue("Test failed");
			}
		}
		//save results
		sh.autoSizeColumn(nouc);
		sh.autoSizeColumn(nouc+1);
		FileOutputStream fo=new FileOutputStream(f); //write
		wb.write(fo); //Mandatory to save file
		wb.close();
		fo.close();
	}
}
