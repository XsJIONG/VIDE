a.setEditable(false);
a.setButtonEnable(true);
var s=0,finished=false;

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

default:
  a.run();
  break;
}
s++;
}