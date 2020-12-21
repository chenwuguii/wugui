package com.example.wugui.word;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSONObject;
import com.example.wugui.excel.handle.CustomSheetWriteHandler;
import com.example.wugui.excel.listener.ExcelListener;
import com.example.wugui.excel.model.User;
import com.example.wugui.excel.model.WorkHistory;
import com.example.wugui.word.model.EpPmenber;
import com.example.wugui.word.model.EpRewandpun;
import com.example.wugui.word.utils.AsposeWordUtil;
import com.example.wugui.word.utils.WordUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
class WordTest {

    /**
     * word文档渲染
     */
    @Test
    void test1() throws Exception {
        String template = "C:\\Users\\likun\\Desktop\\模板.docx";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("${uname}", "乌龟");
        paramMap.put("${usex}", "男");
        paramMap.put("${ubirthdate}", "1998年10月22日");
        paramMap.put("${unation}", "汉族");
        paramMap.put("${unative}", "广东深圳");
        paramMap.put("${uplace}", "广东汕头");
        paramMap.put("${upolity}", "团员");
        paramMap.put("${uworkdate}", "2020年3月16日");
        paramMap.put("${uhealth}", "良好");
        paramMap.put("${umajorpost}", "软件开发");
        paramMap.put("${umajor}", "Java开发");

        // 照片路径以及大小
        Map<String, Object> phomap = new HashMap<>(8);
        phomap.put("width", 100);
        phomap.put("height", 130);
        phomap.put("type", "png");
        phomap.put("content", "C:\\Users\\likun\\Pictures\\头像\\1.jpg");
        paramMap.put("${upho}", phomap);

        // 查询员工家庭信息
        List<EpPmenber> menberlist = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            EpPmenber pmenber = new EpPmenber();
            pmenber.setUname("小王");
            pmenber.setUconnection("父亲");
            pmenber.setUbirthday("1962年10月2日");
            pmenber.setUploity("群众");
            pmenber.setUworkunit("广东xxx公司");
            pmenber.setUstatus("无");
            menberlist.add(pmenber);
        }
        paramMap.put("menberlist", menberlist);

        // 查询员工奖励情况
        List<EpRewandpun> andpunlist = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            EpRewandpun rewandpun = new EpRewandpun();
            rewandpun.setUrewdate("2020年5月1日");
            rewandpun.setUrewunit("深圳xxx有限公司");
            rewandpun.setUrewdesc("无");
            andpunlist.add(rewandpun);
        }
        paramMap.put("andpunlist", andpunlist);

        // 模板填充
        XWPFDocument doc = WordUtil.generateWord(paramMap, template);
        FileOutputStream fopts = new FileOutputStream("C:\\Users\\likun\\Desktop\\填充后的模板.docx");
        doc.write(fopts);
        fopts.close();
    }

    /**
     * word 转 pdf
     */
    @Test
    void test2(){
        String wordPath = "C:Users\\likun\\Desktop\\模板.docx";
        String pdfPath = "C:Users\\likun\\Desktop\\模板.pdf";
        AsposeWordUtil.convertPdfToDocx(wordPath, pdfPath);
    }
}
