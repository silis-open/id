# id

#### 介绍
分布式Id，指在不同地方生成的Id基本能保证唯一，这样就不需要在集中在一个服务器上管理Id的生成。<br>
> 1个Id = 12个字节<br>
> 1个字节 = 8个bit<br>
> 1个字符 = 4个bit<br>
> 1个Id = 24个字符

#### JS版本示例

> 依赖模块：id.min.js

```
<html>
    <head>
        <script src="id.min.js"></script>
    </head>
    <body>
        <script>
            var id = Id.GenerateString();
            document.write(id);
        </script>
    </body>
</html>
```

#### C#版本示例

> 依赖模块：id.cs

```
using App;
using System;

namespace App.Test
{
    class Program
    {
        static void Main(string[] args)
        {
            var id = Id.GenerateString();
            Console.WriteLine(id);
        }
    }
}

```

#### Sql Server版本示例

> 依赖模块：id.ms.sql<br>
> 由于自定义函数内不允许生成随机数（RAND），需要在调用方通过参数传入

```
SELECT dbo.Id_GenerateChar(RAND())
```

