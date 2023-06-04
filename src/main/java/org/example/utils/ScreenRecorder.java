package org.example.utils;

import javax.imageio.ImageIO;
import javax.media.MediaLocator;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ScreenRecorder {

    private static final int FRAME_RATE = 10;
    private static final String VIDEO_FORMAT = "mp4";

    public static void recordScreen(int durationSeconds, String savePath) {
        try {
            // 创建Robot对象
            Robot robot = new Robot();

            // 获取屏幕尺寸
            Rectangle screenRect = getScreenBounds();

            // 创建临时帧文件夹
            File framesDir = new File("temp_frames");
            framesDir.mkdirs();

            // 录制开始时间
            long startTime = System.currentTimeMillis();

            // 录制帧数
            int frameCount = 0;

            Vector<String> inFiles = new Vector();

            long frameGap = 1000 / FRAME_RATE;

            // 录制屏幕
            while ((System.currentTimeMillis() - startTime) < durationSeconds * 1000) {

                long frameStart = System.currentTimeMillis();

                // 截取屏幕图像
                BufferedImage image = robot.createScreenCapture(screenRect);

                // 保存图像帧
                File frameFile = new File(framesDir, String.format("%05d.jpeg", frameCount));
                ImageIO.write(image, "jpeg", frameFile);
                inFiles.add(frameFile.getAbsolutePath());

                long now = System.currentTimeMillis();
                // 等待一帧时间
                if (frameGap>(now-startTime)){
                    Thread.sleep(frameGap - (now-frameStart));
                }

                // 更新帧数
                frameCount++;
            }

            // Generate the output media locators.

            JpegImagesToMovie imageToMovie = new JpegImagesToMovie();

            MediaLocator oml;

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String outputFilename = savePath + File.separator + "screen_" + timestamp + "." + VIDEO_FORMAT;


            if ((oml = imageToMovie.createMediaLocator(outputFilename)) == null) {
                System.err.println("Cannot build media locator from: " + outputFilename);
                System.exit(0);
            }

            imageToMovie.doIt(screenRect.width, screenRect.height, FRAME_RATE, inFiles, oml);

            // 删除临时帧文件夹
            for (File frame : framesDir.listFiles()) {
                frame.delete();
            }
            framesDir.delete();

        } catch (AWTException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Rectangle getScreenBounds() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        Rectangle bounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            bounds = bounds.union(screen.getDefaultConfiguration().getBounds());
        }

        return bounds;
    }

    public static void main(String[] args) {
        int durationSeconds = 10; // 录制时长（秒）
        String savePath = "save"; // 保存路径

        recordScreen(durationSeconds, savePath);
    }
}
