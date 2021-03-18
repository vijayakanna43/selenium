package practice;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Sample6 
{
	public static void main(String[] args) throws Exception
	{
		//open a folder and collect contents(sub folders and files) of that folder
		File f1=new File("E:\\batch251");
		File[] l=f1.listFiles();
		//Create new excel file(.xlsx)
		XSSFWorkbook wwb=new XSSFWorkbook(); //.xlsx workbook
		Sheet sh=wwb.createSheet("result");
		sh.createRow(0).createCell(0).setCellValue("Name");
		sh.getRow(0).createCell(1).setCellValue("file/folder");
		sh.getRow(0).createCell(2).setCellValue("file size");
		sh.getRow(0).createCell(3).setCellValue("last modified");
		sh.getRow(0).createCell(4).setCellValue("Hidden");
		//write all files & sub folders information into excel sheet's 2nd row(index=1) onwards
		int rownum=1;
		for(File f:l)
		{
			sh.createRow(rownum).createCell(0).setCellValue(f.getName());
			if(f.isFile())
			{
				sh.getRow(rownum).createCell(1).setCellValue("file");
				double b=f.length();
		        double k=(b/1024);
				sh.getRow(rownum).createCell(2).setCellValue(k+"KB");
			}
			else
			{
				sh.getRow(rownum).createCell(1).setCellValue("folder");
				long b=FileUtils.sizeOfDirectory(f);
				double k=(b/1024);
				sh.getRow(rownum).createCell(2).setCellValue(k+"KB");
			}
			SimpleDateFormat sdf=new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss");
			sh.getRow(rownum).createCell(3).setCellValue(sdf.format(f.lastModified()));
			//Hidden or not
			if(f.isHidden())
			{
				sh.getRow(rownum).createCell(4).setCellValue("Yes");
			}
			else
			{
				sh.getRow(rownum).createCell(4).setCellValue("No");
			}
			rownum=rownum+1; //Mandatory to goto next row in excel sheet
		}
		//Take write permission on that file to save in HDD
		int nouc=sh.getRow(0).getLastCellNum();
		for(int i=0;i<nouc;i++)
		{
			sh.autoSizeColumn(i);
		}
		File f2=new File("target\\Resultbook.xlsx");
		FileOutputStream fo=new FileOutputStream(f2); //write
		wwb.write(fo); //Mandatory to save file
		wwb.close();
		fo.close();
	}
}
