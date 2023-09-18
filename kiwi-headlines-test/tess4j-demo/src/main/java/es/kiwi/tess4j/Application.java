package es.kiwi.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {

    /**
     * 识别图片中的文字
     * @param args
     */
    public static void main(String[] args) throws TesseractException {

        // 创建实例
        ITesseract tesseract = new Tesseract();
        // 设置字体库路径
        tesseract.setDatapath("D:\\ideawork\\tessdata");
        // 设置语言 --> 字体库文件名
        tesseract.setLanguage("chi_sim");
        // 识别图片
        File file = new File("D:\\jpg\\143.png");
        String result = tesseract.doOCR(file);
        System.out.println("识别的结果为：" + result.replaceAll("\\r|\\n", "-"));
    }
}
