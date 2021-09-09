package sktest.leon.rr.j5n5controller;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.dao.CacheableDao;
import org.shaneking.roc.persistence.simple.SimpleUserEntity;
import org.shaneking.roc.rr.Req;
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
  @RrAccess
  @RrCrypto
  public Resp<Req<SimpleUserEntity, Integer>> add(@RequestBody Req<SimpleUserEntity, Integer> req) {
    Resp<Req<SimpleUserEntity, Integer>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.add(SimpleUserEntity.class, CacheableDao.pti(req.getPri().getObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/lst")
  @ResponseBody
  @RrLimiting(prop = "sktest.leon.rr.j5n5controller.UserControllerPrepare1.lst", limit = 1)
  @RrAudit
  @RrAccess
  @RrCache
  @RrCrypto
  public Resp<Req<SimpleUserEntity, List<SimpleUserEntity>>> lst(@RequestBody Req<SimpleUserEntity, List<SimpleUserEntity>> req) {
    Resp<Req<SimpleUserEntity, List<SimpleUserEntity>>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.lst(SimpleUserEntity.class, CacheableDao.pts(req.getPri().getObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/modByIdVer")
  @ResponseBody
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<SimpleUserEntity, Integer>> modByIdVer(@RequestBody Req<SimpleUserEntity, Integer> req) {
    Resp<Req<SimpleUserEntity, Integer>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.modByIdVer(SimpleUserEntity.class, CacheableDao.ptu(req.getPri().getObj(), req.gnnCtx().gnaTenantId())));
    return resp;
  }

  @RequestMapping("/rmvById")
  @ResponseBody
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<String, Integer>> rmvById(@RequestBody Req<String, Integer> req) {
    Resp<Req<String, Integer>> resp = Resp.success(req);
    SimpleUserEntity userEntity = new SimpleUserEntity();
    userEntity.setId(req.getPri().getObj());
    req.getPri().setRtn(cacheableDao.rmvById(SimpleUserEntity.class, CacheableDao.ptu(userEntity, req.gnnCtx().gnaTenantId())));
    return resp;
  }
}
