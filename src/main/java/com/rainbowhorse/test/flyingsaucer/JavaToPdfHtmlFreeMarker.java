package com.rainbowhorse.test.flyingsaucer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.rainbowhorse.test.util.PathUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarker模板的HTML转PDF Flying Saucer
 * ClassName: JavaToPdfHtmlFreeMarker 
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdfHtmlFreeMarker {

	// 生成PDF路径
	private static final String DEST = "target/HelloWorld_CN_HTML_FREEMARKER_FS.pdf";
	// 模板路径
	private static final String HTML = "template_freemarker_fs.html";
	// 中文字体（黑体）
	private static final String FONT = "simhei.ttf";
	// 图片路径
	private static final String LOGO_PATH = "file:/" + PathUtil.getCurrentPath() + "/";

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

	public static void main(String[] args) throws IOException, DocumentException, com.lowagie.text.DocumentException {
		Map<String, Object> data = new HashMap<String, Object>(16);
		data.put("name", "rainbowhorse");
		String content = JavaToPdfHtmlFreeMarker.freeMarkerRender(data, HTML);
		JavaToPdfHtmlFreeMarker.createPdf(content, DEST);
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
			template.process(data, out); // 将合并后的数据和模板写入到流中，这里使用的字符流
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

	public static void createPdf(String content, String dest)
			throws IOException, DocumentException, com.lowagie.text.DocumentException {
		ITextRenderer render = new ITextRenderer();
		ITextFontResolver fontResolver = render.getFontResolver();
		fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		// 解析html生成pdf
		render.setDocumentFromString(content);
		// 解决图片相对路径的问题
		render.getSharedContext().setBaseURL(LOGO_PATH);
		render.layout();
		render.createPDF(new FileOutputStream(dest));
	}
}
