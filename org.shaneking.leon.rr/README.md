[//]:(https://github.com/ShaneKing/spec.sk/blob/0ae1f0420ef89a04a6a65de1858a0d38b85819c6/Java.md)
# Package
## Process Flow Layer
the layers like car, A classes, B classes, C classes, A0 classes, A1 classes, A00 classes(WuLingHongGuang MINI EV)
### Analysis
- ABCDEFG HIJKLMN OPQRST UVWXYZ
  - access layer(@Controller), process layer(@Service), storage layer(@Repository)
- 123456789
  - 3 Helper(@Component), 7 Utils(Statics)
### Design
- stub: open for caller
  - c5c5bean: XxxBean
  - x5x5entity: XxxEntity
- prog
  - BEGIN
    - e5i5filter
  - e5n5rpc(Access Layer): XxxRpc, XxxSocket, XxxServlet
    - g5e5interceptor
    - g5i5advice
  - g5n5controller(Control Layer): XxxController
  - i5n5rr(Request And Response): XxxRr
  - l5n5transactional(Transactional Layer): XxxTransactional
  - n5n5service(Business Layer): XxxService
  - p5n5rc(Remote Call): XxxRc
  - t5n5repository(Database Layer): XxxRepository
  - END
