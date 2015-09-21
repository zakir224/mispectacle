package com.aru.mispectacle;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Md Zakir Hossen on 6/25/2015.
 */
public class CannyDetector {

    Mat src_gray;
    Mat dst, detected_edges;
    private Bitmap m;

    int edgeThresh = 1;
    private int lowThreshold=15;
    final int max_lowThreshold = 100;
    int ratio = 2;
    int kernel_size = 3;
    private Mat src;

    public CannyDetector(){
        src = new Mat();
        dst = new Mat();
        src_gray = new Mat();
        detected_edges = new Mat();
    }

    public Mat detect()
    {

        Utils.bitmapToMat(m, src);

        /// Create a matrix of the same type and size as src (for dst)
        dst.create(src.size(), src.type() );
        //CV_BGR2GRAY
        /// Convert the image to grayscale
        Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGRA2GRAY);
        /// Create a window
        //namedWindow( window_name, CV_WINDOW_AUTOSIZE );
        /// Create a Trackbar for user to enter threshold
        /// Reduce noise with a kernel 3x3
        Imgproc.blur(src_gray, detected_edges, new Size(3, 3));
        /// Canny detector
        Imgproc.Canny(detected_edges, detected_edges, lowThreshold, lowThreshold * ratio);

        /// Using Canny's output as a mask, weU display our result
        dst = Mat.zeros(src.rows(),src.cols(), CvType.CV_8UC3);

        src.copyTo(dst, detected_edges);
        //imshow( window_name, dst );

        /// Wait until user exit program by pressing a key


        return dst;
    }

    public int getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public Bitmap getM() {
        return m;
    }

    public void setM(Bitmap m) {
        this.m = m;
    }
}
