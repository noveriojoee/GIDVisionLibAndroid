package gid.com.gidvisionlib.Interfaces;

import android.util.SparseArray;

import com.google.android.gms.vision.text.TextBlock;

public interface IOCRListener {
    void onReceiveText(SparseArray<TextBlock> block);
}
