package com.giants3.reader.server.service;

import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.ComicBook;
import com.giants3.reader.entity.ComicChapter;
import com.giants3.reader.entity.ComicChapterFile;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.giants3.reader.noEntity.RemoteData;
import com.giants3.reader.server.repository.BookRepository;
import com.giants3.reader.server.repository.ComicBookRepository;
import com.giants3.reader.server.repository.ComicChapterFileRepository;
import com.giants3.reader.server.repository.ComicChapterRepository;
import com.giants3.reader.utils.Assets;
import com.giants3.reader.utils.StringUtils;
import com.giants3.reader.utils.UrlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/5/12.
 */
@Service
public class BookService  extends AbstractService{

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
    public RemoteData<Book> list()
    {


        return wrapData(bookRepository.findByNameLike(StringUtils.sqlLike("")));
    }

    public void addOne() {


        Book book=new Book();
        book.name="a new book";
        bookRepository.save(book);

    }

    public void addOneComic() {


        ComicBook book=new ComicBook();
        book.name="a new  comic book";
        book.comicFilePath="aa1111111111";
        bookRepository.save(book);

    }

    public RemoteData<ComicBook> listComic() {

        return wrapData(comicBookRepository.findByNameLike(StringUtils.sqlLike("")));

    }

    public RemoteData<ComicChapterInfo> findComicChapters(long bookId) {

        final ComicBook comicBook = comicBookRepository.findOne(bookId);

        File file=new File(comicFilePath,comicBook.name);
        if(!file.exists()||!file.isDirectory()) return wrapData();




         File[] chapters=file.listFiles();

        int len=chapters.length;
        final List<ComicChapter> comicChapters=new ArrayList<>();
        List<ComicChapterInfo> comicChapterInfos=new ArrayList<>();
        for (int i = 0; i < len; i++) {
            File chapterFile=chapters[i];
            ComicChapter comicChapter=new ComicChapter();
            comicChapter.name=chapterFile.getName();
            ComicChapterInfo info=new ComicChapterInfo();
            info.comicChapter=comicChapter;


            if(chapterFile.exists()&&chapterFile.isDirectory())
            {
                info.comicFileList=new ArrayList<>();

                final String[] list = chapterFile.list();

                for (String temp:list)
                {

                    if(temp.toLowerCase().endsWith(".jpg")||temp.toLowerCase().endsWith(".png")) {
                        ComicChapterFile comicChapterFile = new ComicChapterFile();

                        String path="comic"+ File.separator+  comicBook.name+File.separator+comicChapter.name+File.separator+temp;
                        comicChapterFile.url = Assets.completeUrl(path);
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
