[//]:(https://github.com/ShaneKing/spec.sk/blob/dd795a28258f9fa1532f79789e8eef7b984f8568/Java.md)

# Package

## Process Flow Layer

the layers like car, A classes, B classes, C classes, A0 classes, A1 classes, A00 classes(WuLingHongGuang MINI EV)

### Process Flow

- AbCdE`f`GhI`j`KlM`n`OpQ`r`StU`v`WxYz
  - @Controller -> @Service -> @Repository
  - process: b -> d -> f -> ... -> v -> x -> z
  - non-process: A -> C -> E -> ... -> U -> W -> Y
- 123456789
  - 6 Helper(@Component), 8 Utils(Statics)

### Layer

- bean: used for caller. XxxBean, XxxHt2pBean, XxxCloudBean, XxxDubboBean
- api(ht2p/cloud/dubbo): call for caller
- application
  - BEGIN
    - f5j5filter
  - f5n5xxx(Access Layer): XxxServlet, XxxSocket, XxxDubbo
    - **g5**.cfg: XxxCfg
    - j5f5interceptor
    - j5j5advice
  - j5n5controller(Control Layer): XxxController
    - j5r5rr(Request And Response): XxxRr
    - n5j5transactional(Transactional Layer): XxxTransactional
  - n5n5service(Business Layer): XxxService(Biz)
    - n5r5rc(Remote Call): XxxRc
  - r5n5repository(Database Layer): XxxRepository(Dao)
    - **u5**.entity: XxxEntity
  - END
