package com.example.wugui.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author czy
 * @date 2020/12/21 15:13
 */
@Data
public class User {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("身份证")
    private String cardid;

    @ExcelProperty({"普通高等学校全日制教育", "学历"})
    private String kultur;

    @ExcelProperty({"普通高等学校全日制教育", "学位"})
    private String degree;

    @ExcelProperty({"普通高等学校全日制教育", "专业"})
    private String major;

    @ExcelProperty({"普通高等学校全日制教育", "获得学历时间"})
    private String graduatetime;

    @ExcelProperty({"普通高等学校全日制教育", "毕业院校"})
    private String school;

}
