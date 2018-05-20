const xk="今天的课就到这里，你可以修改代码来练习，下课！";
a.setEditable(false);
a.setButtonEnable(true);
a.setRunClass("com.jxs.vapp.runtime.JsConsoleActivityCompat");
a.setContent("布尔值最大的用处就是条件语句！\n今天我们来讲一些条件语句");
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
case 0:a.setContent("首先是if语句\nif是\"如果\"的意思，它的格式是if (布尔值) [代码或代码块]，当中间的布尔值为true时，就执行后面的代码，否则不执行");a.setCode("if (1==1) vout.println(\"1确实等于1！\");\nif (1==2) vout.println(\"夭寿啦！1等于2啦！\");\nif (3>2) {\n  vout.println(\"3大于2！\");\n  vout.println(\"这是真理！\");\n}");a.setButtonText("运行");break;
case 1:a.run();break;
case 2:a.setContent("接下来是while语句，while是\"当...\"的意思，while (布尔值) [代码或代码块]，只要中间的布尔值为true，它就不停循环执行后面的代码\n因此，while (true) xxx会陷入一个死循环！\n我们可以借助这个语句来写一个倒数器");a.setCode("var a=10;\nwhile (a>0) {\n  vout.println(a);\n  a--;\n}");break;
case 3:a.run();break;
case 4:a.setContent("最终是最难的一个语句——for语句！\nfor我就不知道怎么翻译了...为了？不太合适\nfor (初始化语句;布尔值;更改语句) [代码或代码块]\n首先程序会执行初始化一句(只执行一遍)，然后会判断布尔值，如果布尔值不为false，就执行后面的语句，执行完后执行一次更改语句，然后再判断...\n不懂？看看下面的代码");a.setCode("for (var i=1;i<=10;i++) //首先新建变量a并赋值为1，然后只要i小于10，就执行下面的代码，然后执行i++，再判断i小于10...\n  vout.println(i);\n//下面这段代码和上面的效果相同\nvar i=1;\nwhile (i<=10) {\n  vout.println(i);\n  i++;\n}");break;
case 5:a.run();break;
case 6:finish(true);break;
default:a.run();break;
}
s++;
}