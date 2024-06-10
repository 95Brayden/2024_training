package com.example.midemo.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.midemo.R;

public class KeyBoardUtils {
    private final KeyboardView keyboardView;
    private final EditText editText;

    public interface OnEnsureListener {
        void onEnsure();
    }

    private OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);  // 取消弹出系统键盘
        // 自定义键盘
        Keyboard k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1);  // 设置要显示键盘的样式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        // 点击了删除键
        // 点击了清零
        // 点击了完成
        // 通过接口回调的方法，当点击确定时，可以调用这个方法
        // 其他数字直接插入
        KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable editable = editText.getText();
                int start = editText.getSelectionStart();
                switch (primaryCode) {
                    case Keyboard.KEYCODE_DELETE:   // 点击了删除键
                        if (editable != null && editable.length() > 0) {
                            if (start > 0) {
                                editable.delete(start - 1, start);
                            }
                        }
                        break;
                    case Keyboard.KEYCODE_CANCEL:   // 点击了清零
                        if (editable != null) {
                            editable.clear();
                        }
                        break;
                    case Keyboard.KEYCODE_DONE:    // 点击了完成
                        if (editable == null || editable.length() == 0) {
                            Toast.makeText(editText.getContext(), "请输入金额", Toast.LENGTH_SHORT).show();
                        } else if (onEnsureListener != null) {
                            onEnsureListener.onEnsure();   // 通过接口回调的方法，当点击确定时，可以调用这个方法
                        }
                        break;
                    default:  // 其他数字直接插入
                        if (editable != null) {
                            editable.insert(start, Character.toString((char) primaryCode));
                        }
                        break;
                }
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeUp() {
            }
        };
        this.keyboardView.setOnKeyboardActionListener(listener);  // 设置键盘按钮被点击的监听
    }

    // 显示键盘
    public void showKeyboard() {
        if (keyboardView.getVisibility() != View.VISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
}
