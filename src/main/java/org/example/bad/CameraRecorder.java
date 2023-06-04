package org.example.bad;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avdevice;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraRecorder {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int FRAME_RATE = 30;

    public static void main(String[] args) {
        // 设置保存路径
        String savePath = generateSavePath();

        // 初始化FFmpeg
        Loader.load(avdevice.class);

        // 创建FrameGrabber，用于抓取摄像头数据
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("0");
        grabber.setFormat("dshow");
        grabber.setImageWidth(WIDTH);
        grabber.setImageHeight(HEIGHT);
        grabber.setFrameRate(FRAME_RATE);

        // 创建OpenCVFrameRecorder，用于录制视频
        OpenCVFrameRecorder recorder = new OpenCVFrameRecorder(savePath, WIDTH, HEIGHT);
        recorder.setFormat("mp4");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(FRAME_RATE);

        try {
            grabber.start();
            recorder.start();

            long startTime = System.currentTimeMillis();

            // 控制录制时长
            long duration = 10 * 1000; // 10 seconds
            while (System.currentTimeMillis() - startTime < duration) {
                Frame frame = grabber.grab();
                recorder.record(frame);
            }

            recorder.stop();
            grabber.stop();

            System.out.println("Recording completed. Video saved at: " + savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateSavePath() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "record_" + timestamp + ".mp4";
    }
}
