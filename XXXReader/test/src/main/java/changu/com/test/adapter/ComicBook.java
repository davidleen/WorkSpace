package changu.com.test.adapter;

import com.giants3.reader.entity.Book;
import com.giants3.reader.entity.ComicChapterFile;
import com.giants3.reader.noEntity.ComicChapterInfo;
import com.xxx.reader.book.IBook;
import com.xxx.reader.comic.ComicChapter;
import com.xxx.reader.comic.ComicChapterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2018/11/25.
 */

public class ComicBook implements IBook<ComicChapter> {

    private List<ComicChapter> comicChapters;

    public ComicBook(List<ComicChapterInfo> chapterInfos)
    {

        comicChapters=new ArrayList<>();

        int size = chapterInfos.size();
        for (int i = 0; i < size; i++) {
            ComicChapterInfo comicChapterInfo = chapterInfos.get(i);
            ComicChapter comicChapter=new ComicChapter(String.valueOf(comicChapterInfo.comicChapter.id),comicChapterInfo.comicChapter.name,i);

            comicChapter.chapters=new ArrayList<>();

            int itemSize = comicChapterInfo.comicFileList.size();
            for (int j = 0; j < itemSize; j++) {
                ComicChapterFile comicChapterFile = comicChapterInfo.comicFileList.get(j);

                ComicChapterItem item=new ComicChapterItem();

                item.downloadUrl=comicChapterFile.url;
                item.width=comicChapterFile.width;
                item.height=comicChapterFile.height;
                comicChapter.chapters.add(item);

            }


            comicChapters.add(comicChapter);
        }


    }
    @Override
    public String getName() {
        return comicChapters.toString();
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public List<ComicChapter> getChapters() {
        return comicChapters;
    }

    @Override
    public ComicChapter getChapter(int index) {
        return comicChapters.get(index);
    }

    @Override
    public int getChapterCount() {
        return comicChapters.size();
    }

    @Override
    public String getBookId() {
        return String.valueOf(comicChapters.hashCode());
    }
}
