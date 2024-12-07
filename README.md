# ntp server http proxy
just a simple ntp proxy

sample config in `config.sample.json`

### Demo
Demo is available at [ntpproxy.sculk.ltd](https://ntpproxy.sculk.ltd). \
Also this proxy is used on [ntp.sculk.ltd](https://ntp.sculk.ltd) website where you
can check difference your computer's time and server time.

Servers available on demo can be retrieved using this endpoint: \
[https://ntpproxy.sculk.ltd/servers](https://ntpproxy.sculk.ltd/servers)

### API
#### `GET /servers`
Get list of servers, read from `config.json`

Sample answer:
```json
{
  "servers": [
    {
      "address": "ru-01.ntp.sculk.ltd",
      "text": "Russia, Moscow - Stratum 2",
      "stratum": 2
    },
    {
      "address": "ntp1.vniiftri.ru",
      "text": "Russia, Moscow - Stratum 1",
      "stratum": 1
    }
  ]
}
```

#### `GET /current/:server`
Replace `:server` with `address` field from `config.json`
or `/servers` endpoint. For example: `/current/ru-01.ntp.sculk.ltd`.

Sample **success** answer:
```json
{
  "adjusted_time": 1733534988647,
  "ntp_time": 1733534988647,
  "jitter": 134,
  "address": "212.113.99.6",
  "offset": 100,
  "unix_time_millis": 1733534988647,
  "host_name": "ru-01.ntp.sculk.ltd",
  "stratum": 2
}
```
**Where:**
* `adjusted_time` and `unix_time_millis` (they are the same) - current time in Unix milliseconds format that includes offset
* `ntp_time` - current time in Unix milliseconds format received from the NTP server
* `jitter` - time in milliseconds between the time when proxy received request and when NTP answered
* `address` - IPv4 / IPv6 address resolved from hostname
* `offset` - time in milliseconds between the time when proxy received request and when NTP received request
* `host_name` - requested hostname of NTP server
* `stratum` - stratum received from NTP server

Sample **error** answer:
```json
{
  "error": "No server with identifier nonexistent.ntp.sculk.ltd found"
}
```
There are always only one field `error` with description. \
Currently error messages are returned with `200` http code. That's a known bug.

## Copyright
&copy; 2024 [edwardcode](https://edwardcode.net) \
Hosted by Sculk Ltd.