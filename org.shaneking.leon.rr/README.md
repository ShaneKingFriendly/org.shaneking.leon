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
  - c5c: XxxBean
  - x5x: XxxEntity
- prog
  - e5n(Access Layer): XxxRpc, XxxSocket, XxxServlet
  - g5n(Control Layer): XxxController
  - i5n(Request And Response): XxxRr
  - l5n(Transactional Layer): XxxTransactional
  - n5n(Business Layer): XxxService
  - p5n(Remote Call): XxxRc
  - t5n(Database Layer): XxxRepository
