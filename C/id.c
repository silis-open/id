#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

unsigned int Id_increaseNum; //自增数
unsigned int Id_random; //初始化的随机数
pthread_mutex_t Id_increaseNumLock; // 为访问counter而设立的互斥

char* Id_generateString()
{
	time_t timep = time(NULL);
	
	printf("%d\n", (int)(unsigned char)(timep >> 24));
	printf("%d\n", (int)(unsigned char)(timep >> 16));
	printf("%d\n", (int)(unsigned char)(timep >> 8));
	printf("%d\n", (int)(unsigned char)(timep));
	
	//计算Id的自增部分，占3字节，即24位，即6个16进行字符。
	srand((unsigned)time(NULL)); //随机数播种
	if(Id_increaseNum == 0){
	    pthread_mutex_lock(&Id_increaseNumLock); //上锁
	    Id_increaseNum = rand();
	    pthread_mutex_unlock(&Id_increaseNumLock); //解锁
	}
	
    pthread_mutex_lock(&Id_increaseNumLock); //上锁
	++Id_increaseNum;
	pthread_mutex_unlock(&Id_increaseNumLock); //解锁
	
	if(Id_increaseNum >= RAND_MAX){
	    pthread_mutex_lock(&Id_increaseNumLock); //上锁
	    Id_increaseNum = 1;
	    pthread_mutex_unlock(&Id_increaseNumLock); //解锁
	}
	
	
	//叠加式生成随机数，提高随机性
	Id_random = Id_random + rand();
	
	
	printf("%ld\n", rand());
	
	printf("%ld", timep);
	
	return "hello";
}

int main()
{
   /* 我的第一个 C 程序 */
   printf("%s", Id_generateString());
   
   return 0;
}