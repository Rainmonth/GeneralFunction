package com.rainmonth.function.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.rainmonth.function.R;
import com.rainmonth.utils.FileUtils;
import com.rainmonth.utils.PermissionUtils;
import com.rainmonth.utils.ToastUtils;
import com.rainmonth.utils.log.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class VideoCameraDemoActivity extends AppCompatActivity implements SurfaceHolder.Callback, NV21EncoderH264.EncoderListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, VideoCameraDemoActivity.class);
        context.startActivity(intent);
    }

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_camera_demo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createFile();
        surfaceView = findViewById(R.id.surface_view);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    private OutputStream outputStream;

    private void createFile() {
        File file = new File(getExternalCacheDir(), "test.h264");

        try {
            outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            LogUtils.printStackTrace("Camera", e);
        }
    }

    public void openCamera() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size previewSize = getCameraPreviewSize(parameters);
        if (previewSize == null) {
            LogUtils.w("Camera", "previewSize is null");
            return;
        }
        int width = previewSize.width;
        int height = previewSize.height;


        final NV21EncoderH264 nv21EncoderH264 = new NV21EncoderH264(width, height);
        nv21EncoderH264.setEncoderListener(this);

        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(width, height);
        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            LogUtils.printStackTrace("Camera", e);
        }
        //设置监听获取视频流的每一帧
        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                nv21EncoderH264.encoderH264(data);
            }
        });
        //调用startPreview()用以更新preview的surface
        camera.startPreview();
    }

    @Nullable
    private Camera.Size getCameraPreviewSize(@NonNull Camera.Parameters parameters) {
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        Camera.Size needSize = null;
        for (Camera.Size size : list) {
            if (needSize == null) {
                needSize = size;
                continue;
            }
            if (size.width >= needSize.width) {
                if (size.height > needSize.height) {
                    needSize = size;
                }
            }
        }
        return needSize;
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        PermissionUtils.permission(Manifest.permission.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        openCamera();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showLong("你拒绝了相机使用权限");
                    }
                })
                .request();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int w, int h) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        releaseCamera();
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            LogUtils.printStackTrace("Camera", e);
        }
    }

    @Override
    public void h264(byte[] data) {
        try {
            if (outputStream != null) {
                outputStream.write(data);
            }
        } catch (IOException e) {
            LogUtils.printStackTrace("Camera", e);
        }

    }

}