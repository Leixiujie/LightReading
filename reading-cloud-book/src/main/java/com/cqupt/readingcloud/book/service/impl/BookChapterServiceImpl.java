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
import java.util.HashMap;
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
            List<BookChapter> chapters = this.bookChapterMapper.findPageWithResult(Integer.parseInt(bookId));
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
            if(chapter != null) this.redisService.setHashValExpire(key, field, chapter, RedisExpire.HOUR);
            else this.redisService.setHashValExpire(key, field, chapter, 10L);
        }

        if(chapter != null) return ResultUtil.success(chapter);
        else return ResultUtil.notFound(chapter);
    }

    /**
     * 实际章节内容查询模块
     * @param bookId
     * @param chapterId
     * @return
     * 根据bookId获取书本章节信息，如果没有，则返回“没有内容”，有则继续
     * 根据章节信息查询章节内容,并返回上下章节的信息(不包含内容)
     *
     */
    @Override
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {
//        System.out.println(bookId);
        Book book = (Book) this.bookService.getBookById(bookId).getData();
//        System.out.println(book);
        if(book == null) return ResultUtil.notFound().buildMessage("该书本不存在");

        String field = chapterId.toString();

        if(chapterId == 0) field = "first";
        else if(chapterId == -1) field = "last";

        BookPreviousAndNextChapterNode chapterNode = this.getChapterNodeData(bookId, field);

        if(chapterNode == null){
            //找不到的查首章节
            field = "first";
            chapterNode = this.getChapterNodeData(bookId, field);
//            System.out.println(chapterNode);
            if(chapterNode == null) return ResultUtil.notFound().buildMessage("本书还没有任何章节内容");
        }

        String content = this.getChapterContent(bookId, chapterNode.getId());
        BookChapterVO current = new BookChapterVO(chapterNode.getId(), chapterNode.getName(), content);

        //上一章、下一章
        BookChapterVO pre = null;
        BookChapterVO next = null;
        if(chapterNode.getPre() != null)
            pre = new BookChapterVO(chapterNode.getPre().getId(), chapterNode.getPre().getName(), "");

        if(chapterNode.getNext() != null)
            next = new BookChapterVO(chapterNode.getNext().getId(), chapterNode.getNext().getName(), "");

//        System.out.println(current);
        BookChapterReadVO result = new BookChapterReadVO();
        result.setCurrent(current);
        result.setPre(pre);
        result.setNext(next);

        return ResultUtil.success(result);

    }

    /**
     * 获取前后章节节点数据链表
     * @param bookId
     * @param field
     * @return
     * 先通过redis查询章节内容是否存在，查到就返回，
     * 没有查到就去数据库查找，
     * 数据库查找到了就生成一个章节链表并存入redis，并返回数据
     * 数据库没有查到就返回空
     */
    private BookPreviousAndNextChapterNode getChapterNodeData(final String bookId, final String field){
        //查询book前后章节节点
        String key = RedisBookKey.getBookChapterNodeKey(Integer.parseInt(bookId));
        BookPreviousAndNextChapterNode chapterNode = this.redisService.getHashObject(key, field, BookPreviousAndNextChapterNode.class);

        //如果查到就返回
        if(chapterNode != null) return chapterNode;

        //否则就去数据库查
        List<BookChapter> chaptersList = this.bookChapterMapper.findPageWithResult(Integer.parseInt(bookId));

//        LOGGER.debug(chaptersList.toString());

        //为空就表示没了
        if(chaptersList.size() == 0) return null;

        HashMap<String, BookPreviousAndNextChapterNode> map = new HashMap<>();

        //获取上一个节点数据
        BookPreviousAndNextChapterNode pre = null;
        try{
            for(int i=1; i<=chaptersList.size(); i++){
                BookChapter chapter = chaptersList.get(i-1);
                //获取下一章节内容
                if(chapter.getLockStatus()){
                    if(i >= chaptersList.size()) break;
                    chapter = chaptersList.get(i);
                    ++i;
                }

                //获取当前章节节点数据
                BookPreviousAndNextChapterNode curr =
                        new BookPreviousAndNextChapterNode(chapter.getId(), chapter.getName());
                if(pre != null){
                    curr.setPre(new BookPreviousAndNextChapterNode(pre));
                    pre.setNext(new BookPreviousAndNextChapterNode(curr));
                    map.put(pre.getId()+"", pre);
                }

                if(i == 2) map.put("first", pre);
                //排除只有1个章节的错误情况；同时，如果直接设置1没有后继节点
                else if(i == 1) map.put("first", curr);

                //设置中间章节信息
                map.put(curr.getId()+"", curr);
                pre = curr;
            }
            map.put("last", pre);
            this.redisService.setHashValsExpire(key, map, RedisExpire.HOUR);
        }catch (Exception e){
            LOGGER.error("生成章节节点数据异常：{}", e);
        }
        return map.get(field);
    }

    /**
     * 内容查询模块
     * @param bookId
     * @param chapterId
     * @return
     * 根据章节和bookId来查询内容
     */
    private String getChapterContent(String bookId, Integer chapterId){
        String content = "";
        BookChapter chapter = this.getChapterById(bookId, chapterId).getData();
        if(chapter != null) content = chapter.getContent();

        return content;
    }
}
