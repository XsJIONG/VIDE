package com.jxs.vide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import com.jxs.vcompat.ui.UI;
import com.myopicmobile.textwarrior.android.ClipboardPanel;
import com.myopicmobile.textwarrior.android.FreeScrollingTextField;
import com.myopicmobile.textwarrior.android.YoyoNavigationMethod;
import com.myopicmobile.textwarrior.common.ColorScheme;
import com.myopicmobile.textwarrior.common.ColorSchemeDark;
import com.myopicmobile.textwarrior.common.ColorSchemeLight;
import com.myopicmobile.textwarrior.common.Document;
import com.myopicmobile.textwarrior.common.DocumentProvider;
import com.myopicmobile.textwarrior.common.LanguageJavascript;
import com.myopicmobile.textwarrior.common.Lexer;
import java.lang.reflect.Field;

public class TextEditor extends FreeScrollingTextField {
	private Document _inputtingDoc;
	private boolean _isWordWrap;
	private Context mContext;
	private String _lastSelectFile;
	private int _index;
	public TextEditor(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public TextEditor(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		init();
	}

	private  void init() {
		setTypeface(Typeface.MONOSPACE);
		DisplayMetrics dm=mContext.getResources().getDisplayMetrics();
		float size= TypedValue.applyDimension(2, BASE_TEXT_SIZE_PIXELS, dm);
		setTextSize((int)size);
		setShowLineNumbers(true);
		setHighlightCurrentRow(true);
		setWordWrap(false);
		setAutoIndentWidth(2);
		Lexer.setLanguage(LanguageJavascript.getInstance());
		setNavigationMethod(new YoyoNavigationMethod(this));
		int textColor = Color.BLACK;// 默认文字颜色
		int selectionText =Color.argb(255, 0, 120, 215);//选择文字颜色
		setTextColor(textColor);
		setTextHighlightColor(selectionText);
		setColorScheme(new LightColorScheme());
		YoyoNavigationMethod.setCaretColor(UI.getThemeColor());
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (_index != 0 && right > 0) {
			moveCaret(_index);
			_index = 0;
		}
	}

	public void setDark(boolean isDark) {
		if (isDark) {
			setColorScheme(new ColorSchemeDark());
		} else {
			setColorScheme(new ColorSchemeLight());
		}
	}
	public void setPanelBackgroundColor(int color) {
		_autoCompletePanel.setBackgroundColor(color);
	}
	public void setPanelTextColor(int color) {
		_autoCompletePanel.setTextColor(color);
	}
	public void setKeywordColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.KEYWORD, color);
	}

	public void setUserWordColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.LITERAL, color);
	}

	public void setBaseWordColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.NAME, color);
	}

	public void setStringColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.STRING, color);
	}

	public void setCommentColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.COMMENT, color);
	}
	public void setTextColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.FOREGROUND, color);
	}

	public void setTextHighlightColor(int color) {
		getColorScheme().setColor(ColorScheme.Colorable.SELECTION_BACKGROUND, color);
	}

	public String getSelectedText() {
		// TODO: Implement this method
		return  _hDoc.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
	}

	private long last=-1;
    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        final int filteredMetaState = event.getMetaState() & ~KeyEvent.META_CTRL_MASK;
        if (KeyEvent.metaStateHasNoModifiers(filteredMetaState)) {
			if (last != -1 && System.currentTimeMillis() - last <= 100)
				return true;
			last = System.currentTimeMillis();
            switch (keyCode) {
				case KeyEvent.KEYCODE_A:
					selectAll();
					return true;
				case KeyEvent.KEYCODE_X:
					cut();
					hideActionMode();
					return true;
				case KeyEvent.KEYCODE_C:
					copy();
					hideActionMode();
					return true;
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }
	private void hideActionMode() {
		try {
			Field f=FreeScrollingTextField.class.getDeclaredField("_clipboardPanel");
			f.setAccessible(true);
			ClipboardPanel panel=(ClipboardPanel) f.get(this);
			if (panel != null) panel.stopClipboardAction();
		} catch (Exception e) {}
	}
	@Override
	public void setWordWrap(boolean enable) {
		_isWordWrap = enable;
		super.setWordWrap(enable);
	}

	public DocumentProvider getText() {
		return createDocumentProvider();
	}
	public void insert(int idx, String text) {
		selectText(false);
		moveCaret(idx);
		paste(text);
	}

	public void setText(CharSequence c, boolean isRep) {
		replaceText(0, getLength() - 1, c.toString());
	}

	public void setText(CharSequence c) {
		Document doc=new Document(this);
		doc.setWordWrap(_isWordWrap);
		doc.setText(c);
		DocumentProvider p=new DocumentProvider(doc);
		p.setEditable(getText().isEditable());
		setDocumentProvider(p);
	}

	public void setSelection(int index) {
		selectText(false);
		if (!hasLayout())
			moveCaret(index);
		else
			_index = index;
	}
	public void undo() {
		DocumentProvider doc = createDocumentProvider();
		int newPosition = doc.undo();

		if (newPosition >= 0) {
			//TODO editor.setEdited(false); if reached original condition of file
			setEdited(true);

			respan();
			selectText(false);
			moveCaret(newPosition);
			invalidate();
		}
	}
	public void redo() {
		DocumentProvider doc = createDocumentProvider();
		int newPosition = doc.redo();
		if (newPosition >= 0) {
			setEdited(true);
			respan();
			selectText(false);
			moveCaret(newPosition);
			invalidate();
		}
	}
}
