/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: XwpfTest
 * Author:   lida
 * Date:     2018/8/28 16:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/8/28 16:16     V1.0              描述
 */
package com.examcoo.learning.doc;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;

/**
 * 〈使用poi包写word文档〉<br>
 *
 * http://elim.iteye.com/blog/2049110
 * https://blog.csdn.net/j_a_d_e/article/details/53945288
 *
 POI在读写word docx文件时是通过xwpf模块来进行的，其核心是XWPFDocument。
 一个XWPFDocument代表一个docx文档，其可以用来读docx文档，也可以用来写docx文档。
 XWPFDocument中主要包含下面这几种对象：

 l  XWPFParagraph：代表一个段落。

 l  XWPFRun：代表具有相同属性的一段文本。

 l  XWPFTable：代表一个表格。

 l  XWPFTableRow：表格的一行。

 l  XWPFTableCell：表格对应的一个单元格。
 *
 * @author lida
 * @create 2018/8/28
 * @since 1.0.0
 */
public class XwpfTest {

    public static void main(String[] args) {
        XwpfTest xwpfTest = new XwpfTest();
        try {
            xwpfTest.testSimpleWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基本的写操作
     * @throws Exception
     */
    public void testSimpleWrite() throws Exception {
        //新建一个文档
        XWPFDocument doc = new XWPFDocument();
        //创建一个段落
        XWPFParagraph para = doc.createParagraph();

        //一个XWPFRun代表具有相同属性的一个区域。
        XWPFRun run = para.createRun();
        run.setBold(true); //加粗
        run.setText("加粗的内容");
        run.addBreak();
        run = para.createRun();
        run.setColor("FF0000");
        run.setText("红色的字。");
        OutputStream os = new FileOutputStream("E:\\simpleWrite.docx");
        //把doc输出到输出流
        doc.write(os);
        this.close(os);
    }

    /***
     * 写一个表格
     * @throws Exception
     */
    public void testWriteTable() throws Exception {
        XWPFDocument doc = new XWPFDocument();
        //创建一个5行5列的表格
        XWPFTable table = doc.createTable(5, 5);
        //这里增加的列原本初始化创建的那5行在通过getTableCells()方法获取时获取不到，但通过row新增的就可以。
//    table.addNewCol(); //给表格增加一列，变成6列
        table.createRow(); //给表格新增一行，变成6行
        List<XWPFTableRow> rows = table.getRows();
        //表格属性
        CTTblPr tablePr = table.getCTTbl().addNewTblPr();
        //表格宽度
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(8000));
        XWPFTableRow row;
        List<XWPFTableCell> cells;
        XWPFTableCell cell;
        int rowSize = rows.size();
        int cellSize;
        for (int i=0; i<rowSize; i++) {
            row = rows.get(i);
            //新增单元格
            row.addNewTableCell();
            //设置行的高度
            row.setHeight(500);
            //行属性
//       CTTrPr rowPr = row.getCtRow().addNewTrPr();
            //这种方式是可以获取到新增的cell的。
//       List<CTTc> list = row.getCtRow().getTcList();
            cells = row.getTableCells();
            cellSize = cells.size();
            for (int j=0; j<cellSize; j++) {
                cell = cells.get(j);
                if ((i+j)%2==0) {
                    //设置单元格的颜色
                    cell.setColor("ff0000"); //红色
                } else {
                    cell.setColor("0000ff"); //蓝色
                }
                //单元格属性
                CTTcPr cellPr = cell.getCTTc().addNewTcPr();
                cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                if (j == 3) {
                    //设置宽度
                    cellPr.addNewTcW().setW(BigInteger.valueOf(3000));
                }
                cell.setText(i + ", " + j);
            }
        }
        //文件不存在时会自动创建
        OutputStream os = new FileOutputStream("D:\\table.docx");
        //写入文件
        doc.write(os);
        this.close(os);
    }

    /**
     * 关闭输出流
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}