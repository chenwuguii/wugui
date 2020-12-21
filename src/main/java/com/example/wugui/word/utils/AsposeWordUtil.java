package com.example.wugui.word.utils;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author czy
 * @date 2020/12/21 16:40
 */
public class AsposeWordUtil {

    private static final String WIN = "win";

    /**
     * word转pdf 需引入 aspose-words-16.4.0-jdk16.jar包 收费插件windows linux下均可用
     *
     * @param inPath
     * 源文件路径
     * @param outPath
     * 输出文件路径
     */
    public static void convertPdfToDocx(String inPath, String outPath) {
        try {
            FontSettings fontSettings = new FontSettings();
            File file = new File(outPath);
            FileOutputStream os = new FileOutputStream(file);

            Document doc = new Document(inPath);
            // 另外服务器需要上传中文字体到/usr/share/fonts目录（复制windowsC:WindowsFonts目录下的字体文件即可）
            String cos = System.getProperty("os.name");
            if (cos.toLowerCase().startsWith(WIN)) {
                // windows环境
                fontSettings.setFontsFolder("C:/Windows/Fonts", false);
            } else {
                // Linux环境
                fontSettings.setFontsFolder("/usr/share/fonts", false);
            }
            doc.setFontSettings(fontSettings);
            // 全面支持DOC, DOCX, OOXML, RTF HTML,OpenDocument, PDF,EPUB, XPS, SWF 相互转换
            doc.save(os, SaveFormat.PDF);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
