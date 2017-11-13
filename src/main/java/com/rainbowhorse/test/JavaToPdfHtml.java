package com.rainbowhorse.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rainbowhorse.test.util.PathUtil;

/**
 * HTML转PDF
 * ClassName: JavaToPdfHtml 
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdfHtml {

	// 生成PDF路径
	private static final String DEST = "target/HelloWorld_CN_HTML.pdf";
	// 模板路径
	private static final String HTML = PathUtil.getCurrentPath() + "/template.html";
	// 中文字体（黑体）
	private static final String FONT = "simhei.ttf";

	public static void main(String[] args) throws IOException, DocumentException {

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
		document.open();
		XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontImp.register(FONT);
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML), null,
				Charset.forName("UTF-8"), fontImp);
		document.close();
	}
}
