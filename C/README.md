
### 示例
```
#include "id.c"

int main()
{
   char* id = Id_generateString();
   printf("%s", id);
   free(id); //需要释放内存
   
   return 0;
}
```

> 注意C语言版本，需要在调用完后主动释放id的内存