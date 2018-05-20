const xk="今天的课就到这里，你可以修改代码来练习，下课！";
a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setContent('函数(Function)，就是使得一段代码可以重复使用\n来看一个例子，我们想在屏幕上输出"你好，Js！"，"你好，VIDE！","你好，世界！"三句话，那么你应该想到是这样写');
a.setCode('vout.println("你好，Js！");\nvout.println("你好，VIDE！");\nvout.println("你好，世界！");');
a.setButtonText("运行");
var s=0,finished=false;
function finish(r) {
  finished=true;
  a.setEditable(true);
  a.setButtonText("运行");
  if (r) a.setContent(xk);
}
function onActivityFinish() {
  if (finished) return;
  onClick();
}
function onClick(v) {
switch (s) {
case 0:a.run();break;
case 1:a.setContent("我们发现，这三句代码都是差不多的结构：vout.println(\"你好，xxx！\");\n这时候函数就登场了！（论程序员偷懒技术之高超）");a.setButtonText("下一步");break;
case 2:a.setContent("你可以通过function [函数名]() {代码块}来创建一个简单的无参数函数，至于参数是什么，我等会儿会讲到\n只需要使用[函数名]()就可以调用这个无参函数了");a.setCode("function sayHello() {\n  vout.println(\"你好啊！\\n我在用VIDE和你说话！\"); //\\n表示换行，还记得吗？\n}\nsayHello();\nsayHello();\nsayHello();");a.setButtonText("运行");break;
case 3:a.run();break;
case 4:a.setContent("哇！我们只用了三句简单的sayHello()就省去了三句代码！\n其实，函数也是一种值，function [函数名]() {}相当于将函数这个值复制给这个变量");a.setCode("function hh() {\n  vout.println(\"HaHa!\");\n}\nvout.println(hh);");break;
case 5:a.run();break;
case 6:a.setContent("看到刚才的乱码了吗？那就是函数的字符串形式\n正应如此，我们也可以用另一种方式来创建函数");a.setCode("var a=function() {\n  vout.println(\"A!\");\n}\na();");break;
case 7:a.run();break;
case 8:a.setContent('回归到今天的问题，我们要输出"你好，Js！"，"你好，VIDE！","你好，世界！"三句话，显然我们不能用到刚才的无参数函数，这时我们就要用到有参函数！\n参数，就是一些需要变动的东西，比如那三句话中变化的就是Js，VIDE和世界\nfunction [函数名](参数) {}可以定义一个有参函数\n调用时只需[函数名](参数)即可\n当然，参数的名字是由你自己定义的');a.setCode("function say(n) {\n  vout.println(\"你好，\"+n+\"！\"); //加号用于字符串之间\n}\nsay(\"Js\");\nsay(\"VIDE\");\nsay(\"世界\");");break;
case 9:a.run();break;
case 10:a.setContent("成功了！同时，你也可以在函数中修改参数的值，因为参数在函数中相当于是一个变量");a.setCode("function say(a) {\n  a=1;\n  vout.println(a);\n}\nsay(2);\nsay(3);");break;
case 11:a.run();break;
case 12:a.setContent("你可以给一个函数添加多个参数，用英文逗号隔开，参看以下代码");a.setCode("function say(a,b) {\n  vout.println(a+\"和\"+b);\n}\nsay(\"我\",\"你\");");break;
case 13:a.run();break;
case 14:a.setContent("函数其实还有另一个用处，就是返回一个值！\n你可以在函数中return [值]，不仅会将值返回出去，也会同时立刻结束函数运行\n下面的代码创建了一个计算a+b的函数");a.setCode("function add(a,b) {\n  return a+b;\n}\nvout.println(add(1,2));\nvout.println(add(3,4));");break;
case 15:a.run();break;
case 16:finish(true);break;
default:a.run();break;
}
s++;
}