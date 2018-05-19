a.setContent("欢迎您使用VIDE!\n鉴于你已经有一定的JS基础，我将在下面的这一章中直接为你讲述有VIDE的使用方法——以及如何用VIDE开发一个App！");
a.setButtonEnable(true);
a.setButtonText("让我们开始吧！");
a.setEditable(false);
var s=0;
function onClick(v) {
switch(s) {
case 0:
  a.setContent("如果你是一个ModPE开发者，你应该十分熟悉print函数吧！\n这个函数的作用是将一个值的字符串形式显示到屏幕上\n在VIDE中，我也为你提供了作用类似的函数");
  a.setButtonText("下一步");
  break;
case 1:
  a.setContent("VIDE的运行环境中有一个名为ui的常量，这个常量中有许多方法，ui.print就是其中之一\n但有所不同的是，ui.print要求传入一个字符串类型的参数(准确地说是CharSequence)，所以直接使用下面的代码是非法的");
  a.setButtonText("试试吧!");
  a.setCode("ui.print(5);");
  break;
case 2:
  a.run();
  break;
case 3:
  a.setContent("正如你所见，这段代码是会报错的\n因此，我们想到了用String函数将数字转换成字符串");
  a.setButtonText("运行");
  a.setCode("ui.print(String(5));");
  break;
case 4:
  a.run();
  break;
case 5:
  a.setContent("成功！\n相信你也发现了，ui.print的效果与在Mod上的效果不大一样，这是因为VIDE使用了Appcompat包，它会使程序变得更加美观(当然，你也可以在实际开发中取消\"使用兼容\"来取消Appcompat)\n同时，ui.print也有一些使用上要注意的地方\n例如，当你连续使用多次ui.print时，系统只会显示最后一次的文字");
  a.setCode("ui.print(\"第一条信息\");\nui.print(\"第二条信息\");");
  break;
case 6:
  a.run();
  break;
case 7:
  a.setContent("ui还有更多的函数，我将在下一课为你详细讲述\n你可以更改代码来随意练习\n\n下课!");
  a.setEditable(true);
  finish=true;
  break;
default:
  a.run();
  break;
}
s++;
}
var finish=false;
function onActivityFinish() {
  if (finish) return;
  onClick(null);
}