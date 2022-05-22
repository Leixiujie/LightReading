package com.cqupt.readingcloud.book.service.impl;

import com.cqupt.readingcloud.book.dao.BookChapterMapper;
import com.cqupt.readingcloud.book.domain.BookPreviousAndNextChapterNode;
import com.cqupt.readingcloud.book.service.BookChapterService;
import com.cqupt.readingcloud.book.service.BookService;
import com.cqupt.readingcloud.book.vo.BookChapterListVO;
import com.cqupt.readingcloud.book.vo.BookChapterReadVO;
import com.cqupt.readingcloud.book.vo.BookChapterVO;
import com.cqupt.readingcloud.common.cache.RedisBookKey;
import com.cqupt.readingcloud.common.cache.RedisExpire;
import com.cqupt.readingcloud.common.cache.RedisService;
import com.cqupt.readingcloud.common.pojo.book.Book;
import com.cqupt.readingcloud.common.pojo.book.BookChapter;
import com.cqupt.readingcloud.common.result.Result;
import com.cqupt.readingcloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookChapterServiceImpl implements BookChapterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookChapterServiceImpl.class);

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisService redisService;


    /**
     * 书本所有章节查询模块
     * @param bookId
     * @return
     *  * 通过图书ID获取章节列表：
     *       流程：根据Id查询图书信息：
     *       将图书Id转换为redis中存储的标准key->查询redis
     *       ->返回不为null表示redis中有，否则查询数据库并写入redis
     */
    @Override
    public Result getBookChapterListByBookId(String bookId) {
        //查出图书详情信息
        Book book = (Book) bookService.getBookById(bookId).getData();
        //
        if(book == null) return ResultUtil.notFound().buildMessage("该书本不存在");

        //将BookId转化为Redis储存的标准key
        String key = RedisBookKey.getBookChapterKey(bookId);
        //查询redis
        List<BookChapterListVO> chapterVOs = this.redisService.getCacheForList(key, BookChapter.class);
        //如果redis中没有，查询mysql数据库，并上传redis
        if(chapterVOs == null || chapterVOs.size() == 0){
            List<BookChapter> chapters = this.bookChapterMapper.findPageWithResult(book.getId());
            if(chapters.size() > 0){
                chapterVOs = new ArrayList<>();
                for(int i=0; i<chapters.size(); i++){
                    BookChapterListVO vo = new BookChapterListVO();
                    BeanUtils.copyProperties(chapters.get(i), vo);
                    chapterVOs.add(vo);
                }
                //将数据库查到的写回redis
                this.redisService.setExpireCache(key, chapterVOs, RedisExpire.HOUR);
            }
        }
        return ResultUtil.success(chapterVOs);
    }

    /**
     * 书本某章节查询模块
     * @param bookId
     * @param chapterId
     * @return
     * 与之前类似，查缓存，查到就返回，否则查数据库推缓存
     */
    @Override
    public Result<BookChapter> getChapterById(String bookId, Integer chapterId) {
        BookChapter chapter;
        String key = RedisBookKey.getBookChapterKey(bookId);
        String field = chapterId.toString();
        chapter = this.redisService.getHashVal(key, field, BookChapter.class);
        if(chapter == null){
            chapter = this.bookChapterMapper.selectById(chapterId);
            if(chapter != null){
                this.redisService.setHashValExpire(key, field, chapter, RedisExpire.HOUR);
            }
        }
        return ResultUtil.success(chapter);
    }

    /**
     * 实际章节内容查询模块
     * @param bookId
     * @param chapterId
     * @return
     */
    @Override
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {
        Book book = (Book) this.bookService.getBookById(bookId).getData();
        if(book == null) return ResultUtil.notFound().buildMessage("该书本不存在");

        BookChapterReadVO bookChapterReadVO = new BookChapterReadVO();
        String field = chapterId.toString();

        if(chapterId == 0) field = "first";
        else if(chapterId == -1) field = "last";

        BookPreviousAndNextChapterNode chapterNode = this.getChapterNodeData(book.getId(), field);

        return null;
    }

    private BookPreviousAndNextChapterNode getChapterNodeData(final Integer bookId, final String field){
        return null;
    }
}
