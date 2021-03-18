package practice;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sample2 
{
	public static void main(String[] args) throws Exception
	{
		//create a new excel file(.xlsx)
		XSSFWorkbook wb=new XSSFWorkbook();
		Sheet sh=wb.createSheet("Mysheet");
		Row r=sh.createRow(0); //1st row
		Cell c=r.createCell(0); //1st column
		c.setCellValue("Abdul Kalam");
		//Save in HDD
		sh.autoSizeColumn(0); //auto fit on 1st column
		File f=new File("target\\dummy2.xlsx");
		FileOutputStream fo=new FileOutputStream(f);
		wb.write(fo);
		wb.close();
		fo.close();
	}
}
