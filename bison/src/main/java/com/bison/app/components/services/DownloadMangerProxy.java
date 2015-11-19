package com.bison.app.components.services;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.bison.app.commons.utils.IOUtils;
import com.developer.bsince.log.GOL;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by oeager on 2015/5/28.
 * email: oeager@foxmail.com
 */
public class DownloadMangerProxy {

    private Context mContext;

    private boolean isRepeatQuery = false;

    private CompleteReceiver receiver = new CompleteReceiver();

    private final static int queryDelay = 1000;

    volatile static DownloadMangerProxy INSTANCE;

    private final DownloadManager downloadManager;

    private final Set<Long> requestIds = new HashSet<>();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final List<OnDownloadErrorListener> errorListeners = new ArrayList<>();

    private final List<OnDownloadCancelListener> cancelListeners = new ArrayList<>();

    private final List<OnDownloadCompleteListener> completeListeners = new ArrayList<>();

    private final List<OnDownloadProgressChangedListener> progressChangedListeners = new ArrayList<>();


    private DownloadMangerProxy(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.mContext.registerReceiver(receiver, filter);

    }

    public static DownloadMangerProxy getDefault(Context mContext) {
        if (INSTANCE == null) {
            synchronized (DownloadMangerProxy.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DownloadMangerProxy(mContext);
                }
            }
        }
        return INSTANCE;
    }

    public void release(){
        mContext.unregisterReceiver(receiver);
        errorListeners.clear();
        progressChangedListeners.clear();
        completeListeners.clear();
        cancelListeners.clear();
        INSTANCE = null;
    }

    public long enqueue(UpgradeInfo upgradeInfo){
        return enqueue(upgradeInfo,null);
    }

    public long enqueue(UpgradeInfo upgradeInfo,RequestAdapter adapter) {
        //检查是否已在正在下载任务中
        DownloadManager.Query query = new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI));
                if (upgradeInfo.linkUri.equals(uri)) {
                    GOL.d("same task is running");
                    IOUtils.closeQuietly(cursor);
                    return cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
                }
            }
        }
        IOUtils.closeQuietly(cursor);
        //检查文件目录存在与否
        File directory = new File(upgradeInfo.localUri);
        if (!directory.exists() || !directory.isDirectory()) {
            GOL.d("directory is not existed or  not a directory ,mk!");
            directory.mkdirs();
        }

        File downloadFile = new File(upgradeInfo.localUri + upgradeInfo.fileName);
        //检查是否存在已下载好的文件
        if (downloadFile.exists()) {
            //检查是否有下载成功的记录
            GOL.d("file exists");
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            cursor = downloadManager.query(query);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_URI));
                    //验证链接
                    if (upgradeInfo.linkUri.equals(uri)) {
                        GOL.d("the record found");
                        final Uri localPath = getLocalUri(cursor);
                        //验证这条记录是不是相同的存放位置
                        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
                        GOL.d("userPath:[%s],localPath:[%s]", downloadFile.getPath(), localPath.getPath());
                        if (downloadFile.getPath().equals(localPath.getPath())) {
                            GOL.d("the record is right");
                            IOUtils.closeQuietly(cursor);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    callComplete(id, localPath);
                                }
                            },1000);
                            return id;

                        }
                        //移除这条记录
                        downloadManager.remove(id);

                    }

                }
            }
            if (downloadFile.exists()) {
                downloadFile.delete();
            }
            GOL.d("download file is not  match the record , download once more");
        }
        IOUtils.closeQuietly(cursor);
        //添加新的下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(upgradeInfo.linkUri));
        request.setDestinationUri(Uri.fromFile(downloadFile));
        request.setTitle(upgradeInfo.title);//设置下载中通知栏提示的标题
        request.setDescription(upgradeInfo.description);//设置下载中通知栏提示的介绍
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //request.setMimeType("application/com.developer.baisince.download");
        //request.allowScanningByMediaScanner();//表示允许MediaScanner扫描到这个文件，默认不允许。
        //request.setAllowedOverRoaming(false);//移动网络情况下是否允许漫游。
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);//表示下载允许的网络类型，默认在任何网络下都允许下载
        if(adapter!=null)adapter.applyOptions(request);
        long id = downloadManager.enqueue(request);
        requestIds.add(id);
        return id;
    }

    public boolean cancel(long id) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        Cursor c = downloadManager.query(query);
        boolean flag = false;
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_PAUSED || status == DownloadManager.STATUS_RUNNING) {
                callCancel(id);
                flag = true;
            }
        }
        requestIds.remove(Long.valueOf(id));
        downloadManager.remove(id);
        return flag;
    }


    public synchronized void addDownloadProgressChangedListener(OnDownloadProgressChangedListener listener) {
        if (!isRepeatQuery) {
            isRepeatQuery = true;
            mHandler.postDelayed(queryRunnable,queryDelay);
        }
        progressChangedListeners.add(listener);
    }

    public void addDownloadCompleteListener(OnDownloadCompleteListener listener) {
        completeListeners.add(listener);
    }

    public void addDownloadCancelListener(OnDownloadCancelListener listener) {
        cancelListeners.add(listener);
    }

    public synchronized void addDownloadErrorListener(OnDownloadErrorListener listener) {
        if (!isRepeatQuery) {
            isRepeatQuery = true;
            mHandler.postDelayed(queryRunnable,queryDelay);
        }
        errorListeners.add(listener);
    }


    private final Runnable queryRunnable = new Runnable() {
        @Override
        public void run() {
            if(requestIds.size()==0){
                isRepeatQuery = false;
                GOL.d("running request is not empty ,stop query");
                mHandler.removeCallbacks(this);
                return;
            }
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(convertIds());
            Cursor c = downloadManager.query(query);
            GOL.d("reQuery....ids.length==%d",requestIds.size());
            if (c != null) {

                while (c.moveToNext()){
                    int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                    long id = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
                    if(status==DownloadManager.STATUS_FAILED){
                        GOL.d("the request[id=%d] is failed",id);
                        int reason = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON));
                        callError(id,reason);
                        requestIds.remove(Long.valueOf(id));
                    }else{
                        if(status == DownloadManager.STATUS_SUCCESSFUL){
                            GOL.d("the request[id=%d] is success",id);
                            requestIds.remove(Long.valueOf(id));
                        }
                        int current = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        int total = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        callProgress(id,current,total);
                    }
                }
            }
            IOUtils.closeQuietly(c);
            if(requestIds.size()>0){
                GOL.d("running request is not empty ,query go on");
                mHandler.postDelayed(this, queryDelay);
            }
        }
    };

    private long[] convertIds() {
        long [] ids = new long[requestIds.size()];
        Long [] objs = new Long[requestIds.size()];
        requestIds.toArray(objs);
        for (int i = 0,size = requestIds.size();i<size;i++){
            ids[i] = objs[i].longValue();
        }

        return ids;
    }

    private Uri getLocalUri(Cursor cursor) {
        String localUri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
        Uri localPath = Uri.parse(localUri);
        if (localPath.getScheme() == null) {
            localPath = Uri.fromFile(new File(localUri));
        }
        return localPath;
    }

    private void callComplete(long id, Uri localUri) {
        for (int i = 0, size = completeListeners.size(); i < size; i++) {
            completeListeners.get(i).onDownloadComplete(id, localUri);
        }
    }

    private void callError(long id, int reason) {
        for (int i = 0, size = errorListeners.size(); i < size; i++) {
            errorListeners.get(i).onError(id, reason);
        }
    }

    private void callProgress(long id, int currentSize, int totalSize) {
        for (int i = 0, size = progressChangedListeners.size(); i < size; i++) {
            progressChangedListeners.get(i).onProgressChanged(id, currentSize, totalSize);
        }
    }

    private void callCancel(long id) {
        for (int i = 0, size = cancelListeners.size(); i < size; i++) {
            cancelListeners.get(i).onCancel(id);
        }
    }

    public interface OnDownloadProgressChangedListener{

        void onProgressChanged(long id, int currentSize, int totalSize);
    }

    public interface  OnDownloadCompleteListener{

        void onDownloadComplete(long id, Uri localUri);
    }

    public interface OnDownloadCancelListener{

        void onCancel(long id);
    }

    public interface OnDownloadErrorListener{
        void onError(long id, int errorCode);
    }

    public interface RequestAdapter{

        void applyOptions(DownloadManager.Request request);
    }


    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(completeDownloadId);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    Uri localUri = getLocalUri(cursor);
                    IOUtils.closeQuietly(cursor);
                    callComplete(completeDownloadId, localUri);
                    return;
                }
            }
            IOUtils.closeQuietly(cursor);


        }
    }


}
