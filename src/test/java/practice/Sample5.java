package practice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Sample5 
{
	public static void main(String[] args) throws Exception
	{
		//connect to existing file in target folder with read
		File f=new File("target\\dummy5.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		int nouc=sh.getRow(0).getLastCellNum();
		//Row max
		for(int i=0;i<nour;i++)  //row wise
		{
			int rowmax=(int) sh.getRow(i).getCell(0).getNumericCellValue();
			for(int j=1;j<nouc;j++) //column wise in every row
			{
				int x=(int) sh.getRow(i).getCell(j).getNumericCellValue();
				if(rowmax<x)
				{
					rowmax=x;
				}
			}
			sh.getRow(i).createCell(nouc).setCellValue(rowmax);
		}
		//column max
		for(int i=0;i<nouc;i++) //column wise
		{
			int colmax=(int) sh.getRow(0).getCell(i).getNumericCellValue();
			for(int j=0;j<nour;j++) //row wise in each column
			{
				int x=(int) sh.getRow(j).getCell(i).getNumericCellValue();
				if(colmax<x)
				{
					colmax=x;
				}
			}
			if(i==0)
			{
				sh.createRow(nour).createCell(i).setCellValue(colmax);
			}
			else
			{
				sh.getRow(nour).createCell(i).setCellValue(colmax);
			}
			sh.autoSizeColumn(i);
		}
		//Take write permission on that file
		FileOutputStream fo=new FileOutputStream(f);
		wb.write(fo);
		wb.close();
		fi.close();
		fo.close();
	}
}
