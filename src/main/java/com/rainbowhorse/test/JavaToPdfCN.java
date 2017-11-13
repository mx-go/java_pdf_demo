package com.rainbowhorse.test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 支持中文
 * ClassName: JavaToPdfCN 
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdfCN {

	// 生成PDF路径
	private static final String DEST = "target/HelloWorld_CN.pdf";
	// 中文字体（黑体）
	private static final String FONT = "simhei.ttf";

	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		document.add(new Paragraph("hello world，我是rainbowhorse。", font));
		document.close();
		writer.close();
	}
}
