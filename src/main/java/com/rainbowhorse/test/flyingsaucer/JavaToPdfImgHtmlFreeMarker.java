package com.rainbowhorse.test.flyingsaucer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.rainbowhorse.test.util.PathUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Jpedal把pdf转成图片 
 * ClassName: JavaToPdfImgHtmlFreeMarker
 * @Description: TODO
 * @author maxu
 * @date 2017年11月13日
 */
public class JavaToPdfImgHtmlFreeMarker {

	private static final String DEST = "target/HelloWorld_CN_HTML_FREEMARKER_FS_IMG.png";
	private static final String HTML = "template_freemarker_fs.html";
	private static final String FONT = "simhei.ttf";
	private static final String LOGO_PATH = "file://" + PathUtil.getCurrentPath() + "/logo.png";
	private static final String IMG_EXT = "png";

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

		String content = JavaToPdfImgHtmlFreeMarker.freeMarkerRender(data, HTML);
		ByteArrayOutputStream pdfStream = JavaToPdfImgHtmlFreeMarker.createPdf(content);
		ByteArrayOutputStream imgSteam = JavaToPdfImgHtmlFreeMarker.pdfToImg(pdfStream.toByteArray(), 2, 1, IMG_EXT);

		FileOutputStream fileStream = new FileOutputStream(new File(DEST));
		fileStream.write(imgSteam.toByteArray());
		fileStream.close();

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

	/**
	 * 根据模板生成pdf文件流
	 */
	public static ByteArrayOutputStream createPdf(String content) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ITextRenderer render = new ITextRenderer();
		ITextFontResolver fontResolver = render.getFontResolver();
		try {
			fontResolver.addFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 解析html生成pdf
		render.setDocumentFromString(content);
		// 解决图片相对路径的问题
		render.getSharedContext().setBaseURL(LOGO_PATH);
		render.layout();
		try {
			render.createPDF(outStream);
			return outStream;
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据pdf二进制文件 生成图片文件
	 *
	 * @param bytes
	 *            pdf二进制
	 * @param scaling
	 *            清晰度
	 * @param pageNum
	 *            页数
	 */
	public static ByteArrayOutputStream pdfToImg(byte[] bytes, float scaling, int pageNum, String formatName) {
		// 推荐的方法打开PdfDecoder
		PdfDecoder pdfDecoder = new PdfDecoder(true);
		FontMappings.setFontReplacements();
		// 修改图片的清晰度
		pdfDecoder.scaling = scaling;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			// 打开pdf文件，生成PdfDecoder对象
			pdfDecoder.openPdfArray(bytes); // bytes is byte[] array with PDF
			// 获取第pageNum页的pdf
			BufferedImage img = pdfDecoder.getPageAsImage(pageNum);

			ImageIO.write(img, formatName, out);
		} catch (PdfException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
}
