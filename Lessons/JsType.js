a.setEditable(false);
a.setButtonEnable(true);
var s=0,finished=false;
a.setContent("计算机可以存储多种类型的数据，其中有number(数字)、boolean(布尔值)、string(字符串)，今天我们就来重点讲讲这三种数据类型\n\n今天有一个很重要的东西：vout.println(一个数据);\n它可以把一个数据显示到屏幕上！");
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setButtonText("下一步");
function onClick(v) {
switch (s) {
case 0:
  a.setContent("number - 数字\n数字的表达形式很简单，就像你平时书写数字一样就可以了\n不过你并不能使用科学计数法(计算机读不懂啊说？)");
  a.setCode("vout.println(23);\nvout.println(3.14);\nvout.println(2333);");
  a.setButtonText("运行");
  break;
case 1:
  a.run();
  break;
case 2:
  a.setContent("boolean - 布尔值\n这个名词大家可能有些陌生，实际上，布尔值只有两个：true(真的)，false(假的)\n你可能想问：这有什么用？别急，在下面的课程中，你就能体会到它的重要性");
  a.setCode("vout.println(true);\nvout.println(false);");
  break;
case 3:
  a.run();
  break;
case 4:
  a.setContent("string - 字符串\n字符串，简单地来说，就是一段文本(比如你的用户名字，计算机就是使用字符串来存储的)，两端需要用英文的引号(也可以用英文的单引号，没有区别)括起来，就像下面这样");
  a.setCode("vout.println(\"一段文本\");\nvout.println('又一段文本');");
  break;
case 5:
  a.run();
  break;
case 6:
  a.setContent("不过值得注意的是，字符串只能写在一行代码\n也就是说，你不能跨过多行书写字符串\n那我们怎么书写回车呢？这时我们就要用到转义符\\n了\n在一段字符串中，你可以用\\n来代表一个回车");
  a.setCode("vout.println(\"第一行\\n第二行\");");
  break;
case 7:
  a.run();
  break;
case 8:
  a.setContent("不过有些童鞋要问了，如果我想在字符串中表示真正的\\n该怎么办？\n其实转义符有很多种，它们都以\\开头，下面是Js中的一些转义符：\n\\\" - 表示一个引号\n\\' - 表示一个单引号\n\\\& - 表示一个\&\n\\\\ - 表示一个反斜杠\n\\n - 回车\n\\t - 相当于在电脑上打一个Tab");
  a.setCode("vout.println(\"这是一个反斜杠：\\\\\");");
  break;
case 9:
  a.run();
  break;
case 10:
  a.setContent("今天的课程就到这里，你可以随意修改代码来练习，下课！");
  finish();
  break;
default:
  a.run();
  break;
}
s++;
}
function finish() {
  finished=true;
  a.setEditable(true);
}
function onActivityFinish() {
  if (finished) return;
  onClick();
}