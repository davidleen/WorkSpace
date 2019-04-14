package com.giants3.reader.book;

/**
 *
 * epub 格式书籍解析接口类
 * Created by davidleen29 on 2016/12/6.
 */
public interface IEpubParser {
	/**
	 * 设置章节(自动解析)
	 *
	 * @param chapterIndex
	 */
	void setChapterIndex(int chapterIndex);

	/**
	 * 获取章节总数
	 *
	 * @return
	 */
	int getChapterCount();

	/**
	 * 获取上一显示章节
	 *
	 * @param chapterIndex
	 * @return
	 */
	int getEpubPreChapter(int chapterIndex);

	/**
	 * 获取下一显示章节
	 *
	 * @param chapterIndex
	 * @return
	 */
	int getEpubNextChapter(int chapterIndex);

	/**
	 * 获取指定章节
	 *
	 * @param chapterIndex
	 * @return
	 */
	EpubChapter getEpubChapter(int chapterIndex);

	String getTemporaryRelativePath();

	/**
	 * 获取解析后的epub实体
	 *
	 * @return
	 */
	EpubBook getEpub();

	boolean exportFile(String filePath);

	boolean isEpubImageFolderFile(String filePath);


	/**
	 * 判断是否预览版
	 * @return
     */
	public boolean isPreview();


	/**
	 * 判断章节是否存在
	 * @param chapterIndex
	 * @return
     */
	public boolean existChapter(int chapterIndex);

	/**
	 * 重新解析出所有epub   temp文件。
	 */
	public   void  reOpenEpubFile();

}
