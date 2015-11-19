package com.bison.app.components.services;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.bison.app.R;
import com.bison.app.components.utils.AppUtils;
import com.bison.app.data_storage.utils.FileUtil;
import com.bison.app.data_storage.utils.SDCardUtils;
import com.bison.app.text_input.utils.TextUtils;
import com.developer.bsince.log.GOL;

/**
 * Created by oeager on 2015/6/3.
 * email: oeager@foxmail.com
 */
public class UpgradeInfo {

    public final String linkUri;

    public final String localUri;

    public final String fileName;

    public final String tempName;

    public final String newVersionName;

    public final int newVersionCode;

    public final String oldVersionName;

    public final int oldVersionCode;

    public final boolean forceUpgrade;

    public final String title;

    public final String description;

    public final String releaseNote;


    private UpgradeInfo(Builder builder) {
        this.linkUri = builder.linkUri;
        this.localUri = builder.localUri;
        this.fileName = builder.fileName;
        this.tempName = builder.tempName;
        this.title = builder.title;
        this.description = builder.description;
        this.oldVersionCode = builder.oldVersionCode;
        this.oldVersionName = builder.oldVersionName;
        this.newVersionCode = builder.newVersionCode;
        this.newVersionName = builder.newVersionName;
        this.forceUpgrade = builder.forceUpgrade;
        this.releaseNote = builder.releaseNote;
    }


    public static class Builder {
        private String linkUri;

        private String localUri;

        private String fileName;

        private String tempName;

        private String title;

        private String description;

        private String oldVersionName;

        private int oldVersionCode;

        private String newVersionName;

        private int newVersionCode;

        private String releaseNote;

        private boolean forceUpgrade = false;

        public Builder linkUri(String linkUri) {
            this.linkUri = linkUri;
            return this;
        }

        public Builder localUri(String localUri) {
            this.localUri = localUri;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder forceUpgrade(boolean forceUpgrade){
            this.forceUpgrade = forceUpgrade;
            return this;
        }

        public Builder newVersionName(String newVersionName){
            this.newVersionName = newVersionName;
            return this;
        }

        public Builder newVersionCode(int newVersionCode){
            this.newVersionCode = newVersionCode;
            return this;
        }

        public Builder releaseNote(String releaseNote){
            this.releaseNote = releaseNote;
            return this;
        }

        public UpgradeInfo build(Context mContext){
            if(TextUtils.isEmpty(linkUri)){
                throw new IllegalArgumentException("linkUri can not be null");
            }

            if (TextUtils.isEmpty(localUri)){
               if(!SDCardUtils.hasExternalStoragePermission(mContext)){
                  throw new IllegalArgumentException("localUri auto set should must have external storage permission");
               }
                localUri = SDCardUtils.getSDCardPath();
            }

            PackageInfo pi= AppUtils.getPackageInfo(mContext);
            oldVersionCode = pi.versionCode;
            oldVersionName = pi.versionName;

            if(TextUtils.isEmpty(newVersionName)){
               if(oldVersionName.indexOf('.')!=-1){
                   int index = oldVersionName.lastIndexOf('.');
                   newVersionName = oldVersionName.substring(0,index+1)+newVersionCode;
               }
            }

            if(TextUtils.isEmpty(fileName)){
                fileName = FileUtil.getFileName(linkUri);
            }
            StringBuilder buffer = new StringBuilder();
            String fn = FileUtil.getFileNameNoFormat(linkUri);
            String format = FileUtil.getFileFormat(fileName);

            buffer.append(fn).append("_").append(newVersionName).append(".").append(format);
            fileName = buffer.toString();

            tempName = getTempFileName(fileName);

            if(TextUtils.isEmpty(title)){
                title = mContext.getResources().getString(pi.applicationInfo.labelRes);
            }

            if(TextUtils.isEmpty(description)){
                description = "app download";
            }
            if(TextUtils.isEmpty(releaseNote)){
                releaseNote = mContext.getResources().getString(R.string.empty_releaseNote);
            }

            UpgradeInfo ui= new UpgradeInfo(this);
            GOL.d(ui.toString());
            return ui;

        }


    }
    private static String getTempFileName(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex != -1) {
            String tempName = fileName.substring(0, lastIndex);
            return tempName + "_temp";
        } else {
            return fileName + "_temp";
        }
    }

    @Override
    public String toString() {
       return String.format("linkUrl[%1$s]\n" +
               "localUri[%2$s]\n" +
               "fileName[%3$s]\n" +
               "tempFileName[%4$s]\n" +
               "newVersionName[%5$s]\n" +
               "newVesionCode[%6$s]\n" +
               "oldVersionName[%7$s]\n" +
               "oldVersionCode[%8$d]\n" +
               "forceUpdate[%9$d]\n" +
               "title[%10$s]\n" +
               "description[%11$s]\n" +
               "releaseNote[%12$s]\n",linkUri,localUri,fileName,tempName,newVersionName,newVersionCode,oldVersionName,oldVersionCode,forceUpgrade?1:0,title,description,releaseNote);
    }
}
