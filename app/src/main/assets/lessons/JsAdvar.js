const xk="今天的课就到这里，你可以修改代码来练习，下课！";
a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setContent("相信你应该很清楚如何声明变量了，今天我们来谈谈更多类型的变量以及变量的作用范围");
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
case 0:a.setContent("有一些变量，我们并不希望它被修改(比如圆周率π)，我们就可以使用const来创建一个常量(不能被修改的变量称作常量)\n当你尝试修改一个常量的值时，并不会有任何效果");a.setCode("const PI=3.14159;\nvout.println(PI);\nPI=1;\nvout.println(PI);");a.setButtonText("运行");break;
case 1:a.run();break;
case 2:a.setContent("变量的作用域是有限的，也就是说，如果你在一个函数中声明了一个变量，那么你在函数外面并获取不到这个变量");a.setCode("function test() {\n  var a=5;\n}\ntest();\nvout.println(a);");break;
case 3:a.run();break;
case 4:a.setContent("然而，在for循环、while循环以及if中声明的变量在外面却能获取到");a.setCode("for (var i=1;i<=10;i++) var a=1;\nvout.println(a);");break;
case 5:a.run();break;
case 6:finish(true);break;
default:a.run();break;
}
s++;
}