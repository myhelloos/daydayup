package jodd.tutorial.view;

import java.util.List;
import jodd.madvoc.meta.Action;
import jodd.madvoc.meta.MadvocAction;
import jodd.madvoc.meta.Out;
import jodd.petite.meta.PetiteInject;
import jodd.tutorial.model.Message;
import jodd.tutorial.service.AppService;

@MadvocAction
public class IndexAction {

  @PetiteInject AppService appService;

  @Out List<Message> messages;

  @Action(alias = "index")
  public void view() {
    messages = appService.findLastMessagesWithResponses(10);
  }
}
