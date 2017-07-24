package com.k3k.unplat.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * 
 * @author Kennth
 * @version v1.0
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel {
	public static void exportExcel(List<String[]> dataset, OutputStream out) {
        exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
    }

    public static void exportExcel(String[] headers, List<String[]> dataset,
            OutputStream out) {
        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
    }

    public static void exportExcel(String[] headers, List<String[]> dataset,
            OutputStream out, String pattern) {
        exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     * 
     * @param title
     *            表格标题名
     * @param headers
     *            表格属性列名数组
     * @param dataset
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out
     *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern
     *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings({ "deprecation" })
    public static void exportExcel(String title, String[] headers,
    		List<String[]> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        sheet.setColumnWidth(0, 7000);// 设置列宽  
        sheet.setColumnWidth(1, 6000);  
        sheet.setColumnWidth(2, 4000);  
        sheet.setColumnWidth(3, 9200);  
        sheet.setColumnWidth(4, 9200);  
        sheet.setColumnWidth(5, 9200);// 空列设置小一些  
        sheet.setColumnWidth(6, 9200);// 设置列宽 
        sheet.setColumnWidth(8, 6200);// 设置列宽 
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.GREY_80_PERCENT.index);
        font.setFontHeightInPoints((short) 11);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<String[]> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            row.setHeight((short)(20*20));
            String[] t = (String[]) it.next();
//            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
//            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < t.length; i++) {
            	HSSFCell c = row.createCell(i);
            	if(i!=0){
            		c.setCellStyle(style3);
            	}else{
            		c.setCellStyle(style2);
            	}
                c.setCellValue(t[i]);
            }

        }
        try {
            workbook.write(out);
//            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
	public static void exportExcel(String title, String[] headers,
    		List<String[]> dataset, OutputStream out) {
    	// 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        
    	Map<String, List<String[]>> dataMap = new LinkedHashMap<String, List<String[]>>();
    	int index = 0;
    	while(dataset.size() > 65534){
    		index++;
    		dataMap.put(String.valueOf(index), dataset.subList(0, 65534));
    		dataset = dataset.subList(65534, dataset.size());
    	}
    	dataMap.put(String.valueOf(index+1), dataset);
    
    	Iterator it = dataMap.entrySet().iterator();
    	while(it.hasNext()){
    		Map.Entry<String, List<String[]>> entry = (Entry<String, List<String[]>>) it.next();
    		
    		// 生成一个表格
    		String sheetName = title + entry.getKey();
            HSSFSheet sheet = workbook.createSheet(sheetName);
            sheet.setColumnWidth(0, 4000);// 设置列宽  
            sheet.setColumnWidth(1, 8000);  
            sheet.setColumnWidth(2, 7000);  
            sheet.setColumnWidth(3, 7000);  
            sheet.setColumnWidth(4, 7200);  
            sheet.setColumnWidth(5, 7200);// 空列设置小一些  
            sheet.setColumnWidth(6, 3200);// 设置列宽 
            sheet.setColumnWidth(7, 11200);// 设置列宽 
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);
            // 生成一个样式
            HSSFCellStyle style = workbook.createCellStyle();
            // 设置这些样式
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.GREY_80_PERCENT.index);
            font.setFontHeightInPoints((short) 11);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 把字体应用到当前的样式
            style.setFont(font);
            // 生成并设置另一个样式
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(HSSFColor.WHITE.index);
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style2.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            
            HSSFCellStyle style3 = workbook.createCellStyle();
            style3.setFillForegroundColor(HSSFColor.WHITE.index);
            style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style3.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
            style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            style2.setFont(font2);
            
         // 产生表格标题行
            HSSFRow row = sheet.createRow(0);
            for (short i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }

            // 遍历集合数据，产生数据行
            Iterator<String[]> dataIt = entry.getValue().iterator();
            int m = 0;
            while (dataIt.hasNext()) {
                m++;
                row = sheet.createRow(m);
                row.setHeight((short)(20*20));
                String[] t = (String[]) dataIt.next();
//                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
//                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < t.length; i++) {
                	HSSFCell c = row.createCell(i);
                	if(i==6){
                		c.setCellStyle(style3);
                	}else{
                		c.setCellStyle(style2);
                	}
                    c.setCellValue(t[i]);
                }

            }
    	}
    	
    	try {
            workbook.write(out);
//            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static String encodeFileName(HttpServletRequest request,  
            String fileName) throws UnsupportedEncodingException {  
        String agent = request.getHeader("USER-AGENT");  
        if (null != agent && -1 != agent.indexOf("MSIE")) {  
            return URLEncoder.encode(fileName, "UTF-8");  
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {  
            return "=?UTF-8?B?"  
                    + (new String(Base64.encodeBase64(fileName  
                            .getBytes("UTF-8")))) + "?=";  
        } else {  
            return fileName;  
        }  
    }  
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("1");
//		list.add("2");
//		list.add("3");
//		list.add("4");
//		list.add("5");
//		list.add("6");
//		list.add("7");
		Map<String, List<String>> dataMap = new LinkedHashMap<String, List<String>>();
 		int index = 0;
		while(list.size() > 2){
 			index ++ ;
 			dataMap.put(String.valueOf(index), list.subList(0, 2));
 			list = list.subList(2, list.size());
		}
		dataMap.put(String.valueOf(index+1), list);
		
		Iterator it = dataMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry en = (Entry) it.next();
			System.out.println(en.getKey() + " " + en.getValue());
			//for()
		}
	}
}
