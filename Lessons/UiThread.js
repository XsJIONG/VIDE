a.setContent("线程是什么相信你应该很清楚了，由于Android不允许在主线程进行耗时操作(否则你的应用会ANR)，所以我们就要在线程中进行耗时操作\n下面我们来做一个计时器吧！\n代码看上去应该是这样的");
a.setButtonEnable(true);
a.setButtonText("运行");
a.setEditable(false);
a.setCode("var t=new android.widget.TextView(cx); //创建一个TextView，可以用于显示文字\nt.setGravity(android.view.Gravity.CENTER); //设置文字居中显示\ncx.setContentView(t);\nnew java.lang.Thread(new java.lang.Runnable({\n  run:function() {\n    var c=0;\n    while (true) {\n      java.lang.Thread.sleep(1000);\n      c++;\n      t.setText(\"第\"+c+\"秒\"); //设置TextView的文字\n    }\n  }\n})).start();");
var s=0;
var finish=false;
function onClick(v) {
switch (s) {
case 0:
  a.run();
  break;
case 1:
  a.setContent("啊嘞啊嘞？为什么崩溃了？让我们来看一下错误原因：\nOnly the original thread that created a view hierarchy can touch its views.\n翻译过来就是，\"只有创建View的线程才能够使用这个View\"\n原来是因为我们的TextView是在主线程中创建的，而我们却在子线程中修改了它的文字，所以引发了错误\n那有的童鞋会想了，我把TextView也放在子线程创建不就好了？");
  a.setCode("new java.lang.Thread(new java.lang.Runnable({\n  run:function() {\n    var c=0;\n    while (true) {\n      var t=new android.widget.TextView(cx); //创建一个TextView，可以用于显示文字\n      t.setGravity(android.view.Gravity.CENTER); //设置文字居中显示\n      cx.setContentView(t);\n      java.lang.Thread.sleep(1000);\n      c++;\n      t.setText(\"第\"+c+\"秒\"); //设置TextView的文字\n    }\n  }\n})).start();");
  break;
case 2:
  a.run();
  break;
case 3:
  a.setContent("为什么还是报错呢？\n原来Android是禁止在子线程中操作UI的\n这时候我们就要用到异步了\n我为你提供了一个方法 - ui.autoOnUi(java.lang.Runnable)\n这个方法会把中间的Runnable放到主线程执行");
  a.setCode("var t=new android.widget.TextView(cx); //创建一个TextView，可以用于显示文字\nt.setGravity(android.view.Gravity.CENTER); //设置文字居中显示\ncx.setContentView(t);\nnew java.lang.Thread(new java.lang.Runnable({\n  run:function() {\n    var c=0;\n    while (true) {\n      java.lang.Thread.sleep(1000);\n      c++;\n      ui.autoOnUi(new java.lang.Runnable({\n        run:function() {\n          t.setText(\"第\"+c+\"秒\"); //设置TextView的文字\n        }\n      }));\n    }\n  }\n})).start();");
  break;
case 4:
  a.run();
  break;
case 5:
  a.setContent("成功！你已经掌握了ui.autoOnUi的用法\n\n你可以更改代码来自由练习\n下课！");
  a.setEditable(true);
  finish=true;
  break;
default:
  a.run();
  break;
}
s++;
}
function onActivityFinish() {
  if (finish) return;
  onClick(null);
}