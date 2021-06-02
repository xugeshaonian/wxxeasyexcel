package com.wx.easyexcel.mapper;

import com.wx.easyexcel.bean.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangxiaoxu
 * @version 1.0
 * @description: TODO
 * @date 2021/6/2 13:43
 */
@Repository
public class StudentDao {
    public void save(List<Student> students) {
        System.out.println("开始存储数据");
        students.forEach(s -> System.out.println("存储数据：" + s));
    }
}
