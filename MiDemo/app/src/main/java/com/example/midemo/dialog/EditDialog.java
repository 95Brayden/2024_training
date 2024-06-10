package com.example.midemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.midemo.R;

public class EditDialog extends Dialog implements View.OnClickListener {
    private int recordId;
    private float currentMoney;
    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;
    public  interface OnEnsureListener{
        void onEnsure(int id, float money);
    }
    OnEnsureListener onEnsureListener;
    public EditDialog(Context context, int id, float money) {
        super(context);
        this.recordId = id;
        this.currentMoney = money;
        // 初始化布局和其他设置
    }
    public void setOnEnsureListener(OnEnsureListener listener) {
        this.onEnsureListener = listener;
        ensureBtn.setOnClickListener(v -> {
            // 获取用户输入的新金额
            float newMoney = Float.parseFloat(moneyEt.getText().toString());
            listener.onEnsure(recordId, newMoney);
            dismiss();
        });
    }

    public EditDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        cancelIv = findViewById(R.id.dialog_edit_iv_error);
        ensureBtn = findViewById(R.id.dialog_edit_btn_ensure);
        moneyEt = findViewById(R.id.dialog_edit_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_edit_iv_error:
                cancel();  //取消对话框
                break;
            case R.id.dialog_edit_btn_ensure:
                //获取输入数据数值
                String data = moneyEt.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(),"输入数据不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if (money<=0) {
                    Toast.makeText(getContext(),"金额必须大于0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(recordId,currentMoney);
                }
                cancel();
                break;
        }
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    public void setDialogSize(){
//        获取当前窗口对象
        Window window = getWindow();
//        获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
//        获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软键盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}