const xk="今天的课就到这里，你可以修改代码来练习，下课！";
a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setContent("今天我们来学习对象的概念！\n请注意，此对象非彼对象[滑稽]\n对象是在计算机语言中一个很重要的概念\n如果你理解了对象，那么不仅是Js，Java等语言对你来说也会变得很简单");
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
case 0:a.setContent("世界上的许多事物都可以用 对象(Object) 来表示\n而对象是由许多的属性构成的\n比如，VIDE可以被视为一个对象，那么它的\"作者\"这一属性就是\"Xs.JIONG\"\n这个苹果也有许多属性，比如大小、颜色、品种等等");break;
case 1:a.setContent("让我们来看一段你很熟悉的代码\n这段代码创建了一个变量，数据设置为\"abcd\"\n那么，我要告诉你的是，你就已经创建了一个对象了！");a.setCode("var a=\"abcd\";");a.setButtonText("怎么回事？");break;
case 2:a.setContent("其实字符串这一数据是很特殊的，它是由许多属性构成的一个对象，其中包括长度(length)等等\n访问属性的代码是[数据].[属性名]\n我们可以尝试来输出一下");a.setCode("var a=\"abcd\";\nvout.println(a.length);");a.setButtonText("运行");break;
case 3:a.run();break;
case 4:a.setContent("成功！\n相信你已经注意到了，我们一直在使用的vout.println，println是不是就是vout中的一个的属性呢？\n对！准确来说，println是一个函数");a.setButtonText("下一步");break;
case 5:a.setContent("你可以通过new Object()来创建一个对象，并随意修改它的属性\n你甚至可以为它添加一个函数！还记得我们讲到的函数的另一种创建方式吗？");a.setButtonText("运行");a.setCode("var apple=new Object();\napple.size=50;\napple.color=\"红色\";\napple.print=function() {\n  vout.println(\"苹果大小：\"+apple.size+\"，苹果颜色：\"+apple.color);\n}\napple.print();");break;
case 6:a.run();break;
case 7:a.setContent("但是不可能我们每新建一个apple都要把上面的东西重复制一遍吧！\n这时我们学过的函数就又派上用场了");a.setCode("function getApple(a,b) {\n  var apple=new Object();\n  apple.size=a;\n  apple.color=b;\n  apple.print=function () {\n    vout.println(\"苹果大小：\"+apple.size+\"，苹果颜色：\"+apple.color);\n  }\n  return apple;\n}\nvar a=getApple(20,\"蓝色\");\na.print();\na=getApple(45,\"黑色\");\na.print();");break;
case 8:a.run();break;
case 9:a.setContent("当然，这样的方式也有一丢丢麻烦，那就是我们每个函数中都要写var xx=new Object()，有其它的解决方法吗？\n当然有！你可以在function中使用this.xx(this是一个关键字)来代替上面代码中的apple.xx，然后在创建时使用new [函数名]()\n(new也是一个关键字)");a.setCode("function Apple(a,b) {\n  this.size=a;\n  this.color=b;\n  this.print=function () {\n    vout.println(\"苹果大小：\"+this.size+\"，苹果颜色：\"+this.color);\n  }\n}\nvar a=new Apple(20,\"蓝色\");\na.print();\na=new Apple(45,\"黑色\");\na.print();");break;
case 10:a.run();break;
case 11:finish(true);break;
default:a.run();break;
}
s++;
}