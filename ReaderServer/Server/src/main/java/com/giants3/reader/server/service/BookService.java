package com.giants3.reader.server.service;

import com.giants3.reader.entity.*;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.repository.BookRepository;
import com.giants3.reader.server.repository.ComicBookRepository;
import com.giants3.reader.server.repository.ComicChapterFileRepository;
import com.giants3.reader.server.repository.ComicChapterRepository;
import com.giants3.utils.Assets;
import com.giants3.utils.StringUtils;
import de.greenrobot.common.io.IoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by davidleen29 on 2018/5/12.
 */
@Service
public class BookService extends AbstractService {

    @Value("${rootPath}")
    private String rootPath;
    @Value("${comicFilePath}")
    private String comicFilePath;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ComicBookRepository comicBookRepository;
    @Autowired
    ComicChapterRepository comicChapterRepository;
    @Autowired
    ComicChapterFileRepository comicChapterFileRepository;

    public RemoteData<Book> list() {


        return wrapData(bookRepository.findByNameLike(StringUtils.sqlLike("")));
    }

    public void addOne() {


        Book book = new Book();
        book.name = "a new book";
        bookRepository.save(book);

    }

    public void addOneComic() {


        ComicBook book = new ComicBook();
        book.name = "a new  comic book";
        book.comicFilePath = "aa1111111111";
        bookRepository.save(book);

    }

    public RemoteData<ComicBook> listComic() {

        return wrapData(comicBookRepository.findByNameLike(StringUtils.sqlLike("")));

    }

    public RemoteData<Chapter> findChapters(long bookId, int pageIndex, int pageSize) {


        return wrapData();


    }


    public RemoteData<Book> listBooks() {

        File file = new File(rootPath);

        List<File> allChildFile=new ArrayList<>();
        findAllFiles(allChildFile,file);

        List<Book> books = new ArrayList<>();

        for (int i = 0; i < allChildFile.size(); i++) {

            File item = allChildFile.get(i);
            Book book = new Book();
            book.id = i;
            book.name = item.getName();
            String relativePath=item.getAbsolutePath().replace(file.getAbsolutePath(),"");
            book.url = Assets.completeUrl(relativePath);
            books.add(book);
        }


        return wrapData(books);

    }



    private void findAllFiles(List<File> files,File aFile)
    {
        if (aFile==null) return;
        if(aFile.isFile())
        {
            files.add(aFile);
        }else
            if(aFile.isDirectory()) {
                File[] children = aFile.listFiles();
                if (children != null) {
                    for (File item : children) {
                        findAllFiles(files, item);
                    }

                }
            }

    }



    public RemoteData<ComicChapterInfo> findComicChapters(long bookId) {

        final ComicBook comicBook = comicBookRepository.findOne(bookId);

        File file = new File(comicFilePath, comicBook.name);
        if (!file.exists() || !file.isDirectory()) return wrapData();


        File[] chapters = file.listFiles();

        int len = chapters.length;
        final List<ComicChapter> comicChapters = new ArrayList<>();
        List<ComicChapterInfo> comicChapterInfos = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            File chapterFile = chapters[i];
            ComicChapter comicChapter = new ComicChapter();
            comicChapter.name = chapterFile.getName();
            ComicChapterInfo info = new ComicChapterInfo();
            info.comicChapter = comicChapter;


            if (chapterFile.exists() && chapterFile.isDirectory()) {
                info.comicFileList = new ArrayList<>();

                final String[] list = chapterFile.list();

                for (String temp : list) {

                    if (temp.toLowerCase().endsWith(".jpg") || temp.toLowerCase().endsWith(".png")) {


                        ComicChapterFile comicChapterFile = new ComicChapterFile();
                        String path = "comic" + File.separator + comicBook.name + File.separator + comicChapter.name + File.separator + temp;
                        comicChapterFile.url = Assets.completeUrl(path);

                        int width = 1000;
                        int height = 1000;
                        FileInputStream input = null;
                        try {
                            input = new FileInputStream(new File(chapterFile, temp));
                            final ImageInputStream imageInputStream = ImageIO.createImageInputStream(input);


                            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
                            while (readers.hasNext()) {
                                ImageReader reader = readers.next();
                                reader.setInput(imageInputStream);
                                width = reader.getWidth(0);
                                height = reader.getHeight(0);
                            }


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {

                            IoUtils.safeClose(input);
                        }


                        comicChapterFile.width = width;
                        comicChapterFile.height = height;
                        info.comicFileList.add(comicChapterFile);
                    }
                }


            }

            comicChapterInfos.add(info);
        }

        return wrapData(comicChapterInfos);


//        final List<ComicChapter> chapters = comicChapterRepository.findByBookIdEquals(bookId);
//
//        for(ComicChapter chapter:chapters)
//        {
//            ComicChapterInfo comicChapterInfo=new ComicChapterInfo();
//            comicChapterInfo.comicChapter=chapter;
//
//
//           List< ComicChapterFile> comicFiles=comicChapterFileRepository.findByBookIdEqualsAndChapterIdEqualsOrderByIndexDesc(bookId,chapter.id);
//
//            comicChapterInfo.comicFileList=comicFiles;
//            comicChapterInfos.add(comicChapterInfo);
//
//        }
//        return wrapData(comicChapterInfos);

    }
}
