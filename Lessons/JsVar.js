a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
var s=0,finished=false;
a.setContent("什么是变量？\n通俗地说，变量就是一个箱子，里面可以放入一个数据，你可以随时读取这个数据，也可以替换这个数据\n学过英语的童鞋知道，英语有多种句式(陈述句、疑问句)，而我们今天要讲的声明变量语句也是编程语法其中一种\n要创建一个变量的语法是\"var [变量名]=[数据];\"");
a.setCode("var a=3;\nvar 名字=\"XsJIONG\";");
function onActivityFinish() {
  if (!finished) onClick();
}
function onClick(v) {
switch (s) {
case 0:a.run();break;
case 1:a.setContent("好吧——我们什么都没看到，因为你只是创建了一个箱子，接下来我们要把箱子里面的东西放到屏幕上！");a.setButtonText("下一步");break;
case 2:a.setContent("还记得上节课讲到的vout.println()吗？是时候展现真正的技术了！\n在代码中，你可以直接写变量名来这个箱子里面的内容");a.setCode("var a=3;\nvout.println(a);");a.setButtonText("运行");break;
case 3:a.run();break;
case 4:a.setContent("成功了！\n然而，变量既然是箱子，就应该发挥它箱子的作用\n我们可以通过[变量名]=[数据]来修改变量的值");a.setCode("var a=4;\nvout.println(a);\na=5;\nvout.println(a);");break;
case 5:a.run();break;
case 6:a.setContent("当然，你也可以把一个变量的值赋值给另一个变量");a.setCode("var a=4;\nvar b=5;\nvout.println(a);\na=b;\nvout.println(a);");break;
case 7:a.run();break;
case 8:a.setContent("偷偷告诉你，你也可以通过var [变量名];来创建一个变量，此时这个变量是空的，没有任何的值");a.setCode("var a;");a.setButtonText("下一步");break;
case 9:a.setContent("今天的课程就到这里，你可以随意修改代码来练习，下课！");a.setButtonText("运行");finished=true;break;
default:a.run();s--;break;
}
s++;
}