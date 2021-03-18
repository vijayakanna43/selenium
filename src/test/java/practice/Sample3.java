package practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Sample3 
{
	public static void main(String[] args) throws Exception
	{
		//Open an existing excel file(.xlsx) in read mode
		File f=new File("target\\dummy3.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		//Create 3rd column in 1st row for output
		sh.getRow(0).createCell(2).setCellValue("Output");
		//Data driven from 2nd row(index=1) to last row(index=nour-1)
		//Skip 1st row(index=0), because it have names to columns
		for(int i=1;i<nour;i++)
		{
			int x=(int) sh.getRow(i).getCell(0).getNumericCellValue();
			int y=(int) sh.getRow(i).getCell(1).getNumericCellValue();
			int z=x+y;
			sh.getRow(i).createCell(2).setCellValue(z);
		}
		//Save changes of excel file in HDD
		sh.autoSizeColumn(2); //auto fit on 3rd column
		FileOutputStream fo=new FileOutputStream(f); //write mode
		wb.write(fo);
		wb.close();
		fo.close();
	}
}
