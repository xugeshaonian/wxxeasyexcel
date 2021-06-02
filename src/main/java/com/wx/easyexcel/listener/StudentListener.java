package com.wx.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.wx.easyexcel.bean.Student;
import com.wx.easyexcel.mapper.StudentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wangxiaoxu
 * @version 1.0
 * @description: TODO
 * @date 2021/6/2 11:20
 */
public class StudentListener extends AnalysisEventListener<Student> {

    private static final Logger logger = LoggerFactory.getLogger(StudentListener.class);
   /**
   *每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
   */
   private static  final int BATCH_COUNT = 5;

    List<Student> list = new ArrayList<Student>();
    private StudentDao studentDao;

    public StudentListener(StudentDao studentDao) {
//         studentDao = new StudentDao();
        this.studentDao = studentDao;
    }

    /*** 
     * @description:  这个每一条数据解析都会来调用
     * @param: student
analysisContext
     * @return: void 
     * @author wangxiaoxu
     * @date: 2021/6/2 11:29
     */
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        logger.info("解析到一条数据：{}", JSON.toJSON(student));
        System.out.println("invoke方法被调用");
        list.add(student);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() > BATCH_COUNT){
            saveData();
            list.clear();
        }
    }

    /*** 
     * @description:  保存数据库
     * @param:  
     * @return: void 
     * @author wangxiaoxu
     * @date: 2021/6/2 13:31
     */
    private void saveData() {
        logger.info("{} 条数据，开始存储数据库",list.size());
        //存储数据库
        studentDao.save(list);
        logger.info("存储数据库成功");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("doAfterAllAnalysed方法 被调用");
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        logger.info("所有数据解析完成！");

    }
}
