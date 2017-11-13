package com.rainbowhorse.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rainbowhorse.test.util.PathUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarker模板的HTML转PDF
 * ClassName: JavaToPdfHtmlFreeMarker 
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdfHtmlFreeMarker {

	// 生成PDF路径
	private static final String DEST = "target/HelloWorld_CN_HTML_FREEMARKER.pdf";
	// 模板路径
	private static final String HTML = "template_freemarker.html";
	// 中文字体（黑体）
	private static final String FONT = "simhei.ttf";
	private static Configuration freemarkerCfg = null;

	static {
		freemarkerCfg = new Configuration();
		// freemarker的模板目录
		try {
			freemarkerCfg.setDirectoryForTemplateLoading(new File(PathUtil.getCurrentPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, DocumentException {
		Map<String, Object> data = new HashMap<String, Object>(16);
		data.put("name", "rainbowhorse");
		String content = JavaToPdfHtmlFreeMarker.freeMarkerRender(data, HTML);
		JavaToPdfHtmlFreeMarker.createPdf(content, DEST);
	}

	public static void createPdf(String content, String dest) throws IOException, DocumentException {

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontImp.register(FONT);
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(content.getBytes()), null,
				Charset.forName("UTF-8"), fontImp);
		document.close();

	}

	/**
	 * freemarker渲染html
	 */
	public static String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
		Writer out = new StringWriter();
		try {
			// 获取模板,并设置编码方式
			Template template = freemarkerCfg.getTemplate(htmlTmp);
			template.setEncoding("UTF-8");
			// 合并数据模型与模板
			template.process(data, out);
			// 将合并后的数据和模板写入到流中，这里使用的字符流
			out.flush();
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
}
