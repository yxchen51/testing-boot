package com.open.qa.common;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.qa.template.ServiceTemplate;
import com.open.qa.template.TestTemplate;
import com.open.qa.utils.ClassUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : chenliang@tsfinance.com
 * create in 2018/7/17 下午3:01
 */
public class TestingCodeFactory {

    /**
     * 根据包名生成测试代码
     * @param packageName
     */
    public static void genTestingCode(String packageName)throws Exception{
        List<Class<?>> classList= ClassUtil.getClasses(packageName);
        classList = classList.stream().filter(clazz -> clazz.isInterface()).collect(Collectors.toList());
        classList.stream().forEach(aClass -> {
            //生成测试Service类文件
            genServiceFile(aClass);
            //生成Excel文件
            genExcelFile(aClass);
            //生成test类文件
            genTestFile(aClass);

        });
    }



    private static void genServiceFile(Class<?> aClass){
        List<String> importList = new ArrayList<>();
        String serviceFileName = "Service"+aClass.getSimpleName();
        File serviceFileDir = new File(ServiceTemplate.SERVICE_FILE_PATH);
        if(!serviceFileDir.exists()) {
            serviceFileDir.mkdirs();
        }
        File testOutFile = new File(serviceFileDir,serviceFileName+".java");
        StringBuilder sb = new StringBuilder(ServiceTemplate.service_template);
        String requestValue = "";
        if (aClass.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);
            requestValue = requestMapping.value()[0];
        }
        for (Method method : aClass.getMethods()){
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String httpMethod = requestMapping.method()[0].name();
            Type returnType = method.getGenericReturnType();
            Class[] paramType = method.getParameterTypes();
            Type[] typeArguments = ((ParameterizedType) returnType).getActualTypeArguments();

            System.out.println(method.getName());
            System.out.println(JSON.toJSON(requestMapping.value()));
            System.out.println(JSON.toJSON(requestMapping.method()));

            String methodString = ServiceTemplate.method_template;

            methodString = methodString.replaceAll("\\$methodName",method.getName());
            methodString = methodString.replaceAll("\\$httpMethod",httpMethod);
            //FIXME 兼容一个方法有多个请求参数的情况
            methodString = methodString.replaceAll("\\$requestType",paramType[0].getSimpleName());
            methodString = methodString.replaceAll("\\$returnType",typeArguments[0].getTypeName());
            methodString = methodString.replaceAll("\\$apiPath",requestValue+requestMapping.value()[0]);

            importList.add("import "+paramType[0].getName()+";\n");
            sb.insert(sb.indexOf("//method"),methodString);
        }
        for (String importString : importList){
            sb.insert(sb.indexOf("//import"),importString);
        }
        try {
            String writeStr =  sb.toString();
            writeStr = writeStr.replaceAll("\\$ServiceName",serviceFileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(testOutFile));
            bw.write(writeStr);
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 生成测试入口文件
     * @param clazz
     */
    private static void genTestFile(Class<?> clazz){
        String testFileName = "Test"+clazz.getSimpleName();
        File testFileDir = new File(TestTemplate.TEST_FILE_PATH);
        String serviceFileName = "Service"+clazz.getSimpleName();

        //判断Excel目录是否存在
        if(!testFileDir.exists())
            testFileDir.mkdirs();
        File testFile = new File(testFileDir,testFileName+".java");

        StringBuilder sb = new StringBuilder(TestTemplate.test_template);
        //生成测试类
        for (Method method : clazz.getMethods()){
            String methodString = TestTemplate.method_template;
            methodString = methodString.replaceAll("\\$methodName",method.getName());
            methodString = methodString.replaceAll("\\$serviceName",serviceFileName);
            sb.insert(sb.indexOf("//method"),methodString);
        }
        try {
            String writeStr =  sb.toString();
            writeStr = writeStr.replaceAll("\\$serviceName",serviceFileName);
            writeStr = writeStr.replaceAll("\\$testFileName",testFileName);
            writeStr = writeStr.replaceAll("\\$excelFileName",clazz.getSimpleName());
            BufferedWriter bw = new BufferedWriter(new FileWriter(testFile));
            bw.write(writeStr);
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private static void genExcelFile(Class<?> clazz) {
        String excelFileName = clazz.getSimpleName();

        File excelFileDir = new File(ServiceTemplate.EXCEL_FILE_PATH);
        //判断Excel目录是否存在
        if(!excelFileDir.exists())
            excelFileDir.mkdirs();
        File excelFile = new File(excelFileDir,excelFileName+".xlsx");
        Method[] methods = clazz.getMethods();
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (Method method : methods){
            Class<?>[] parameterTypes = method.getParameterTypes();
            String paramJson = "";
            try {
                 paramJson = new ObjectMapper().writeValueAsString(parameterTypes[0].newInstance());
            }catch (Exception e){
                 paramJson = "{}";
            }
            XSSFSheet sheet = workbook.createSheet(method.getName());
            sheet.setDefaultColumnWidth(15);
            //**预留
            writeCell(sheet,0,0,"caseName",true);
            writeCell(sheet,0,1,"caseDescription",false);
            writeCell(sheet,0,2,"caseType",false);
            writeCell(sheet,0,3,"casePriority",false);
            writeCell(sheet,0,4,"caseRun",false);
            writeCell(sheet,0,5,"paramJson",false);
            writeCell(sheet,0,6,"url",false);

            writeCell(sheet,1,0,"用例名称",true);
            writeCell(sheet,1,1,"用例描述",false);
            writeCell(sheet,1,2,"正常流程",false);
            writeCell(sheet,1,3,"P0",false);
            writeCell(sheet,1,4,"Y",false);
            writeCell(sheet,1,5,paramJson,false);
            writeCell(sheet,1,6,"/",false);
        }

        FileOutputStream fo;
        try {
            fo = new FileOutputStream(excelFile);
            workbook.write(fo);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 写入数据到表格
     * @param sheet
     * @param rowNum
     * @param cellNum
     * @param value
     * @param isCreate
     */
    private static void writeCell(XSSFSheet sheet, int rowNum, int cellNum,String value,boolean... isCreate){
        XSSFRow row = null;
        if(isCreate.length == 0 || isCreate[0]){
            row = sheet.createRow(rowNum);
        }else{
            row = sheet.getRow(rowNum);
        }
        XSSFCell cell = row.createCell(cellNum);
        cell.setCellValue(value);
    }

}
