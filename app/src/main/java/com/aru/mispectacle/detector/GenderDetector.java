package com.aru.mispectacle.detector;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.aru.mispectacle.exception.MiSpectacleLog;

import org.bytedeco.javacpp.opencv_contrib;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_highgui;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Md Zakir Hossen on 6/17/2015.
 */

public class GenderDetector {

    private AssetManager assetManager;
    private opencv_core.Mat m;
    private Context context;
    private List<String> fileList;
    private opencv_core.MatVector trainingImageMatVector;
    private opencv_core.Mat labels;
    private IntBuffer labelsBuf;
    private String[] files;
    private opencv_contrib.FaceRecognizer faceRecognizer;
    private int counter = 0;
    private int label;
    private OpenCVFrameConverter.ToMat converterToMat;
    private static final int TRAINING_DATA_SIZE = 40;

    public GenderDetector(Context context) {
        this.context = context;
        assetManager = context.getAssets();
        faceRecognizer = org.bytedeco.javacpp.opencv_contrib.createFisherFaceRecognizer();
        converterToMat = new OpenCVFrameConverter.ToMat();
        copyFemaleAssets();
        copyMaleAssets();
    }

    public boolean train() {

        try {
            files = context.getFilesDir().list();
            fileList = Arrays.asList(files);
            Log.e("zakir", "array size: " + fileList.size());
        } catch (Exception e) {
            MiSpectacleLog.e(e.toString());
        }

        trainingImageMatVector = new opencv_core.MatVector(TRAINING_DATA_SIZE);

        labels = new opencv_core.Mat(TRAINING_DATA_SIZE, 1, CvType.CV_32SC1);
        labelsBuf = labels.getIntBuffer();


        for (String image : fileList) {

                opencv_core.Mat img = opencv_highgui.imread(context.getFilesDir() + "/" + image, opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE);


                if (!img.empty() &&  (image.contains("png") || image.contains("jpg")) && !image.contains("predict")) {
                    if (image.contains("female"))
                        label = 0;
                    else
                        label = 1;

                    trainingImageMatVector.put(counter, img);
                    labelsBuf.put(counter, label);

                    counter++;
                    if(counter==TRAINING_DATA_SIZE)
                        break;
                    MiSpectacleLog.e("Label "+ label);
                } else
                    MiSpectacleLog.e("Empty image");

            MiSpectacleLog.e("Image name: " + image);
        }

        for(int i=0; i < trainingImageMatVector.size(); i++)
            MiSpectacleLog.e("Image "+i+": "+ trainingImageMatVector.get(i).toString());



        try {
            faceRecognizer.train(trainingImageMatVector, labels);
            return true;
        } catch (Exception e) {
            MiSpectacleLog.e("Error: " + e.getMessage());
            return false;
        }

    }

    public int predict() {
        opencv_core.Mat m = opencv_highgui.imread(context.getExternalFilesDir(null) + "/predict.png",opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE);
        int predictedLabel = faceRecognizer.predict(m);
        if(predictedLabel==0) {
            Toast.makeText(context, "Face of Female", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Face of Male", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    private void copyMaleAssets() {
        String[] files = null;
        try {
            files = assetManager.list("male");
        } catch (IOException e) {
            MiSpectacleLog.e("Failed to get Male asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("male/" + filename);
                File outFile = new File(context.getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                MiSpectacleLog.e("Failed to copy Male training file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        MiSpectacleLog.e("Failed to close input file: " + filename, e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        MiSpectacleLog.e("Failed to close output file: " + filename, e);
                    }
                }
            }
        }
    }

    private void copyFemaleAssets() {

        String[] files = null;
        try {
            files = assetManager.list("female");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }

        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("female/" + filename);
                File outFile = new File(context.getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                MiSpectacleLog.e("Failed to copy Female asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        MiSpectacleLog.e("Failed to close input file: " + filename, e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        MiSpectacleLog.e("Failed to close output file: " + filename, e);
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
