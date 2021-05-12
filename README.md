# 创建项目啦！！

# API接口设置
api端口为8.140.133.34:7264

## 用户登录
/user/login
```Json
{
    username:,
    password:
}
result:{
    success:
}
```
成功以后维护一个websocket

/user/logon
```Json
{
    username:,
    password:
}
result:{
    success:
}
```

## 加好友
/user/addContact
```Json
{
    username:,
    sendTo:,
}
result:{
    success:
}
```

/user/agreeContact
```Json
{
    sendTo:,
    agree:,
}
result:{
    success:
}
```
## 发消息、朋友圈、加载朋友圈
/user/send
```Json
{
    sendTo:,
    msgType:, 
    // 0,1,2,3,4
    // 文本、图片、视频、地点
    msg:, // 不同形式再定义
}
```

/user/post
```Json
{
    // 发朋友圈
    msgType:, 
    // 0,1,2,3
    // 文本、图片、视频
    msg:, // 不同形式再定义
}
```

/user/getDiscover
```Json
{
    // 获取朋友圈
    msgType:, 
    // 0,1,2
    // 文本、图片、视频
    msg:, // 不同形式再定义
}
```

