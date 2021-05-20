[//]:(https://github.com/ShaneKing/spec.sk/blob/5532d608ac34065651ac4d7eb58b366bb0a107ae/Java.md)

# Package

## Process Flow Layer

the layers like car, A classes, B classes, C classes, A0 classes, A1 classes, A00 classes(WuLingHongGuang MINI EV)

### Analysis

- AbCdE`f`GhI`j`KlM`n`OpQ`r`StU`v`WxYz
  - @Controller -> @Service -> @Repository
  - b -> d -> f -> ... -> v -> x -> z
- 123456789
  - 2 Helper(@Component), 8 Utils(Statics)

### Design

- stub: open for caller
  - q5q5bean: XxxBean
  - u5u5entity: XxxEntity
- prog
  - BEGIN
    - f5j5filter
  - f5n5rpc(Access Layer): XxxRpc, XxxSocket, XxxServlet
    - j5f5interceptor
    - j5j5advice
  - j5n5controller(Control Layer): XxxController
    - j5r5rr(Request And Response): XxxRr
    - n5j5transactional(Transactional Layer): XxxTransactional
  - n5n5service(Business Layer): XxxService
    - n5r5rc(Remote Call): XxxRc
  - r5n5repository(Database Layer): XxxRepository
  - END
