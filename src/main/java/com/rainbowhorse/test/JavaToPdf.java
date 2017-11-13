package com.rainbowhorse.test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 不支持中文
 * ClassName: JavaToPdf 
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdf {

	// 生成PDF路径
	private static final String DEST = "target/HelloWorld.pdf";

	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		document.add(new Paragraph("hello world"));
		document.close();
		writer.close();
	}
}
