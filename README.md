# id

#### 介绍
分布式Id

#### JS
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

#### C#

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
