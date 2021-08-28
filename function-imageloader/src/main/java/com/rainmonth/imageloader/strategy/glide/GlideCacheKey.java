package com.rainmonth.imageloader.strategy.glide;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * @author randy
 * @date 2021/08/12 1:33 下午
 */
public class GlideCacheKey implements Key {

    private final String mId;
    private final Key mSignature;

    public GlideCacheKey(String id, Key signature) {
        mId = id;
        mSignature = signature;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(mId.getBytes(Key.STRING_CHARSET_NAME));
            mSignature.updateDiskCacheKey(messageDigest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlideCacheKey that = (GlideCacheKey) o;
        return Objects.equals(mId, that.mId) &&
                Objects.equals(mSignature, that.mSignature);
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mSignature.hashCode();
        return result;
    }
}
