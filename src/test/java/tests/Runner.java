package tests;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import classes.ReusableMethods;

public class Runner 
{
	public static void main(String[] args) throws Exception
	{
		//connect to existing file in target folder with read
		File f=new File("src\\test\\resources\\Book1.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		for(int i=1;i<nour;i++)
		{
			DataFormatter df=new DataFormatter();
			String env=df.formatCellValue(sh.getRow(i).getCell(0));
			String u=df.formatCellValue(sh.getRow(i).getCell(1));
			String bn=df.formatCellValue(sh.getRow(i).getCell(2));
			String v=df.formatCellValue(sh.getRow(i).getCell(3));
			String os=df.formatCellValue(sh.getRow(i).getCell(4));
			String uid=df.formatCellValue(sh.getRow(i).getCell(5));
			String uidc=df.formatCellValue(sh.getRow(i).getCell(6));
			String pwd=df.formatCellValue(sh.getRow(i).getCell(7));
			String pwdc=df.formatCellValue(sh.getRow(i).getCell(8));
			//Create object to ReusableMethods class
			ReusableMethods obj;
			if(env.equalsIgnoreCase("local"))
			{
				obj=new ReusableMethods(bn);
			}
			else
			{
				obj=new ReusableMethods(bn,v,os);
			}
			obj.launchSite(u);
			obj.fillAndValidateLogin(uid, uidc, pwd, pwdc);
			obj.closeSite();
		}
		wb.close();
		fi.close();
	}
}
