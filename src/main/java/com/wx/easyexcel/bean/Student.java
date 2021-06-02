package com.wx.easyexcel.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wangxiaoxuss
 * @version 1.0
 * @description: TODO
 * @date 2021/6/2 10:48
 */
@Data
public class Student {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("成绩")
    private BigDecimal score;
}
