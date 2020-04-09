package jodd.tutorial.service;

import static jodd.db.oom.DbOomQuery.query;
import static jodd.db.oom.sqlgen.DbSqlBuilder.sql;

import java.util.List;
import jodd.db.oom.DbOomQuery;
import jodd.db.oom.sqlgen.DbSqlBuilder;
import jodd.joy.db.AppDao;
import jodd.jtx.meta.ReadOnlyTransaction;
import jodd.jtx.meta.ReadWriteTransaction;
import jodd.petite.meta.PetiteBean;
import jodd.petite.meta.PetiteInject;
import jodd.tutorial.model.Message;
import jodd.tutorial.model.Response;

@PetiteBean
public class AppService {

  @PetiteInject AppDao appDao;

  @ReadOnlyTransaction
  public List<Message> findLastMessages(int count) {
    DbSqlBuilder dbsql =
        sql("select $C{m.*} from $T{Message m} order by $m.messageId desc limit :count");

    DbOomQuery dbquery = query(dbsql);
    dbquery.setInteger("count", count);

    return dbquery.list(Message.class);
  }

  @ReadOnlyTransaction
  public List<Message> findLastMessagesWithResponses(int count) {
    DbSqlBuilder dbsql =
        sql(
            "select $C{m.*}, $C{m.responses:r.*} "
                + "from $T{Message m} "
                + "left join $T{Response r} using ($.messageId) "
                + "order by $m.messageId desc limit :count");

    DbOomQuery dbquery = query(dbsql);
    dbquery.entityAwareMode(true);
    dbquery.setInteger("count", count);

    return dbquery.list(Message.class, Response.class);
  }

  @ReadWriteTransaction
  public void addMessage(Message message) {
    appDao.store(message);
  }

  @ReadOnlyTransaction
  public Message findMessageById(long messageId) {
    return appDao.findById(Message.class, messageId);

    //        return DbEntitySql.findById(Message.class, messageId)
    //                .query()
    //                .autoClose()
    //                .find();
  }
}
