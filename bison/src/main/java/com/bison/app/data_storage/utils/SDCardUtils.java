package com.bison.app.data_storage.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SD卡相关的辅助类
 * 
 * 
 * 
 */
public class SDCardUtils
{
	private SDCardUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 是否具有外部存储权限
	 * @param context
	 * @return
	 */
	public static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
		
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}
	/**
	 * 检测SD卡否有足够的空间
	 * @param updateSize
	 * @return
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard(Environment.getExternalStorageDirectory().getAbsolutePath()));
	}
	/**获取指定路定的真实存储空间大小
	 * 
	 */
	public static long getRealSizeOnSdcard(String filePath) {
		File path = new File(filePath);
		StatFs stat = new StatFs(path.getPath());
		@SuppressWarnings("deprecation")
		long blockSize = stat.getBlockSize();
		@SuppressWarnings("deprecation")
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}
	/**
	 * 查看当前更新大小在手机上是否有足够空间
	 * @param updateSize
	 * @return
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}
	/**
	 * 获取手机存储大小
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getRealSizeOnPhone() {
		if(sdCardIsAvailable()){
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long realSize = blockSize * availableBlocks;
			return realSize;
		}
		return 0;
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath()
	{
		return Environment.getRootDirectory().getAbsolutePath();
	}


}

