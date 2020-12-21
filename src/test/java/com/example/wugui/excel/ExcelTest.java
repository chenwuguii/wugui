package com.example.wugui.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSONObject;

import com.example.wugui.handle.CustomSheetWriteHandler;
import com.example.wugui.listener.ExcelListener;

import com.example.wugui.model.User;
import com.example.wugui.model.WorkHistory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * @author czy
 * @date 2020/12/21 14:27
 */
@SpringBootTest
class ExcelTest {
    /*
      不创建对象的写
     */
    @Test
    public void test1() {
        // 生成Excel路径
        String excelPath = "C:\\Users\\likun\\Desktop\\测试.xlsx";
        EasyExcel.write(excelPath).head(head()).sheet("模板").doWrite(dataList());
    }

    /*
      创建对象的写
     */
    @Test
    public void test2() {
        // 生成Excel路径
        String excelPath = "C:\\Users\\likun\\Desktop\\测试.xlsx";
        EasyExcel.write(excelPath, User.class).sheet("模板").doWrite(data());
    }

    /*
      自定义拦截器
     */
    @Test
    public void test3() {
        // 生成Excel路径
        String excelPath = "C:\\Users\\likun\\Desktop\\测试.xlsx";
        EasyExcel.write(excelPath, User.class).sheet("模板").registerWriteHandler(new CustomSheetWriteHandler()).doWrite(data());
    }

    /*
      Excel模板填充(模板已放到doc文件夹下)
     */
    @Test
    public void test4() throws IOException {
        // 生成Excel路径
        String excelPath = "C:\\Users\\likun\\Desktop\\填充后的模板.xlsx";
        String templatePath = "C:\\Users\\likun\\Desktop\\模板.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(excelPath).withTemplate(templatePath).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        // 填充数据
        Map<String, Object> map = new HashMap<>(64);
        map.put("uname", "张三");
        map.put("usex", "男");
        map.put("ubirthday", "2020.10.01");
        map.put("ucardid", "440582xxxxxxxx");
        map.put("umarriage", "未婚");
        map.put("unation", "汉族");
        map.put("unative", "广东xxxx");
        map.put("ubirthplace", "广东xxxx");
        map.put("upolity", "团员");
        map.put("uworktime", "2020.05.15");
        map.put("uhealth", "良好");
        excelWriter.fill(map, writeSheet);
        excelWriter.fill(new FillWrapper("data1", data1()), fillConfig, writeSheet);
        // 别忘记关闭流
        excelWriter.finish();
        // 合并单元格
        mergeExcel(excelPath);

    }

    /*
      Excel读取
    */
    @Test
    public void test5() {
        // 生成Excel路径
        String fileName = "C:\\Users\\likun\\Desktop\\测试.xlsx";
        ExcelListener excelListener = new ExcelListener();
        EasyExcel.read(fileName, excelListener).sheet().doRead();
        // 表格头数据
        Map<String, Integer> importHeads = excelListener.getImportHeads();
        System.out.println(importHeads);
        // 每一行数据
        List<JSONObject> dataList = excelListener.getDataList();
        for (JSONObject object : dataList) {
            System.out.println(object);
        }
    }

    private void mergeExcel(String excelPath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(excelPath));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 合并列
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 5, 9));
        sheet.addMergedRegion(new CellRangeAddress(8, 8, 10, 11));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 5, 9));
        sheet.addMergedRegion(new CellRangeAddress(9, 9, 10, 11));
        // 合并行
        sheet.addMergedRegion(new CellRangeAddress(6, 9, 0, 0));

        // 设置边框（其它可自行设置）
        RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(8, 8, 1, 2), sheet);

        // 头像插入
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        BufferedImage bufferImg = ImageIO.read(new File("C:\\Users\\likun\\Pictures\\头像\\1.jpg"));
        ImageIO.write(bufferImg, "jpg", byteArrayOut);

        XSSFDrawing patriarch = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 11, 2, (short) 12, 6);
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
        // 生成excel
        String mergeExcelPath = "C:\\Users\\likun\\Desktop\\合并单元格后的excel.xlsx";
        FileOutputStream outputStream = new FileOutputStream(mergeExcelPath);
        workbook.write(outputStream);
        outputStream.flush();
    }

    private List<WorkHistory> data1() {
        List<WorkHistory> list = new ArrayList<>();
        WorkHistory workHistory;
        for (int i = 1; i <= 3; i++) {
            workHistory = new WorkHistory();
            workHistory.setUbegintime("2020.05.01");
            workHistory.setUendtime("2020.05.01");
            workHistory.setUworkcomp("xxx公司");
            workHistory.setUworkdesc("后勤");
            list.add(workHistory);
        }
        return list;
    }

    private List<User> data() {
        List<User> userList = new ArrayList<>();
        User user;
        for (int i = 1; i <= 10; i++) {
            user = new User();
            user.setName("张三" + i);
            user.setSex("男");
            user.setAge(i);
            user.setCardid("440582xxxx");
            userList.add(user);
        }
        return userList;
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("姓名");
        List<String> head1 = new ArrayList<>();
        head1.add("年龄");
        List<String> head2 = new ArrayList<>();
        head2.add("生日");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<>();
            data.add("张三");
            data.add(25);
            data.add(new Date());
            list.add(data);
        }
        return list;
    }
}
