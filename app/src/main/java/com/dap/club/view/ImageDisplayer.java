package com.dap.club.view;

import android.net.Uri;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Created by dap on 15/12/18.
 */
public interface ImageDisplayer {
    Uri getUri();

    void setUri(Uri uri);

    boolean getWebPEnabled();

    void setWebPEnabled(boolean value);

    void setOnImageChangeListener(OnImageChangeListener onImageChangeListener);

    interface OnImageChangeListener {
        void onImageLoading(int percent);

        void onImageLoaded(ImageInfo imageInfo);

        void onImageLoadError();
    }
}
