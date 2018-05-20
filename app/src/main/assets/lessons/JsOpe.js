const xk="今天的课就到这里，你可以修改代码来练习，下课！";
a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setContent("和我们在数学中要用到运算符号(+-*/)一样，Js中也有很多运算符号\n今天我们就来讲一下其中的一部分");
a.setButtonText("下一步");
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
case 0:a.setContent("首先就是我们最熟悉的四个运算符——加减乘除+-*/\n它们的使用方法和数学中大致一样\n和数学一样，Js也可以使用括号，不过并没有中括号大括号之分，全部使用小括号");a.setCode("vout.println(1+2);\nvout.println((1+3)*(2+5));");a.setButtonText("运行");break;
case 1:a.run();break;
case 2:a.setContent("同时，加号+也可以用于字符串的拼接");a.setCode('vout.println("A"+"B");\nvar a="Js";\nvout.println("你好，"+a);');break;
case 3:a.run();break;
case 4:a.setContent("这里我们还要接触到一个特殊的取余运算符%\n我们都知道，5除以2等于2，余1。那么%就是取得这个余数");a.setCode("vout.println(5%2);\nvout.println(6%4);");break;
case 5:a.run();break;
case 6:a.setContent("当然，极致偷懒的程序员也设计了一些针对于变量的运算符\n它们分别是+=，-=，*=，/=和%=\n它们的作用和+-*/%基本相似，只是它们会对变量进行赋值\n比如a+=2与a=a+2的作用相同");a.setCode("var a=1;\nvout.println(a);\na+=3;\nvout.println(a);\na*=2;\nvout.println(a);");break;
case 7:a.run();break;
case 8:a.setContent("让我们来见识一下程序员懒到极致的性格！\n++和--运算符，与[变量]+=1和[变量]-=1的作用相同");a.setCode("var a=1;\na++;\nvout.println(a);\na--;\nvout.println(a);");break;
case 9:a.run();break;
case 10:a.setContent("接下来，我们要认识一类特殊的运算符——比较运算符！\n其中有==(等于)，!=(不等于)，>=(大于等于)，>(大于)，<=(小于等于)和<(小于)\n它们都会返回一个布尔值(true或false,真的 或 假的)");a.setCode("vout.println(2>3);\nvout.println(5<=6);\nvout.println(5==5);\nvout.println(\"ASD\"==\"ASD\");");break;
case 11:a.run();break;
case 12:a.setContent("随后是一类更特殊的运算符，逻辑运算符\n它们只能用于布尔值相互的运算\n&&(并且)，||(或者)，!(不)");a.setCode("vout.println((1==2)&&(2==2)); //1等于2并且2等于2，返回false(假的)\nvout.println((2==3)||(3==3)); //2等于3或3等于3，返回true(真的)\nvout.println(!(3>5)); //3大于5原本是false，但加了感叹号(否定运算)，就变成了true；同理，!true的值是false");break;
case 13:a.run();break;
case 14:finish(true);break;
default:a.run();break;
}
s++;
}