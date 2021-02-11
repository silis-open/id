#### 介绍
分布式Id，指在不同地方生成的Id基本能保证唯一，这样就不需要集中在一个服务器上管理Id的生成。<br>
> 1个字节 = 8个bit<br>
> 1个字符 = 4个bit<br>
> 1个Id = 12个字节 = 24个字符 = char(24)

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

#### C#版本示例
生成字符串类型的id

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

---

new一个带实体泛型的id对象

```
using App;
using System;

namespace App.Test
{
    class Program
    {
        static void Main(string[] args)
        {
            var id = new Id<MyEntity>;
            Console.WriteLine(id.toString());
        }
    }
}

```

#### Sql Server版本示例

```
SELECT dbo.ID_GENERATECHAR(RAND())
```
> 由于自定义函数内不允许生成随机数（RAND），需要在调用方通过参数传入

#### 关于id对象带泛型的好处：

- 理解id属于哪个实体的id有利于代码更容易阅读，即提高代码可读性。
- 使得在编写代码时，减少出现Id赋值错误的可能性，即降低写错代码概率。

#### 关于id对象的实现：

- 只在支持泛型的语言下可以使用id对象；
- 不支持泛型的语言就不能使用id对象，只能使用字符串形式。

> 这是因为，假如未来原来不支持泛型的语言，变成支持泛型了，想要一种更完美的应对未来可能性的策略。