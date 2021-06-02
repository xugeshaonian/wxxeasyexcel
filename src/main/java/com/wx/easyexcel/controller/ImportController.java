package com.wx.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.sun.deploy.net.URLEncoder;
import com.wx.easyexcel.bean.Student;
import com.wx.easyexcel.listener.StudentListener;
import com.wx.easyexcel.mapper.StudentDao;
import com.wx.easyexcel.util.TestFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangxiaoxu
 * @version 1.0
 * @description: TODO
 * @date 2021/6/2 10:50
 */
@RestController
@RequestMapping("/import")
public class ImportController {
    @Autowired
    StudentDao studentDao;

    // 模拟从数据库查询出来的数据
    private List<Student> data() {
        List<Student> list = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            list.add(student);
            student.setName("张三" + i);
            student.setAge(18 + i);
            student.setScore(new BigDecimal(i + ""));
        }
        return list;
    }

    /***
     * @description: 导出
     * @param: httpResponse
     * @return: void
     * @author wangxiaoxu
     * @date: 2021/6/2 10:56
     */
    @GetMapping("/down")
    public void download(HttpServletResponse httpResponse) throws IOException {

        httpResponse.setContentType("application/vnd.ms-excel");
        httpResponse.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        httpResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //   Student.class 是按导出类  data()应为数据库查询数据，这里只是模拟
        EasyExcel.write(httpResponse.getOutputStream(), Student.class).sheet("模板").doWrite(data());

    }

    /*** 
     * @description: 读取excel 
     * @param: multipartFile 
     * @return: java.lang.String
     * @author wangxiaoxu
     * @date: 2021/6/2 13:44
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        EasyExcel.read(multipartFile.getInputStream(), Student.class, new StudentListener(studentDao)).sheet(1).build();
        // 写法2：
        ExcelReader excelReader = EasyExcel.read(multipartFile.getInputStream(), Student.class, new StudentListener(studentDao)).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
        return "success";

    }


    @GetMapping("template")
    public String downloadDataByExcelTemplate(HttpServletResponse response) throws Exception {
        // 获取模板路径
        /**   public static String getPath() { return TestFileUtil.class.getResource("/").getPath();   }**/
        String templateFileName =
                TestFileUtil.getPath() + "excel"  + File.separator + "list.xlsx";
        System.out.println("templateFileName" +templateFileName);
        response.setContentType("application/vnd.ms-excel");
        String fileName = TestFileUtil.getPath() + "listFill" + System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        System.out.println("fileName" + fileName);
        EasyExcel.write(response.getOutputStream()).withTemplate(templateFileName).sheet().doFill(data());
        return "success";

    }

    /***
     * @description: 导出指定列
     * @param: httpResponse
     * @return: void
     * @author wangxiaoxu
     * @date: 2021/6/2 10:56
     */
    @GetMapping("/down/column")
    public void downloadColumn(HttpServletResponse httpResponse) throws IOException {

        httpResponse.setContentType("application/vnd.ms-excel");
        httpResponse.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        httpResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //   Student.class 是按导出类  data()应为数据库查询数据，这里只是模拟
        //排除age 数据
//        Set<String> excludeColumnFiledNames = new HashSet<>();
//        excludeColumnFiledNames.add("age");
        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("age");
        EasyExcel.write(httpResponse.getOutputStream(), Student.class).
                includeColumnFiledNames(includeColumnFiledNames).sheet("模板").doWrite(data());

    }
}
