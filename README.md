# Kurenai Bot

[go-cqhttp](https://github.com/Mrs4s/go-cqhttp) sdk，目前只实现了自己想用的接口（只有几个）。

## Usage

暂时不知道java调用的兼容性，下面是kotlin调用

```kotlin
val bot = DefaultCQBot(your_api_host, your_api_ws_port)
bot.addHandle(GroupMessageEvent::class) { event ->
    if (event.groupId == targetId) doSomeThing()
    else bot.send("Not match.".asMessage().privateMsg(your_qq_id))
}
```