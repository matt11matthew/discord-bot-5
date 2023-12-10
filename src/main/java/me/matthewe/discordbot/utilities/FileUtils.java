package me.matthewe.discordbot.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Matthew E on 5/1/2018.
 */
public class FileUtils {
    public static String readFileToString(File file) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean writeStringToFile(File file, String string) {
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(file, string, Charset.defaultCharset(), false);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
