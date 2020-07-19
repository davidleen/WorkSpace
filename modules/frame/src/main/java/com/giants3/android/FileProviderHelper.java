package com.giants3.android;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;


/**
 * provider 解析 处理外部应用传递过来的content://的处理。
 * <p>
 * 代码复制自{@link FileProvider}
 * <p>
 * Strategy that provides access to files living under a narrow whitelist of
 * filesystem roots. It will throw {@link SecurityException} if callers try
 * accessing files outside the configured roots.
 * <p>
 * For example, if configured with
 * {@code addRoot("myfiles", context.getFilesDir())}, then
 * {@code context.getFileStreamPath("foo.txt")} would map to
 * {@code content://myauthority/myfiles/foo.txt}.
 */
public class FileProviderHelper {

    private static final String[] COLUMNS = {
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE};

    private static final String
            META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_ROOT_PATH = "root-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final File DEVICE_ROOT = new File("/");


    private static HashMap<String, PathStrategy> sCache = new HashMap<String, PathStrategy>();


    public static Uri getUriForFile(Context context, String authority, File file) throws IOException, XmlPullParserException {
        final PathStrategy strategy = getPathStrategy(context, authority);
        return strategy.getUriForFile(file);
    }

    /**
     * 从content://中获取文件路径
     *
     * @param context
     * @param uri
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static File getFileFormUri(Context context, Uri uri) {


        //首先尝试解析系统的配置。   例如：content://media/external/file/220185
        String filePath = getFilePathFromContentUri(uri, context.getContentResolver());
        File result;
        if (!TextUtils.isEmpty(filePath)) {
            result = new File(filePath);
            if (result.exists()) return result;
        }
        //其次解析应用分享的contentUrl   比如  content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/%E5%90%84%E7%A7%8D%E4%B9%A6%E7%B1%8D/%5Bcomic%5D%5B%E6%B3%A2%E6%96%AF%E7%8E%8B%E5%AD%90.cbr
        result = null;
        try {
            final PathStrategy strategy;
            strategy = getPathStrategy(context, uri.getAuthority());

            result = strategy.getFileForUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public static PathStrategy getPathStrategy(Context context, String authority) throws IOException, XmlPullParserException {


        final SimplePathStrategy strat = new SimplePathStrategy(authority);
        final ProviderInfo info = context.getPackageManager()
                .resolveContentProvider(authority, PackageManager.GET_META_DATA);
        final XmlResourceParser in = info.loadXmlMetaData(
                context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
        if (in == null) {
            throw new IllegalArgumentException(
                    "Missing " + META_DATA_FILE_PROVIDER_PATHS + " meta-data");
        }

        int type;
        while ((type = in.next()) != END_DOCUMENT) {
            if (type == START_TAG) {
                final String tag = in.getName();

                final String name = in.getAttributeValue(null, ATTR_NAME);
                String path = in.getAttributeValue(null, ATTR_PATH);

                File target = null;
                if (TAG_ROOT_PATH.equals(tag)) {
                    target = DEVICE_ROOT;
                } else if (TAG_FILES_PATH.equals(tag)) {
                    target = context.getFilesDir();
                } else if (TAG_CACHE_PATH.equals(tag)) {
                    target = context.getCacheDir();
                } else if (TAG_EXTERNAL.equals(tag)) {
                    target = Environment.getExternalStorageDirectory();
                } else if (TAG_EXTERNAL_FILES.equals(tag)) {
                    File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                    if (externalFilesDirs.length > 0) {
                        target = externalFilesDirs[0];
                    }
                } else if (TAG_EXTERNAL_CACHE.equals(tag)) {
                    File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    if (externalCacheDirs.length > 0) {
                        target = externalCacheDirs[0];
                    }
                }

                if (target != null) {
                    strat.addRoot(name, buildPath(target, path));
                }
            }
        }

        return strat;


    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
    }


    public static String getFilePathFromContentUri() {


        return null;

    }


    static class SimplePathStrategy implements PathStrategy {

        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap<String, File>();

        public SimplePathStrategy(String authority) {
            mAuthority = authority;
        }

        /**
         * Add a mapping from a name to a filesystem root. The provider only offers
         * access to files that live under configured roots.
         */
        public void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }

            try {
                // Resolve to canonical path to keep path checking fast
                root = root.getCanonicalFile();
            } catch (IOException e) {
                throw new IllegalArgumentException(
                        "Failed to resolve canonical path for " + root, e);
            }

            mRoots.put(name, root);
        }

        @Override
        public Uri getUriForFile(File file) {
            String path;
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }

            // Find the most-specific root path
            Map.Entry<String, File> mostSpecific = null;
            for (Map.Entry<String, File> root : mRoots.entrySet()) {
                final String rootPath = root.getValue().getPath();
                if (path.startsWith(rootPath) && (mostSpecific == null
                        || rootPath.length() > mostSpecific.getValue().getPath().length())) {
                    mostSpecific = root;
                }
            }

            if (mostSpecific == null) {
                throw new IllegalArgumentException(
                        "Failed to find configured root that contains " + path);
            }

            // Start at first char of path under root
            final String rootPath = mostSpecific.getValue().getPath();
            if (rootPath.endsWith("/")) {
                path = path.substring(rootPath.length());
            } else {
                path = path.substring(rootPath.length() + 1);
            }

            // Encode the tag and path separately
            path = Uri.encode(mostSpecific.getKey()) + '/' + Uri.encode(path, "/");
            return new Uri.Builder().scheme("content")
                    .authority(mAuthority).encodedPath(path).build();
        }

        @Override
        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();

            final int splitIndex = path.indexOf('/', 1);
            final String tag = Uri.decode(path.substring(1, splitIndex));
            path = Uri.decode(path.substring(splitIndex + 1));

            final File root = mRoots.get(tag);
            if (root == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            }

            File file = new File(root, path);
            try {
                file = file.getCanonicalFile();
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }

            if (!file.getPath().startsWith(root.getPath())) {
                throw new SecurityException("Resolved path jumped beyond configured root");
            }

            return file;
        }


    }


    interface PathStrategy {
        /**
         * Return a {@link Uri} that represents the given {@link File}.
         */
        public Uri getUriForFile(File file);

        /**
         * Return a {@link File} that represents the given {@link Uri}.
         */
        public File getFileForUri(Uri uri);
    }


    /**
     * 系统content 文件路径处理   例如：content://media/external/file/220185
     *
     * @param selectedVideoUri
     * @param contentResolver
     * @return
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {

        String filePath = null;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//	    也可用下面的方法拿到cursor
//	    Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }
}