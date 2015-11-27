package com.bison.app.components.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;

import java.util.List;

/**
 * Created by oeager on 2015/11/23.
 * email:oeager@foxmail.com
 */
public abstract class BaseListLoader<E> extends AsyncTaskLoader<List<E>> {

    CancellationSignal mCancellationSignal;

    public BaseListLoader(Context context) {
        super(context);
    }

    @Override
    public List<E> loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }
        try {
           return doInBackground();
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
            }
        }
    }

    protected abstract List<E> doInBackground();


    @Override
    public boolean isLoadInBackgroundCanceled() {
        return super.isLoadInBackgroundCanceled();
    }
}
    