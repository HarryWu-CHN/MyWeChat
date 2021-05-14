# 创建项目啦！！

# API接口设置
api端口为8.140.133.34:7264

## 用户登录
/user/login
```Json
{
    "username":,
    "password":
}
result:{
    "success":
}
```
成功以后维护一个websocket

/user/logon
```Json
{
    "username":,
    "password":
}
result:{
    "success":
}
```

## 加好友
/contact/find
```Json
{
    "find":, // 想要查询的用户名字符串
}
```

/contact/add
```Json
{
    "sendTo":,
}
result:{
    "success":
}
```

/contact/agree
```Json
{
    "sendTo":,
    "agree":,
}
result:{
    "success":
}
```
## 发消息、朋友圈、加载朋友圈
/chat/send
```Json
{
    "sendTo":, // 接收者名称
    "msgType":, 
    // 0,1,2,3,4
    // 文本、图片、视频、地点
    "msg":, // 不同形式再定义
}
```

/discover/post
```Json
{
    // 发朋友圈
    "msgType":, 
    // 0,1,2,3
    // 文本、图片、视频
    "msg":, // 不同形式再定义
}
```

/discover
```Json
{
    // 获取朋友圈
    "msgType":, 
    // 0,1,2
    // 文本、图片、视频
    "msg":, // 不同形式再定义
}
```

