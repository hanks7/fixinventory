package com.easyway.fixinventory.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyway.BarcodeObject;
import com.easyway.BarcodeSimple;
import com.easyway.RefObject;
import com.easyway.fixinventory.R;
import com.hanks.frame.utils.UToast;
import com.hanks.frame.utils.Ulog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easyway.fixinventory.base.BaseConstants.IS_RELEASE;


/**
 * Created by User on 2017/4/19.
 */

public class BarCodeEditView extends LinearLayout {


    private final float mContentSize;
    private final float mTitleSize;

    @BindView(R.id.item_input_iv_close)
    ImageView ivClose;
    @BindView(R.id.item_input_tv_title)
    TextView tvTitleName;
    @BindView(R.id.item_input_edt_content)
    EditText edtContent;

    private String mTitle;
    private String mContent;

    public String strBarCodeSource;
    public String strSubBarCodeSource;
    public String strBarCode;
    public String strSubBarCode;
    public BarcodeObject barcodeObject;

    /**
     * "BarType": "R",
     * "Barcode": "1719050010170616V",
     * "BarcodeControlSymbol": "~9~9v[\"cc&,R,",
     * "BarcodeSrc": "~9~9v[\"cc&,R,1719050010170616V",
     * "ErrNo": 0,
     * "ExpirationDate": "2019-05-31",
     * "Lot": "170616V",
     * "RetryTimes": 0,
     * "ScanerSN": "30002",
     * "SubCode": "1719050010170616V",
     * "barcodeType": "Secondary"
     * <p>
     * "BarType": "R",
     * "Barcode": "010082700209497017201130108409387",
     * "BarcodeControlSymbol": "\"x\"i*Y|sz`,R,",
     * "BarcodeSrc": "\"x\"i*Y|sz`,R,010082700209497017201130108409387",
     * "Country": "美国",
     * "ErrNo": 0,
     * "ExpirationDate": "2020-11-30",
     * "Lot": "8409387",
     * "MainCode": "0100827002094970",主码
     * "Manufacturer": "70020",
     * "Merchandise": "9497",
     * "Package": "0",
     * "RetryTimes": 0,
     * "ScanerSN": "30002",
     * "SubCode": "17201130108409387",次码
     * "barcodeType": "Concatenated"
     */


    public BarCodeEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarCodeEditView(Context context) {
        this(context, null);
    }

    public BarCodeEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_input_my_edit_view, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.input_test_view);
        ButterKnife.bind(this, view);

        mTitle = array.getString(R.styleable.input_test_view_input_title);
        mContent = array.getString(R.styleable.input_test_view_input_content);
        mTitleSize = array.getDimension(R.styleable.input_test_view_input_title_size, 14);
        mContentSize = array.getDimension(R.styleable.input_test_view_input_title_size, 14);


        tvTitleName.setTextSize(mTitleSize);
        edtContent.setTextSize(mContentSize);

        tvTitleName.setText(mTitle);
        edtContent.setText(mContent);
    }


    public void setOnEditorBarCodeTypeListener(final AsdfdsListener listener) {
        setBarCodeEmpty();
        edtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                test(textView);//测试上线后关闭

                if (TextUtils.isEmpty(textView.getText().toString())) {
                    UToast.showText("输入不能为空");
                    return false;
                }
                String strEditMain = getText().replace('', ',');
                barcodeObject = getBarcodeObject(strEditMain);

                if (i == 5 || i == 0 || i == 6) {
                    Ulog.i("barCodeType", "barCodeType");
                    if (barcodeObject.getBarcodeType() == null) {
                        UToast.showText("条码格式不正确");
                        selectAll();
                        return false;
                    }
                    switch (barcodeObject.getBarcodeType()) {
                        case Primary:
                            strBarCodeSource = strEditMain;
                            strBarCode = barcodeObject.getBarcode();
                            strSubBarCodeSource = null;//次码设置为空
                            strSubBarCode = null;//次码设置为空
                            setTitleText(getContext().getString(R.string.primary_code));
                            setText(strBarCode);
                            listener.barCodeType(1);
                            break;
                        case Secondary:
                            if (strBarCode == null) {
                                UToast.showText("请先扫描主码");
                                selectAll();
                            }
                            strSubBarCodeSource = strEditMain;

                            strSubBarCode = barcodeObject.getBarcode();

                            setTitleText(getContext().getString(R.string.second_code));
                            setText(strSubBarCode);
                            listener.barCodeType(2);
                            break;
                        case Concatenated:
                            strBarCodeSource = strEditMain;

                            strBarCode = barcodeObject.getMainCode();
                            strSubBarCode = barcodeObject.getSubCode();
                            strSubBarCodeSource = null;//次码设置为空
                            strSubBarCode = null;//次码设置为空
                            setTitleText(getContext().getString(R.string.concatenated_code));
                            listener.barCodeType(3);
                            break;
                    }

                }
                return false;
            }
        });

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setBarCodeEmpty();
                listener.doClose();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(edtContent.getText())) {
                    ivClose.setVisibility(View.GONE);
                } else {
                    ivClose.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void setText(Object content) {
        edtContent.setText(content + "");
    }

    public void setTitleText(Object title) {
        tvTitleName.setText(title + "");
    }

    public void selectAll() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                getFocus();
                edtContent.selectAll();
            }
        }, 100);

    }

    public String getText() {
        return edtContent.getText().toString().trim();
    }

    /**
     * 设置成员变量 为空 但是eidtext还显示的内容
     */
    public void setBarCodeEmpty() {
        setTitleText("主码:");
        selectAll();
        strBarCodeSource = null;
        strSubBarCodeSource = null;
        strBarCode = null;
        strSubBarCode = null;
    }

    /**
     * 选中所有
     */
    public void setSelectAll() {
        setTitleText("主码:");
        selectAll();
    }


    /**
     * editContent获取焦点
     */
    public void getFocus() {
        edtContent.setFocusable(true);
        edtContent.setFocusableInTouchMode(true);
        edtContent.requestFocus();
    }

    public interface AsdfdsListener {
        void barCodeType(int barCodeType);

        void doClose();
    }

    /**
     * 是否有次码
     */
    public boolean isHasSubCode() {
        if (strBarCode != null && strSubBarCode != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 测试上线后关闭
     *
     * @param tv
     */
    private void test(TextView tv) {

        if (IS_RELEASE) return;

        String strTest = null;
        switch (tv.getText().toString()) {
            case "1":
                strTest = "201811190271";
                break;
            case "2":
                strTest = "201811190272";
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
            case "6":
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;
            default:
                strTest = tv.getText().toString();
                edtContent.setText(strTest);
        }
        if (strTest == null) {
            edtContent.setText("");
        } else {
            edtContent.setText(strTest);
        }
    }

    public static BarcodeObject getBarcodeObject(String barCode) {
        BarcodeObject bo = null;
        RefObject<BarcodeObject> barObject = new RefObject<>(bo);
        try {
            BarcodeSimple.Parse(barCode, barObject);
            bo = barObject.argvalue;
        } catch (Exception e) {
            return new BarcodeObject();
        }
        return bo;

    }
}
