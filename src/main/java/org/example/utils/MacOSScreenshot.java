package org.example.utils;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class MacOSScreenshot {

    public static void main(String[] args) {
        try {
            // 调用截屏函数，传入保存路径和文件名前缀
            captureScreenWithTimestamp("screenshots/", "screenshot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 截屏并保存文件，文件名包含时间戳
    public static void captureScreenWithTimestamp(String savePath, String fileNamePrefix) throws Exception {
        // 获取当前时间戳
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // 创建保存目录
        File directory = new File(savePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 获取屏幕尺寸
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        // 截屏
        BufferedImage screenshot = new Robot().createScreenCapture(screenRect);

        // 生成文件名
        String fileName = fileNamePrefix + "_" + timestamp + ".png";

        // 保存截图到文件
        File outputFile = new File(savePath + fileName);
        ImageIO.write(screenshot, "png", outputFile);

        System.out.println("截屏成功，保存到：" + outputFile.getAbsolutePath());
    }
}
