package gid.com.gidvisionlib.ViewActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64DataException;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import gid.com.gidvisionlib.Common.Activity.BaseActivity;
import gid.com.gidvisionlib.Common.Application.Application;
import gid.com.gidvisionlib.Common.Handlers.RequestPermissionHandler;
import gid.com.gidvisionlib.Interfaces.IOCRListener;
import gid.com.gidvisionlib.Interfaces.RequestPermissionListener;
import gid.com.gidvisionlib.R;
import gid.com.gidvisionlib.Utilities.Camera.CameraSource;
import gid.com.gidvisionlib.Utilities.Camera.CameraSourcePreview;
import gid.com.gidvisionlib.Utilities.OcrDetectorProcessor;
import gid.com.gidvisionlib.ViewModels.OcrDetectorViewModel;

public class GIDLibVisionMainActivity extends BaseActivity implements IOCRListener, CameraSource.PictureCallback, RequestPermissionListener {

    //Activity State
    public static final String ACTIVITY_STATUS = "ACTIVITY_STATUS";
    public static final String OCR_CAPTURED_TEXT = "OCR_CAPTURED_TEXT";
    public static final String OCR_CAPTURED_IMG = "OCR_CAPTURED_IMG";

    // Constants used to pass extra data in the intent
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";


    private final static String TAG = "lib";
    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_ALL_REQUIRED_PERM = 123;

    //Flag is on process recognizing text or not
    private static boolean isReadingImage = false;


    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private TextView tvOcrResult;
    private OcrDetectorViewModel viewModel;
    private RequestPermissionHandler permissionHandler;
    private boolean autoFocus;
    private boolean useFlash;

    @Override
    protected void bindView() {
        tvOcrResult = (TextView) findViewById(R.id.tvOcrResult);
        preview = (CameraSourcePreview) findViewById(R.id.preview);
    }

    @Override
    protected void registerEvent() {
        // Set good defaults for capturing text.
        this.autoFocus = true;
        this.useFlash = false;
        this.permissionHandler = new RequestPermissionHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onCreateSetContentView(R.layout.activity_gid_lib_vision_main_activity);
        this.getExtrasData();
        this.requestSetsPermission("all_required");
    }

    private void getExtrasData() {
        Bundle bundle = getIntent().getExtras();
        this.viewModel = new OcrDetectorViewModel();
        this.viewModel.setOcrMode(bundle.getString("OCR_MODE"));
        GIDLibVisionMainActivity.isReadingImage = false;
    }

    private void requestSetsPermission(String permissiontype) {
        int[] listPermissionStatus = new int[3];
        String[] listPermission = new String[3];
        if(permissiontype.equals("all_required")){
            listPermissionStatus[0] = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionStatus[1] = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionStatus[2] = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            listPermission[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
            listPermission[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            listPermission[2] = Manifest.permission.CAMERA;
        }
        Boolean isRequiredPermission = false;

        for (int i = 0; i < listPermissionStatus.length; i++){
            if (listPermissionStatus[i] == PackageManager.PERMISSION_DENIED){
                //request permission
                isRequiredPermission = true;
            }
        }

        if (isRequiredPermission){
            this.permissionHandler.requestPermission(this,listPermission, 123, this);
        }else{
            this.createCameraSource(this.autoFocus, this.useFlash);
        }
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // TODO: Create the TextRecognizer
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        // TODO: Set the TextRecognizer's Processor.
        textRecognizer.setProcessor(new OcrDetectorProcessor(this));


        // TODO: Check if the TextRecognizer is operational.
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "low storage", Toast.LENGTH_LONG).show();
                Log.w(TAG, "low storage");
            }
        }

        // TODO: Create the cameraSource using the TextRecognizer.
        cameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(15.0f)
                        .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                        .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO : null)
                        .build();
    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (cameraSource != null) {
            try {
                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.startCameraSource();
    }


    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (preview != null) {
            preview.stop();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preview != null) {
            preview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_ALL_REQUIRED_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // We have permission, so create the camerasource
            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            this.createCameraSource(this.autoFocus, this.useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage("Permission for camera isn't granted ")
                .setPositiveButton("OK", listener)
                .show();
    }

    @Override
    public void onReceiveText(SparseArray<TextBlock> blocks) {
        if (isReadingImage == false) {
            isReadingImage = true;
            String textPerRow = "";
            for (int i = 0; i < blocks.size(); ++i) {
                TextBlock block = blocks.get(i);
                if (block != null && block.getValue() != null) {
                    textPerRow += block.getValue() + "\n";
                }
            }
            final String textResult = textPerRow;
            this.viewModel.setTextCaptured(textResult);
            final String result = this.viewModel.extractData();
            if (!result.equals("NOT_FOUND")) {
                //dismiss activity
                //release camera memory
                GIDLibVisionMainActivity.this.cameraSource.takePicture(null, this);
            } else {
                //Debug mode
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvOcrResult.setText(textResult);
                    }
                });
                isReadingImage = false;
            }
        }

    }


    @Override
    public void onPictureTaken(byte[] data) {
        try {
            this.cameraSource.stop();
            this.viewModel.setImageCaptured(data);

            Intent intent = new Intent();
            intent.putExtra(GIDLibVisionMainActivity.ACTIVITY_STATUS, Application.ACTIVITY_STATUS_OK);
            intent.putExtra(GIDLibVisionMainActivity.OCR_CAPTURED_TEXT,this.viewModel.getTextResult());
            intent.putExtra(GIDLibVisionMainActivity.OCR_CAPTURED_IMG,this.viewModel.getImageCapturedPath());
            this.setResult(Activity.RESULT_OK, intent);
            this.finish();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public String getResponseData(){
        String result = "";
        JSONObject responseEnvelope = new JSONObject();
        JSONObject responseBody = new JSONObject();
        try{
            //setup responseBody
            responseBody.put("CapturedText",this.viewModel.getTextCaptured());
            responseBody.put("CapturedImage",this.viewModel.getBase64Image());
            responseEnvelope.put("ErrCode","99999");
            responseEnvelope.put("ErrStatus","ERROR");
            responseEnvelope.put("ServiceResult",responseBody);

            result = responseEnvelope.toString();

        }catch(Base64DataException bse){
            Log.d(TAG, bse.getMessage());
        }
        catch(JSONException je){
            Log.d(TAG, je.getMessage());
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }


        return result;
    }

    //BaseActivity Methods
    @Override
    public void goToNewIntent(int id) {

    }


    //OnRequest Permission Storage ALLOWED
    @Override
    public void onSuccess() {

    }
    //OnRequest Permission Storage DENIED
    @Override
    public void onFailed() {

    }
}
