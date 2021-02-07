(function(win){

    if(win.top.Id) return win.top.Id;
	
	var increaseNum; //自增数
		random = Math.random(); //初始化的随机数

	win.Id = function(){};

	win.Id.generateString = function(){
		//计算Id的时间部分，占4字节，即32位，即8个16进行字符。
		var timePart = (Array(8).join(0) + parseInt(Date.now() / 1000).toString(16)).slice(-8);

		//叠加式生成随机数，提高随机性
		random =  random + Math.random();
		if(random >= 1) random -= 1;

		//计算Id的自增部分，占3字节，即24位，即6个16进行字符。
		increaseNum = (increaseNum || parseInt(random*10000000)) + 1;
		if(increaseNum >= 16777216) increaseNum = 0;
		var increasePart = (Array(6).join(0) + increaseNum).toString(16).slice(-6);

		//叠加式生成随机数，提高随机性
		random =  random + Math.random();
		if(random >= 1) random -= 1;

		//计算Id的环境部分，占5字节，即40位，即10个16进行字符。
		var envPart = (Array(10).join(0) + parseInt(random * 1000000000000)).toString(16).slice(-10);

		return timePart + envPart  + increasePart;
	}

})(window);