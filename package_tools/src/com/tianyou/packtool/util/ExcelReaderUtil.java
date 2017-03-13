package com.tianyou.packtool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShapeFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelReaderUtil {

	
	public static void main(String[] args) {
		Map<String, List<String>> readChannelInfo = readChannelInfo(new File("D:\\install_package\\apache-tomcat-7.0.63-windows-x64\\apache-tomcat-7.0.63\\webapps\\packing\\WEB-INF\\keystone_info\\keystone_info.xls"));
		System.out.println(readChannelInfo);
	}
	
	
	/**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
//	public static String[] readExcelTitle(File file){
//		POIFSFileSystem fs = null;
//		HSSFWorkbook wb = null;
//		HSSFSheet sheet = null;
//		HSSFRow row = null;
//		InputStream is;
//		try {
//			is = new FileInputStream(file);
//			fs = new POIFSFileSystem(is);
//			wb = new HSSFWorkbook(fs);
//			sheet = wb.getSheetAt(0);
//			row = sheet.getRow(0);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		sheet = wb.getSheetAt(0);
//		row = sheet.getRow(0);
//		// 标题总列数
//		int colNum = row.getPhysicalNumberOfCells();
//		System.out.println("colNum == "+colNum);
//		String[] title = new String[colNum];
//		for (int i=0;i<colNum;i++){
//			title[i] = getCellFormatValue(row.getCell((short)i));
//		}
//		return title;
//	}
	
	
	 /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
	public static Map<Integer,String> readExcelContent(File file){
		Map<String, List<String>> excelInfo = new HashMap<String, List<String>>();
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<Integer,String> content = new HashMap<Integer,String>();
		String str = "";
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		List<String> keyList = new ArrayList<String>();
		List<List<String>> valueList = new ArrayList<List<String>>();
		// 正文应该从第二行开始，第一行为表头的标题
		for (int i=0;i<=rowNum;i++){
			row = sheet.getRow(i);
			int j = 0;
			while (j<colNum){
				HSSFCell cell = row.getCell(j);
				String cellValue = getStringCellValue(cell);
				if (i == 0) {
					keyList.add(cellValue);
					valueList.add(new ArrayList<String>());
				} else {
					valueList.get(j).add(cellValue);
				}
				
				/**
				 * 每个单元格的内容用"-"分割开,以后需要时用String类的replace方法还原数据
				 * 也可以将每个单元格的数据设置到一个javabean的属性中,此时需要创建一个javabean
				 * str += getStringCellValue(row.getCell((short) j)).trim() +"-";
				 */
//				str += getCellFormatValue(row.getCell((short) j)).trim()+"-";//+ "    ";
				
				str += cellValue+"-";
				j++;
			}
			content.put(i,str);
			str = "";
		}
		for (int i = 0; i < keyList.size(); i++) {
			excelInfo.put(keyList.get(i), valueList.get(i));
		}
		return content;
		
	}
	
	
	public static Map<String,List<String>> readChannelInfo(File file){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String,List<String>> content = new HashMap<String,List<String>>();
		
		List<String> channelNames = new ArrayList<String>();
		List<String> channelIds = new ArrayList<String>();
		String str = "";
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		
		HSSFRow keyRow = sheet.getRow(0);
		for (int m=0;m<colNum;m++){
			HSSFCell cell = keyRow.getCell(m);
			String cellValue = getStringCellValue(cell);
			str += cellValue+"-";
		}
		
		String channelName = str.split("-")[0];
		String channelId = str.split("-")[1];
		// 正文应该从第二行开始，第一行为表头的标题
		for (int i=0;i<=rowNum;i++){
			row = sheet.getRow(i);
			int j = 0;
			while (j<colNum){
				/**
				 * 每个单元格的内容用"-"分割开,以后需要时用String类的replace方法还原数据
				 * 也可以将每个单元格的数据设置到一个javabean的属性中,此时需要创建一个javabean
				 * str += getStringCellValue(row.getCell((short) j)).trim() +"-";
				 */
//				str += getCellFormatValue(row.getCell((short) j)).trim()+"-";//+ "    ";
				HSSFCell cell = row.getCell(j);
				String cellValue = getStringCellValue(cell);
				str += cellValue+"-";
				j++;
			}
			channelNames.add(str.split("-")[0]);
			channelIds.add(str.split("-")[1]);
			str = "";
		}
		content.put(channelName,channelNames); 
		content.put(channelId,channelIds);
		return content;
		
	}
	
	/**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
	public static Map<String, List<String>> readExcelContent2(File file){
		Map<String, List<String>> excelInfo = new HashMap<String, List<String>>();
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		List<String> keyList = new ArrayList<String>();
		List<List<String>> valueList = new ArrayList<List<String>>();
		// 正文应该从第二行开始，第一行为表头的标题
		for (int i=0;i<=rowNum;i++){
			row = sheet.getRow(i);
			int j = 0;
			while (j<colNum){
				HSSFCell cell = row.getCell(j);
				String cellValue = getStringCellValue(cell);
				if (i == 0) {
					keyList.add(cellValue);
					valueList.add(new ArrayList<String>());
				} else {
					valueList.get(j).add(cellValue);
				}
				j++;
			}
		}
		for (int i = 0; i < keyList.size(); i++) {
			excelInfo.put(keyList.get(i), valueList.get(i));
		}
		return excelInfo;
		
	}
	
	/**
     * 获取单元格数据内容为字符串类型的数据
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
	private static String getStringCellValue(HSSFCell cell){
		String strCell = "";
		switch(cell.getCellType()){
		
			case HSSFCell.CELL_TYPE_STRING:
	            strCell = cell.getStringCellValue();
	            break;
	        case HSSFCell.CELL_TYPE_NUMERIC:
	            strCell = String.valueOf((int)cell.getNumericCellValue());
	            break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:
	            strCell = String.valueOf(cell.getBooleanCellValue());
	            break;
	        case HSSFCell.CELL_TYPE_BLANK:
	            strCell = "";
	            break;
	        default:
	            strCell = "";
	            break;
		}
		if (strCell.equals("") || strCell == null) {
	            return "";
        }
        if (cell == null) {
            return "";
        }
		return strCell;
	}
	
	/**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
	
	private String getDateCellValue(HSSFCell cell){
		String result = "";
		try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
		return result;
	}
	
//	 /**
//     * 根据HSSFCell类型设置数据
//     * @param cell
//     * @return
//     */
//	private static String getCellFormatValue(HSSFCell cell){
//		String result = "";
//		try{
//			int cellType = cell.getCellType();
//			if (cellType == HSSFCell.CELL_TYPE_NUMERIC){
//				Date date = cell.getDateCellValue();
//				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
//	                    + "-" + date.getDate();
//			} else if (cellType == HSSFCell.CELL_TYPE_STRING){
//				String date = getStringCellValue(cell);
//	            result = date.replaceAll("[年月]", "-").replace("日", "").trim();
//			} else if (cellType == HSSFCell.CELL_TYPE_BLANK){
//				result = "";
//			}
//		} catch(Exception e){
//			System.out.println("日期格式不正确!");
//            e.printStackTrace();
//		}
//		return result;
//	}
//	
//	public static void writeExcel(String filePath,String alias,String name,String password) throws Exception{
//		FileInputStream fs=new FileInputStream(filePath);  //获取d://test.xls  
//        POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
//        HSSFWorkbook wb=new HSSFWorkbook(ps);    
//        HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表  
//        HSSFRow row=sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值  
//        System.out.println(sheet.getLastRowNum()+" "+row.getLastCellNum());  //分别得到最后一行的行号，和一条记录的最后一个单元格  
//          
//        FileOutputStream out=new FileOutputStream(filePath);  //向d://test.xls中写数据  
//        row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据  
//        row.createCell(0).setCellValue(alias); //设置第一个（从0开始）单元格的数据  
//        row.createCell(1).setCellValue(name); //设置第二个（从0开始）单元格的数据  
//        row.createCell(2).setCellValue(password);
//  
//        out.flush();  
//        wb.write(out);    
//        out.close();    
//        System.out.println(row.getPhysicalNumberOfCells()+" "+row.getLastCellNum());  
//	}
	
	public static Map<String,String> searchKeystone(String filePath,String name){
		File file = new File(filePath);
		Map<Integer, String> content = ExcelReaderUtil.readExcelContent(file);
		System.out.println("content:" + content);
		Iterator<Integer> iterator = content.keySet().iterator();
		while (iterator.hasNext()){
			Integer key = iterator.next();
			String value = content.get(key);
			String[] splits = value.split("-");
			System.out.println(value);
			if (splits[0].equals(name)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", splits[0]);
				map.put("password",splits[1]);
				map.put("alias",splits[2]);
				return map;
			}
		}
		return null;
	}
	
}
