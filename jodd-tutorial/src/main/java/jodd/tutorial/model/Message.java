package jodd.tutorial.model;

import java.util.List;
import jodd.db.oom.meta.DbColumn;
import jodd.db.oom.meta.DbId;
import jodd.db.oom.meta.DbTable;

@DbTable
public class Message {

  @DbId private long messageId;

  @DbColumn private String text;

  private List<Response> responses;

  // ...getters and setters...

  public long getMessageId() {
    return messageId;
  }

  public void setMessageId(long messageId) {
    this.messageId = messageId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<Response> getResponses() {
    return responses;
  }
}
