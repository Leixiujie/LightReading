package com.cqupt.readingcloud.account.service.task;


import com.cqupt.readingcloud.common.pojo.account.UserBookshelf;
import com.cqupt.readingcloud.account.dao.UserBookshelfMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBookshelfTask implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookshelfTask.class);

    //处理任务类型：1：新增，2：修改，3：删除
    private Integer syncType;
    private UserBookshelf bookshelf;
    private UserBookshelfMapper userBookshelfMapper;
    private Integer userId;


    @Override
    public void run() {
        try{
            if(syncType == 1){
                this.bookshelf.setUserId(this.userId);
                this.userBookshelfMapper.insert(this.bookshelf);
            }
            else if(syncType == 2){
                this.bookshelf.setUserId(this.userId);
                this.userBookshelfMapper.updateByUserIdAndBookId(this.bookshelf);
            }
            else if(syncType == 3){
                this.userBookshelfMapper.deleteById(this.bookshelf.getId());
            }
        }
        catch (Exception e){
            LOGGER.error("书架同步失败,同步类型[{}], 异常：{}", this.syncType, e);
        }
    }

    public UserBookshelfTask(){}

    public UserBookshelfTask(Integer syncType, UserBookshelf bookshelf, UserBookshelfMapper bookshelfMapper, Integer userId){
        this.syncType = syncType;
        this.bookshelf = bookshelf;
        this.userBookshelfMapper = bookshelfMapper;
        this.userId = userId;
    }
}
