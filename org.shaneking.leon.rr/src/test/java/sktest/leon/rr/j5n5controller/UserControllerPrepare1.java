package sktest.leon.rr.j5n5controller;

import org.shaneking.ling.rr.Req;
import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;
import org.shaneking.roc.rr.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserControllerPrepare1 {

  @Autowired
  private CacheableDao cacheableDao;

  @RequestMapping("/add")
  @ResponseBody
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<SimpleUserEntity>> add(@RequestBody Req<SimpleUserEntity> req) {
    Resp<Integer, Req<SimpleUserEntity>> resp = Resp.success(req, null);
    resp.srtMsgBodyData(cacheableDao.add(SimpleUserEntity.class, CacheableDao.pti(req.gnaMsgBdyObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/lst")
  @ResponseBody
  @RrLimiting(prop = "sktest.leon.rr.j5n5controller.UserControllerPrepare1.lst", limit = 1)
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  @RrCache
  public Resp<List<SimpleUserEntity>, Req<SimpleUserEntity>> lst(@RequestBody Req<SimpleUserEntity> req) {
    Resp<List<SimpleUserEntity>, Req<SimpleUserEntity>> resp = Resp.success(req, null);
    resp.srtMsgBodyData(cacheableDao.lst(SimpleUserEntity.class, CacheableDao.pts(req.gnaMsgBdyObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/modByIdVer")
  @ResponseBody
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<SimpleUserEntity>> modByIdVer(@RequestBody Req<SimpleUserEntity> req) {
    Resp<Integer, Req<SimpleUserEntity>> resp = Resp.success(req, null);
    resp.srtMsgBodyData(cacheableDao.modByIdVer(SimpleUserEntity.class, CacheableDao.ptu(req.gnaMsgBdyObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/rmvById")
  @ResponseBody
  @RrAudit
  @RrChannel
  @RrCrypto
  @RrTenant
  @RrUser
  public Resp<Integer, Req<String>> rmvById(@RequestBody Req<String> req) {
    Resp<Integer, Req<String>> resp = Resp.success(req, null);
    SimpleUserEntity userEntity = new SimpleUserEntity();
    userEntity.setId(req.gnaMsgBdyObj());
    resp.srtMsgBodyData(cacheableDao.rmvById(SimpleUserEntity.class, CacheableDao.ptu(userEntity, req.gnnCtx().gnaTenantId())));
    return resp;
  }
}
