package org.example.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class IDCardOCR {
    public static void main(String[] args) {
        String imagePath = "/Users/bytedance/Downloads/sfz.jpeg";
        String result = extractIDCardInfo(imagePath);
        System.out.println(result);
    }

    public static String extractIDCardInfo(String imagePath) {
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("chi_sim"); // 设置识别语言为中文简体

        try {
            String result = tesseract.doOCR(new File(imagePath));
            return parseIDCardInfo(result);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseIDCardInfo(String ocrResult) {
        String[] lines = ocrResult.split("\\n");
        String name = null;
        String gender = null;
        String birthday = null;
        String idNumber = null;

        // 根据OCR识别结果提取身份证信息
        for (String line : lines) {
            if (line.contains("姓名")) {
                name = line.substring(line.indexOf("姓名") + 2);
            } else if (line.contains("性别")) {
                gender = line.substring(line.indexOf("性别") + 2);
            } else if (line.contains("出生")) {
                birthday = line.substring(line.indexOf("出生") + 2);
            } else if (line.contains("号码")) {
                idNumber = line.substring(line.indexOf("号码") + 2);
            }
        }

        // 构建提取的身份证信息字符串
        StringBuilder sb = new StringBuilder();
        sb.append("姓名：").append(name).append("\n");
        sb.append("性别：").append(gender).append("\n");
        sb.append("生日：").append(birthday).append("\n");
        sb.append("身份证号码：").append(idNumber);
        
        return sb.toString();
    }
}
