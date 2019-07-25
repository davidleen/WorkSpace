package com.rnmap_wb.android.dao;

import com.rnmap_wb.android.entity.DownloadTask;
import com.rnmap_wb.android.entity.MessageState;
import com.rnmap_wb.android.entity.MessageState;
import com.rnmap_wb.android.entity.MessageStateDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class IMessageStateDao {

    private MessageStateDao dao;

    public IMessageStateDao(MessageStateDao dao) {

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        this.dao = dao;
    }


    public List<MessageState> findAll() {

        return dao.loadAll();

    }


    public Long insert(MessageState taskMessage) {

        return dao.insert(taskMessage);
    }



    public void save(MessageState messageState) {

        dao.save(messageState);
    }

    public MessageState load(long taskId) {
        return dao.load(taskId);
    }

    public void delete(MessageState task) {
        dao.delete(task);
    }

    public MessageState findByMessageId(String messageId) {



        QueryBuilder<MessageState> builder=dao.queryBuilder();
        builder.where(MessageStateDao.Properties.MessageId.eq(messageId));

        List<MessageState> list = builder.list();
        if(list==null||list.size()==0) return null;
        return list.get(0);


    }
}
