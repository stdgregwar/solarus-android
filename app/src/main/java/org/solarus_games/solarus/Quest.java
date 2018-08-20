package org.solarus_games.solarus;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Quest implements Comparable<Quest> {
    private static Pattern makeFieldPattern(String field) {
        return Pattern.compile(field + " = \"([^\"]*)");
    }

    private static final Pattern longDescriptionPattern = Pattern.compile("long_description = \\[\\[\n([^\\]]*)");
    private static final Pattern shortDescriptionPattern = makeFieldPattern("short_description");
    private static final Pattern titlePattern = makeFieldPattern("title");
    private static final Pattern versionPattern = makeFieldPattern("quest_version");
    private static final Pattern authorPattern = makeFieldPattern("author");
    private static final Pattern formatPattern = makeFieldPattern("solarus_version");

    public final String path;
    public final String title;
    public final String shortDescription;
    public final String longDescription;
    public final String version;
    public final String format;
    public final String author;
    public final Bitmap logo;
    public final Bitmap icon;
    public final boolean valid;

    private static final String TAG = "Quest";
    private static final String logoFileName = "logos/logo.png";
    private static final String[] iconFileNames = {
            "logos/icon_1024.png",
            "logos/icon_512.png",
            "logos/icon_256.png",
            "logos/icon_128.png",
            "logos/icon_64.png",
            "logos/icon_48.png",
            "logos/icon_32.png",
            "logos/icon_24.png",
            "logos/icon_16.png",
    };

    /**
     * Create a valid quest with all info
     * @param path the file/folder path
     * @param title quest title
     * @param shortDescription quest short description
     * @param longDescription long description
     * @param version version of the quest
     * @param author author of the quest
     * @param logo logo of the quest or placeholder logo
     * @param icon icon of the quest or placeholder icon
     */
    private Quest(String path, String title, String shortDescription, String longDescription, String version, String format, String author, Bitmap logo, Bitmap icon) {
        this.path = path;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.version = version;
        this.format = format;
        this.author = author;
        this.logo = logo;
        this.icon = icon;
        valid = true;
    }

    /**
     * Create an invalid quest with the given invalid path
     *
     * @param path
     */
    private Quest(String path) {
        this.path = path;
        title = null;
        shortDescription = null;
        longDescription = null;
        version = null;
        format = null;
        author = null;
        logo = null;
        icon = null;
        valid = false;
    }

    private static String getFieldOrEmpty(Pattern p,String data) {
        Matcher m = p.matcher(data);
        if(m.find()) {
            return m.group(1);
        }
        return "";
    }

    private static Quest fromQuestDatAndImages(String path, String questDatContent, Bitmap logo, Bitmap icon) {
        String longDescription = getFieldOrEmpty(longDescriptionPattern,questDatContent);
        String shortDescription = getFieldOrEmpty(shortDescriptionPattern,questDatContent);
        String title = getFieldOrEmpty(titlePattern,questDatContent);
        String version = getFieldOrEmpty(versionPattern,questDatContent);
        String format = getFieldOrEmpty(formatPattern,questDatContent);
        String author = getFieldOrEmpty(authorPattern,questDatContent);
        return new Quest(path,title,shortDescription,longDescription,version,format,author,logo,icon);
    }

    private static byte[] IStreamToBytes(InputStream stream, int size) throws  IOException {
        byte[] buffer = new byte[size];
        stream.read(buffer);
        return buffer;
    }

    private static String IStreamToString(InputStream stream, int size) throws IOException {
        return new String(IStreamToBytes(stream,size),"UTF8");
    }

    public static Bitmap getLogoFromZip(ZipFile zipFile) throws IOException {
        ZipEntry logoEntry = zipFile.getEntry(logoFileName);
        if(logoEntry == null) {
            return BitmapFactory.decodeResource(SolarusApp.getContext().getResources(),R.drawable.no_logo);
        }
        return BitmapFactory.decodeStream(zipFile.getInputStream(logoEntry));
    }

    public static Bitmap getLogoFromFolder(String folder) throws IOException {
        File logoFile = new File(folder + logoFileName);
        if(!logoFile.exists()) {
            return BitmapFactory.decodeResource(SolarusApp.getContext().getResources(),R.drawable.no_logo);
        }
        return BitmapFactory.decodeStream(new FileInputStream(logoFile));
    }

    public static Bitmap getIconFromFolder(String folder) throws  IOException {
        for(String fileName : iconFileNames) {
            File file = new File(folder + fileName);
            if(file.exists()) {
                return BitmapFactory.decodeStream(new FileInputStream(file));
            }
        }
        return BitmapFactory.decodeResource(SolarusApp.getContext().getResources(),R.drawable.default_icon);
    }

    public static Bitmap getIconFromZip(ZipFile zipFile) throws IOException {
        for(String fileName : iconFileNames) {
            ZipEntry fileEntry = zipFile.getEntry(fileName);
            if(fileEntry != null) {
                return BitmapFactory.decodeStream(zipFile.getInputStream(fileEntry));
            }
        }
        return BitmapFactory.decodeResource(SolarusApp.getContext().getResources(),R.drawable.default_icon);
    }

    public static Quest invalidQuest(String path) {
        return new Quest(path);
    }

    public static Quest fromZipFile(String path) {
            try {
                ZipFile zipFile = new ZipFile(path);
                ZipEntry questDatEntry = zipFile.getEntry("quest.dat");
                if (questDatEntry == null) {
                    Log.d(TAG, "no quest.dat found!");
                    return new Quest(path);
                }
                InputStream questDatIStream = zipFile.getInputStream(questDatEntry);
                String questDatContent = IStreamToString(questDatIStream, (int) questDatEntry.getSize());

                return fromQuestDatAndImages(path,
                        questDatContent,
                        getLogoFromZip(zipFile),
                        getIconFromZip(zipFile));
            } catch (IOException e) {
                return invalidQuest(path);
            }
    }

    public static Quest fromFolder(String path) {
        try {
            String dataFolder = path + "/data/";
            File questDatFile = new File(dataFolder + "quest.dat");
            if (!questDatFile.exists()) {
                return new Quest(path);
            }
            InputStream questDatIStream = new FileInputStream(questDatFile);
            String questDatContent = IStreamToString(questDatIStream, (int) questDatFile.length());
            return fromQuestDatAndImages(path,
                    questDatContent,
                    getLogoFromFolder(dataFolder),
                    getIconFromFolder(dataFolder));
        } catch (IOException e) {
            return invalidQuest(path);
        }
    }

    public static Quest fromPath(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return invalidQuest(path);
        } else if (file.isDirectory()) {
            return fromFolder(path);
        } else {
            return fromZipFile(path);
        }
    }

    @Override
    public String toString() {
        return "Quest :" + title +
                "\n" + shortDescription +
                "\n" + longDescription +
                "\n" + author +
                "\n" + version;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Quest) {
            Quest otherq = (Quest) other;
            return otherq.path.equals(path);
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Quest quest) {
        if(quest.valid && valid) {
            return title.compareTo(quest.title);
        } else {
            return path.compareTo(quest.path);
        }
    }
}
