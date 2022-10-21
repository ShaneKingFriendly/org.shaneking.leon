# org.shaneking.leon

ShaneKing Spring Web Library

## @

### @ConditionalOnProperty

```yaml
sk:
  leon:
    rr:
      req:
        ips:
          enabled: true
        url:
          enabled: true
      controller:
        advice:
          enabled: true
      audit:
        enabled: true
```

### @ConfigurationProperties

```yaml
sk:
  leon:
    swagger:
      ui:
        enabled: false
        base-pkg: org.shaneking
        path-reg: /api/\w*/open/\S*
        title: ShaneKing
        version: 1.0
```

### @Value ${

```yaml
sk:
  leon:
    persistence:
      file:
        csv:
          buffer: 1023
        temporary:
          folder: /tmp
      dbserver:
        backup:
          folder: /tmp
    rr:
      req:
        ips:
          enabled: true
        url:
          enabled: true
      ctl:
        cmd:
          prefix: /leon/rr/cmd
        file:
          prefix: /leon/rr/file
        ht2p:
          prefix: /leon/rr/ht2p
        state:
          prefix: /leon/rr/state
      file:
        temporary:
          folder: /tmp
        attach:
          folder: /tmp
        upload:
          folder: /tmp
      audit:
        enabled: true
```
