# id

#### 介绍
分布式Id

#### JS版本示例

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
> 依赖模块：id.min.js

#### C#版本示例

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
> 依赖模块：id.cs

#### Sql Server版本示例

```
SELECT dbo.Id_GenerateChar(RAND())
```
> 依赖模块：id.ms.sql
> 由于自定义函数内不允许生成随机数（RAND），需要在调用方通过参数传入

