# id

#### 介绍
分布式Id

#### 分布式id的JS版本示例
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

#### 分布式id的C#版本示例

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
