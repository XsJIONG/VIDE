package com.jxs.vide;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.jxs.v.widget.*;
import com.jxs.vcompat.activity.*;
import com.jxs.vcompat.ui.*;
import com.jxs.vcompat.widget.*;
import java.io.*;
import java.util.*;

import android.support.design.widget.FloatingActionButton;
import android.view.View.OnClickListener;
import com.jxs.v.widget.CardView;
import com.jxs.vcompat.widget.VListView;

import static com.jxs.vide.L.get;

public class VAppActivity extends VActivity {
	public static VAppActivity Top;
	private SwipeRefreshLayout Refresh;
	private VListView ContentList;
	private VAppAdapter Adapter;
	private RelativeLayout Root;
	private FloatingActionButton Upload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Top = this;
		super.onCreate(savedInstanceState);
		enableBackButton();
		setTitleText(get(L.Title_VApp));
		setSubTitle(get(L.HaveFun));
		Global.checkBmob(this);
		initView();
		setContentView(Root);
		loadData();
	}
	public static void showMessage(CharSequence cs) {
		if (Top == null) return;
		Top.ui.print(cs);
	}
	public static void refresh() {
		if (Top == null) return;
		Top.loadData();
	}
	private void loadData() {
		Refresh.post(new Runnable() {
				@Override
				public void run() {
					Refresh.setRefreshing(true);
				}
			});
		BmobQuery<VAppEntity> q=new BmobQuery<>();
		q.order("-createdAt");
		q.findObjects(new FindListener<VAppEntity>() {
				@Override
				public void done(List<VAppEntity> data, BmobException e) {
					if (e != null) {
						Global.onBmobErr(ui, e);
						Refresh.setRefreshing(false);
						return;
					}
					if (data == null) return;
					Adapter.clear();
					for (VAppEntity one : data) Adapter.add(one);
					Adapter.notifyDataSetChanged();
					Refresh.setRefreshing(false);
				}
			});
	}
	@Override
	public void onThemeChange(String key) {
		super.onThemeChange(key);
		if (!key.equals(UI.THEME_UI_COLOR)) return;
		Upload.setBackgroundTintList(ColorStateList.valueOf(UI.getThemeColor()));
		for (int i=0;i < ContentList.getChildCount();i++) ((VAppAdapter.ViewHolder) ContentList.getChildAt(i).getTag()).onThemeChange();
		Refresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
	}
	public static final File TempJscDir=new File(Environment.getExternalStorageDirectory(), "Android/data/com.jxs.vide/Jsc");
	private void initView() {
		Root = new RelativeLayout(this);
		Refresh = new SwipeRefreshLayout(this);
		Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					Global.UserNames.clear();
					loadData();
				}
			});
		Refresh.setColorSchemeColors(new int[]{UI.getThemeColor()});
		ContentList = new VListView(this);
		ContentList.setDividerHeight(0);
		ContentList.setDivider(null);
		ContentList.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Adapter = new VAppAdapter(this);
		ContentList.setAdapter(Adapter);
		Refresh.addView(ContentList);
		Root.addView(Refresh, new RelativeLayout.LayoutParams(-1, -1));
		Upload = new FloatingActionButton(this);
		Upload.setImageResource(R.drawable.icon_upload);
		Upload.setBackgroundTintList(ColorStateList.valueOf(UI.getThemeColor()));
		RelativeLayout.LayoutParams UploadPara=new RelativeLayout.LayoutParams(UI.dp2px(64), UI.dp2px(64));
		UploadPara.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		UploadPara.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		UploadPara.bottomMargin = UploadPara.rightMargin = UI.dp2px(32);
		Root.addView(Upload, UploadPara);
		Upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (BmobUser.getCurrentUser(VUser.class) == null) {
						ui.toast(get(L.LoginFirst));
						startActivity(new Intent(VAppActivity.this, LoginActivity.class));
						return;
					}
					startActivity(new Intent(VAppActivity.this, BmobUser.getCurrentUser(VUser.class) == null ?LoginActivity.class: UploadVAppActivity.class));
				}
			});
		ContentList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					VAppEntity en=Adapter.getItem(pos);
					downloadJsc(en, getTempFile(en));
				}
			});
	}
	private void runJsc(File f) {
		Intent i=new Intent(VAppActivity.this, NoDisplayActivity.class);
		i.putExtra("JscPath", f.getAbsolutePath());
		startActivity(i);
	}
	private void downloadJsc(VAppEntity en, File f) {
		VProgressDialog dialog=ui.newLoadingDialog();
		dialog.setTitle(get(L.Wait));
		dialog.setCancelable(false).setMessage(String.format(get(L.Downloading), 0));
		ProgressDialog d=dialog.show();
		new Thread(new DownloadRunnable(this, en, f, new DialogHandler(d))).start();
	}
	public static class DownloadRunnable implements Runnable {
		private DialogHandler H;
		private VAppEntity en;
		private File f;
		private VAppActivity cx;
		public DownloadRunnable(VAppActivity cx, VAppEntity en, File f, DialogHandler handler) {
			H = handler;
			this.en = en;
			this.f = f;
			this.cx = cx;
		}
		@Override
		public void run() {
			if (en.Content == null) return;
			en.Content.download(f, new DownloadFileListener() {
					@Override
					public void done(String path, BmobException e) {
						if (e != null) {
							H.dismiss();
							Global.onBmobErr(cx.ui, e);
							return;
						}
						H.dismiss();
						cx.ui.autoOnUi(new Runnable() {
								@Override
								public void run() {
									cx.runJsc(f);
								}
							});
					}
					@Override
					public void onProgress(Integer value, long size) {
						H.setMessage(String.format(get(L.Downloading), value));
					}
				});
		}
	}
	public static File getTempFile(VAppEntity entity) {
		if (!TempJscDir.exists()) TempJscDir.mkdirs();
		return new File(TempJscDir, entity.getObjectId());
	}
	public static class VAppAdapter extends ArrayAdapter<VAppEntity> {
		public VAppAdapter(Context cx) {
			super(cx, android.R.layout.simple_list_item_1, new ArrayList<VAppEntity>());
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			VAppEntity entity=getItem(position);
			LinearLayout Content=new LinearLayout(getContext());
			Content.setGravity(Gravity.CENTER);
			Content.setClickable(false);
			CardView Card=new CardView(getContext());
			Card.setCardElevation(5f);
			Card.setRadius(5f);
			LinearLayout root=new LinearLayout(getContext());
			root.setOrientation(LinearLayout.VERTICAL);
			TextView Title=new TextView(getContext());
			Title.setText(entity.Title);
			Title.setGravity(Gravity.LEFT);
			Title.setSingleLine(true);
			Title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			root.addView(Title, new LinearLayout.LayoutParams(-1, -2));
			TextView SubTitle=new TextView(getContext());
			SubTitle.setMaxEms(15);
			SubTitle.setSingleLine(true);
			SubTitle.setMaxLines(1);
			SubTitle.setGravity(Gravity.LEFT);
			SubTitle.setText(entity.Description);
			SubTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			LinearLayout bottomLayout=new LinearLayout(getContext());
			bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
			bottomLayout.setGravity(Gravity.BOTTOM);
			LinearLayout.LayoutParams SubPara=new LinearLayout.LayoutParams(0, -2);
			SubPara.weight = 1;
			bottomLayout.addView(SubTitle, SubPara);
			final TextView User=new TextView(getContext());
			User.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			User.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
			bottomLayout.addView(User);
			LinearLayout.LayoutParams BottomPara=new LinearLayout.LayoutParams(-1, -2);
			BottomPara.topMargin = UI.dp2px(15);
			root.addView(bottomLayout, BottomPara);
			int p=UI.dp2px(13);
			Card.setContentPadding(p, p, p, p);
			Card.addView(root, new CardView.LayoutParams(-1, -1));
			LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(-1, -1);
			para.setMargins(p, p, p, p);
			Content.addView(Card, para);
			ViewHolder holder=new ViewHolder();
			holder.Card = Card;
			holder.Title = Title;
			holder.Des = SubTitle;
			holder.User = User;
			holder.onThemeChange();
			Content.setTag(holder);
			if (Global.UserNames.containsKey(entity.Author)) User.setText(Global.UserNames.get(entity.Author)); {
				BmobQuery<VUser> q=new BmobQuery<>();
				q.addWhereEqualTo("objectId", entity.Author);
				q.setLimit(1); //WTF
				q.findObjects(new FindListener<VUser>() {
						@Override
						public void done(List<VUser> data, BmobException e) {
							if (e != null) {
								Global.onBmobErr(new UI(getContext()), e);
								return;
							}
							if (data == null || data.size() != 1) return;
							VUser u=data.get(0);
							Global.UserNames.put(u.getObjectId(), u.getUsername());
							User.setText(u.getUsername());
						}
					});
			}
			return Content;
		}
		private static class ViewHolder {
			public CardView Card;
			public TextView Title,Des,User;
			public void onThemeChange() {
				int ui=UI.getThemeColor();
				int w=UI.getAccentColor();
				if (Card != null) Card.setCardBackgroundColor(ui);
				if (Title != null) Title.setTextColor(w);
				if (Des != null) Des.setTextColor(w);
				if (User != null) User.setTextColor(w);
			}
		}
	}
}
