package com.open.qa.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 读取Excel数据驱动的工具类
 *
 */
public class ExcelUtil {
    private String filePath, errorInfo;
    private int totalRows = 0, totalCells = 0;

    public ExcelUtil(String filePath) {
        this.filePath = filePath;
    }

    private static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    private static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public boolean validateExcel(String filePath) {
        if ((filePath == null)
                || ((!isExcel2003(filePath)) && (!isExcel2007(filePath)))) {
            this.errorInfo = "文件名不是excel格式";
            return false;
        }

        File file = new File(filePath);
        if ((file == null) || (!file.exists())) {
            this.errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    /**
     * 按竖行方式读取数据
     *
     * @param sheetName
     * @return
     */
    public List<Map<String, String>> readDataByRow(String sheetName) {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        List<List<String>> lists = readBySheetName(sheetName);

        List<String> listTitel = new ArrayList<String>();
        // 获取标题
        for (int j = 0; j < lists.size(); j++) {
            listTitel.add(lists.get(j).get(0));
        }
        // 获取数据
        for (int i = 1; i < lists.get(0).size(); i++) {
            Map<String, String> dataMap = new HashMap<String, String>();
            if (!"".equals(lists.get(0).get(i))) {
                for (int k = 0; k < listTitel.size(); k++) {
                    dataMap.put(listTitel.get(k), lists.get(k).get(i));
                }
                datas.add(dataMap);
            }
        }
        return datas;
    }

    public static List<Map<String, String>> reflectMapList(List<List<String>> list) {
        List<Map<String, String>> mlist = new ArrayList();
        Map<String, String> map = new HashMap();
        if (list != null) {
            for (int i = 1; i < list.size(); i++) {
                map = new HashMap();
                List<String> cellList = (List) list.get(i);
                for (int j = 0; j < cellList.size(); j++) {
                    map.put((String) ((List) list.get(0)).get(j),(String) cellList.get(j));
                }
                mlist.add(map);
            }
        }
        return mlist;
    }

    public List<Map<String, String>> excelDatas(String sheetName) {
        List<List<String>> lists = readBySheetName( sheetName);
        List<Map<String, String>> datas = reflectMapList(lists);
        return datas;
    }

    public List<List<String>> readBySheetName(String sheetName) {
        List<List<String>> dataLst = new ArrayList();
        InputStream is = null;
        if (!validateExcel(filePath)) {
            System.out.println(this.errorInfo);
            return null;
        }
        boolean isExcel2003 = true;
        if (isExcel2007(filePath)) {
            isExcel2003 = false;
        }
        try {
            File file = new File(filePath);
            is = new FileInputStream(file);
            Workbook wb;
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {
                wb = new XSSFWorkbook(is);
            }
            dataLst = readWorkbookBySheetName(wb, sheetName);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataLst;
    }

    private List<List<String>> readWorkbookBySheetName(Workbook wb, String sheetName) {
        int sheetIndex = 0;
        try {
            sheetIndex = wb.getSheetIndex(sheetName);
        } catch (Exception localException) {
        }
        if (sheetIndex < 0) {
            sheetIndex = 0;
        }
        return readWorkbookBySheetIndex(wb, sheetIndex);
    }

    private List<List<String>> readWorkbookBySheetIndex(Workbook wb, int sheetIndex) {
        List<List<String>> dataLst = new ArrayList();

        Sheet sheet = wb.getSheetAt(sheetIndex);

        this.totalRows = sheet.getPhysicalNumberOfRows();
        if ((this.totalRows >= 1) && (sheet.getRow(0) != null)) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                List<String> rowLst = new ArrayList();
                for (int c = 0; c < totalCells; c++) {
                    Cell cell = row.getCell(c);

                    String cellValue = "";
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case 0:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();

                                    cellValue = DateUtil.dateToStr(date,
                                            "yyyy-MM-dd HH:mm:ss").toString();
                                    System.out.println(cellValue);
                                } else {
                                    Integer num = new Integer(
                                            (int) cell.getNumericCellValue());
                                    cellValue = String.valueOf(num);
                                }
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue().trim();
                                break;
                            case 4:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case 2:
                                cellValue = cell.getCellFormula();
                                break;
                            case 3:
                                cellValue = "";
                                break;
                            case 5:
                                cellValue = "非法字符";
                                break;
                            default:
                                cellValue = "未知类型";
                        }
                    }
                    rowLst.add(cellValue);
                }
                dataLst.add(rowLst);
            }
        }
        return dataLst;
    }


}
