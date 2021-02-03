package sktest.leon.rr.ctl;

import org.shaneking.ling.rr.Resp;
import org.shaneking.roc.persistence.dao.TenantedCacheableDao;
import org.shaneking.roc.persistence.hello.entity.HelloUserEntity;
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
public class UserController {

  @Autowired
  private TenantedCacheableDao cacheableDao;

  @RequestMapping("/add")
  @ResponseBody
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<HelloUserEntity, Integer>> add(@RequestBody Req<HelloUserEntity, Integer> req) {
    Resp<Req<HelloUserEntity, Integer>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.add(HelloUserEntity.class, req.getPri().getObj(), req.gnnCtx().getTenant().getId()));
    return resp;
  }

  @RequestMapping("/delById")
  @ResponseBody
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<String, Integer>> delById(@RequestBody Req<String, Integer> req) {
    Resp<Req<String, Integer>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.delById(HelloUserEntity.class, req.getPri().getObj(), req.gnnCtx().getTenant().getId()));
    return resp;
  }

  @RequestMapping("/modByIdVer")
  @ResponseBody
  @RrAudit
  @RrAccess
  @RrCrypto
  public Resp<Req<HelloUserEntity, Integer>> modByIdVer(@RequestBody Req<HelloUserEntity, Integer> req) {
    Resp<Req<HelloUserEntity, Integer>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.modByIdVer(HelloUserEntity.class, req.getPri().getObj(), req.gnnCtx().getTenant().getId()));
    return resp;
  }

  @RequestMapping("/lst")
  @ResponseBody
  @RrLimiting(prop = "sktest.leon.rr.ctl.UserController.lst", limit = 1)
  @RrAudit
  @RrAccess
  @RrCache
  @RrCrypto
  public Resp<Req<HelloUserEntity, List<HelloUserEntity>>> lst(@RequestBody Req<HelloUserEntity, List<HelloUserEntity>> req) {
    Resp<Req<HelloUserEntity, List<HelloUserEntity>>> resp = Resp.success(req);
    req.getPri().setRtn(cacheableDao.lst(HelloUserEntity.class, req.getPri().getObj(), req.gnnCtx().getTenant().getId()));
    return resp;
  }
}
