package sktest.leon.persistence.ctl;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.shaneking.leon.test.SKSpringMvcUnit;
import org.shaneking.ling.jackson.databind.OM3;
import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.ReqMsg;
import org.shaneking.ling.rr.ReqMsgBdy;
import org.shaneking.ling.zero.crypto.SKC1;
import org.shaneking.ling.zero.util.UUID0;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserEntityControllerTest extends SKSpringMvcUnit {

  @Autowired
  @Getter
  public MockMvc mockMvc;

  @Test
  void curd() throws Exception {
    setTstSeq("add");
    assertNotNull(performJT("/user/add"));
    setTstSeq("mge");
    assertNotNull(performJT("/user/mge"));
    setTstSeq("modByIdVer");
    assertNotNull(performJT("/user/mod"));
    setTstSeq("mge");
    assertNotNull(performJT("/user/mge"));

    setTstSeq("rmv");
    Req req = Req.build().setCno("tstChannelNo").setMsg(ReqMsg.build().setRno(UUID0.cUl33()).setUno("tstUserNo").setBdy(ReqMsgBdy.build().setTno("tstTenantNo").setObj(new SimpleUserEntity().setId("1612353237501_DcNd45KtJXPmSpz2xRB"))));
    req.setEnc(SKC1.encrypt(OM3.writeValueAsString(req.getMsg()))).setMsg(null);
    ResultActions resultActions = mockMvc.perform(appJson(cookie(post("/user/rmv").content(OM3.writeValueAsString(req)))));
    resultActions.andExpect(status().isOk()).andDo(print());
  }
}
