package com.example.wugui.word.utils;

import com.example.wugui.word.model.EpPmenber;
import com.example.wugui.word.model.EpRewandpun;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author czy
 * @date 2020/12/21 16:09
 */
public class WordUtil {

    /**
     * 根据指定的参数值、模板，生成 word 文档
     * 注意：其它模板需要根据情况进行调整
     *
     * @param param    变量集合
     * @param template 模板路径
     */
    public static XWPFDocument generateWord(Map<String, Object> param, String template) {
        XWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(template);
            doc = new XWPFDocument(pack);
            if (param != null && param.size() > 0) {
                // 处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc);
                // 处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                //表格索引
                int i = 0;
                List<EpPmenber> menberlist = (List<EpPmenber>) param.get("menberlist");
                List<EpRewandpun> andpunlist = (List<EpRewandpun>) param.get("andpunlist");

                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    int size = table.getRows().size() - 1;
                    XWPFTableRow row2 = table.getRow(size);
                    if (i == 1) {//家庭成员
                        if (menberlist.size() > 0) {
                            for (int j = 0; j < menberlist.size() - 1; j++) {
                                copy(table, row2, size + j);
                            }
                        }
                    } else if (i == 2) {//奖惩情况
                        if (andpunlist.size() > 0) {
                            for (int j = 0; j < andpunlist.size() - 1; j++) {
                                copy(table, row2, size + j);
                            }
                        }
                    }

                    List<XWPFTableRow> rows = table.getRows();
                    int _row = 0;
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc);
                        }

                        // 家庭成员
                        if (i == 1 && _row >= size) {
                            if (menberlist.size() == 0) {
                                _row++;
                                continue;
                            }
                            row.getCell(0).setText(menberlist.get(_row - size).getUconnection());
                            row.getCell(1).setText(menberlist.get(_row - size).getUname());
                            row.getCell(2).setText(menberlist.get(_row - size).getUbirthday());
                            row.getCell(3).setText(menberlist.get(_row - size).getUploity());
                            row.getCell(4).setText(menberlist.get(_row - size).getUworkunit());
                            row.getCell(5).setText(menberlist.get(_row - size).getUstatus());
                        } else if (i == 2 && _row >= size) {//奖励情况
                            if (andpunlist.size() == 0) {
                                _row++;
                                continue;
                            }
                            row.getCell(0).setText(andpunlist.get(_row - size).getUrewdate());
                            row.getCell(1).setText(andpunlist.get(_row - size).getUrewunit());
                            row.getCell(2).setText(andpunlist.get(_row - size).getUrewdesc());
                        }
                        _row++;
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 拷贝赋值行
     */
    public static void copy(XWPFTable table, XWPFTableRow sourceRow, int rowIndex) {
        // 在表格指定位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(rowIndex);
        // 复制行属性
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return;
        }
        // 复制列及其属性和内容
        XWPFTableCell targetCell;
        for (XWPFTableCell sourceCell : cellList) {
            targetCell = targetRow.addNewTableCell();
            // 列属性
            targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            // 段落属性
            if (sourceCell.getParagraphs() != null && sourceCell.getParagraphs().size() > 0) {
                targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
                if (sourceCell.getParagraphs().get(0).getRuns() != null && sourceCell.getParagraphs().get(0).getRuns().size() > 0) {
                    XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                    cellR.setText(sourceCell.getText());
                    cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
                } else {
                    targetCell.setText(sourceCell.getText());
                }
            } else {
                targetCell.setText(sourceCell.getText());
            }
        }
    }

    /**
     * 处理段落
     */
    @SuppressWarnings({"unused", "rawtypes"})
    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param, XWPFDocument doc) throws Exception {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        boolean isSetText = false;
                        for (Map.Entry<String, Object> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if (text.contains(key)) {
                                isSetText = true;
                                Object value;
                                if (entry.getValue() != null) {
                                    value = entry.getValue();
                                } else {
                                    value = "";
                                }
                                // 文本替换
                                if (value instanceof String) {
                                    // 处理答案中的回车换行
                                    if (((String) value).contains("n")) {
                                        String[] lines = ((String) value).split("n");
                                        if (lines.length > 0) {
                                            text = text.replace(key, lines[0]);
                                            for (int j = 1; j < lines.length; j++) {
                                                run.addCarriageReturn();
                                                run.setText(lines[j]);
                                            }
                                        }
                                    } else {
                                        text = text.replace(key, value.toString());
                                    }
                                } else if (value instanceof Map) {
                                    // 图片替换
                                    text = text.replace(key, "");
                                    Map pic = (Map) value;
                                    int width = Integer.parseInt(pic.get("width").toString());
                                    int height = Integer.parseInt(pic.get("height").toString());
                                    int picType = getPictureType(pic.get("type").toString());
                                    String byteArray = (String) pic.get("content");
                                    CTInline inline = run.getCTR().addNewDrawing().addNewInline();
                                    //插入图片
                                    insertPicture(doc, byteArray, inline, width, height, paragraph);
                                }
                            }
                        }
                        if (isSetText) {
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 插入图片
     */
    private static void insertPicture(XWPFDocument document, String filePath, CTInline inline, int width, int height, XWPFParagraph paragraph) throws Exception {
        // 读取图片路径
        InputStream inputStream = new FileInputStream(new File(filePath));

        document.addPictureData(inputStream, XWPFDocument.PICTURE_TYPE_PNG);
        int id = document.getAllPictures().size() - 1;
        final int emu = 9525;
        width *= emu;
        height *= emu;
        String blipId = paragraph.getDocument().getRelationId(document.getAllPictures().get(id));

        String picXml = getPicXml(blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("IMG_" + id);
        docPr.setDescr("IMG_" + id);
    }


    private static String getPicXml(String blipId, int width, int height) {
        return "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 + "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" + "         </pic:nvPicPr>" + "         <pic:blipFill>" + "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" + "            <a:stretch>" + "               <a:fillRect/>" + "            </a:stretch>" + "         </pic:blipFill>" + "         <pic:spPr>" + "            <a:xfrm>"
                + "               <a:off x=\"0\" y=\"0\"/>" + "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" + "            </a:xfrm>" + "            <a:prstGeom prst=\"rect\">" + "               <a:avLst/>" + "            </a:prstGeom>" + "         </pic:spPr>" + "      </pic:pic>" + "   </a:graphicData>" + "</a:graphic>";
    }


    /**
     * 根据图片类型，取得对应的图片类型代码
     */
    private static int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if ("png".equalsIgnoreCase(picType)) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if ("dib".equalsIgnoreCase(picType)) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if ("emf".equalsIgnoreCase(picType)) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if ("jpg".equalsIgnoreCase(picType) || "jpeg".equalsIgnoreCase(picType)) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if ("wmf".equalsIgnoreCase(picType)) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

}
