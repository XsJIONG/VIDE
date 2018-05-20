a.setEditable(false);
a.setButtonEnable(true);
var s=0,finished=false;
a.setContent("标题栏(Toolbar)位于整个界面的最上方，它可以给用户提供很多信息\n下面我将告诉你几个简单的函数来自定它");
a.setButtonText("下一步");
function onActivityFinish() {
  if (finished) return;
  onClick(null);
}
function finish() {
  finished=true;
  a.setEditable(true);
}
function onClick(v) {
switch (s) {
case 0:
  a.setContent("setTitleText(标题) - 设置标题\nsetSubTitle(副标题) - 设置副标题\nsetTitleTextColor(颜色) - 设置标题颜色\nsetSubTitleTextColor(颜色) - 设置副标题颜色\nenableBackButton() - 启用返回键(默认禁用)\ndisableBackButton() - 禁用返回键\nsetBackButtonColor(颜色) - 设置返回键颜色\n\n下面是一段演示代码");
  a.setCode("cx.setTitleText(\"这是一个JsApp\");\ncx.setSubTitle(\"作者XXX\");\ncx.setTitleTextColor(android.graphics.Color.BLACK); //设置标题颜色为黑色\ncx.enableBackButton();\ncx.setBackButtonColor(android.graphics.Color.BLACK);");
  a.setButtonText("运行");
  break;
case 1:
  a.run();
  break;
case 2:
  a.setContent("注意到标题栏下面的阴影了吗？\n你可以通过cx.setTitleElevation(阴影大小)来控制它");
  a.setCode("cx.setTitleElevation(0);");
  break;
case 3:
  a.run();
  break;
case 4:
  a.setContent("你可以自由修改代码来练习\n下课！");
  finish();
  break;
default:
  a.run();
  break;
}
s++;
}