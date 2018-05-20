a.setEditable(false);
a.setContent("上节课我们详细讲了ui.print的用法，但这种弹出式的信息不一会就会消失，然而有一些消息我们必须确定让用户清楚地看到，这时我们就要用到ui.alert了");
a.setButtonEnable(true);
a.setButtonText("下一步");
var s=0;
function onClick(v) {
switch (s) {
case 0:
  a.setContent("ui.alert的格式为\nui.alert(标题,内容);\n你可以参考下面的代码");
  a.setCode("ui.alert(\"标题\",\"这是一个对话框\");");
  a.setButtonText("运行");
  break;
case 1:
  a.run();
  break;
case 2:
  a.setContent("然而，ui.alert的功能实在是十分局限，你可以使用VAlertDialog来扩展它的功能\n使用ui.newAlertDialog(或者new com.jxs.vcompat.ui.VAlertDialog)后，你可以使用以下的函数(带括号的是可以不加的参数)：\nsetTitle(标题) - 设置对话框标题\nsetMessage(信息) - 设置对话框信息\nsetPositiveButton(文字,(是否在点击时关闭),点击事件) - 设置正面按钮(最右边)\nsetNegativeButton(文字,(是否在点击时关闭),点击事件) - 设置负面按钮(中间)\nsetNeutralButton(文字,(是否在点击时关闭),点击事件) - 设置普通按钮(最左边)\nsetItems(选项[字符串数组],(是否在点击时关闭),点击事件) - 设置对话框选项\nsetView(View) - 设置对话框的View\nsetCancelable(布尔值) - 设置对话框是否可以被取消(点击对话框外或者按返回键时)\nshow - 显示对话框\n\n你可以参考下面的代码");
  a.setCode("ui.newAlertDialog()\n  .setTitle(\"标题\")\n  .setMessage(\"这是对话框内容\")\n  .setCancelable(true)\n  .setPositiveButton(\"确定\", new com.jxs.vcompat.ui.VAlertDialog.OnClickListener({\n    onClick:function(dialog,pos) {\n      ui.print(\"你点击了确定键\");\n    }\n  }))\n  .show();");
  break;
case 3:
  a.run();
  break;
case 4:
  a.setContent("此外，当我们想让用户输入内容时，你还可以使用下面的函数\nsetEdit(文字) - 设置编辑框的文字\nsetEditHint(文字) - 设置编辑框的隐藏内容\nsetEditTextColor(颜色) - 设置编辑框的文字颜色\ngetEditText() - 获取输入框(EditText)\ngetInputText() - 获取用户输入的内容\n\n你可以参照下面的代码");
  a.setCode("ui.newAlertDialog()\n  .setEdit(\"Xs.JIONG\")\n  .setEditHint(\"你的名字\")\n  .setPositiveButton(\"确定\",new com.jxs.vcompat.ui.VAlertDialog.OnClickListener({\n    onClick:function(dialog,pos) {\n      ui.print(\"你好,\"+dialog.getInputText()+\"!\");\n    }\n  }))\n  .show();");
  break;
case 5:
  a.run();
  break;
case 6:
  a.setContent("你可以随意更改代码来练习\n\n下课!");
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