package com.hdu.team.hiwanan.util.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerryYin on 7/19/16.
 */
public class FileUtils {


    /**
     * 获取路径下的所有文件（只是文件）
     *
     * @param path
     * @return
     */
    public static ArrayList<File> readFile(String path) {
        ArrayList<File> fileList = new ArrayList<>();

        File file = new File(path);
        if (file.isFile()) {
            fileList.add(file);

        } else if (file.isDirectory()) {
            String[] lists = file.list();

            for (int i = 0; i < lists.length; i++) {
                File file1 = new File(path + "/" + lists[i]);

                if (file1.isFile()) {
                    fileList.add(file1);

                } else if (file1.isDirectory()) {
                    readFile(path + "/" + lists[i]);
                }
            }
        }
        return fileList;
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean deleteFile(String path){
        Boolean ok = false;
        File file = new File(path);
        if (file.isFile()){
            ok = file.delete();
        }
        else if (file.isDirectory()){
            ok = file.delete();
        }
        return ok;
    }
}
