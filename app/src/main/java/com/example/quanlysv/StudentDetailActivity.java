package com.example.quanlysv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.Date;

public class StudentDetailActivity extends AppCompatActivity {

    EditText editName,editBirth,editAddress,editMssv;
    RadioButton rMale, rFeMale;
    CheckBox cSport,cTravel,cReadBook;
    Intent intent;
    Button btnCancel,btnSave, btnReset;
    Bundle bundle;

    int status;//=0 nếu là thêm mới sinh viên, =1 xem thông tin chi tiết sinh viên

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        editName=findViewById(R.id.edit_name);
        editBirth=findViewById(R.id.edit_birthday);
        editAddress=findViewById(R.id.edit_address);
        editMssv=findViewById(R.id.edit_mssv);

        rMale=findViewById(R.id.r_male);
        rFeMale=findViewById(R.id.r_feMale);

        cReadBook=findViewById(R.id.c_read_book);
        cSport=findViewById(R.id.c_sport);
        cTravel=findViewById(R.id.c_travel);
        btnSave=findViewById(R.id.btn_save);

        btnReset=findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValue();
            }
        });
        status=0;
        intent=getIntent();
        bundle=intent.getExtras();
        status=bundle.getInt("status");

        if(status==1) {
            showDetail();
            btnSave.setEnabled(false);
            btnReset.setEnabled(false);
        }
        else if(status==2) {
            showDetail();
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun=new Bundle();
                bun.putInt("status",status);//dựa vào status để phân biệt giữa thêm mới hoặc cập nhật trạng thái

                String fullname=editName.getText().toString();
                bun.putString("fullname",fullname);
                String mssv=editMssv.getText().toString();
                bun.putString("mssv",mssv);
                String birthday=editBirth.getText().toString();
                bun.putString("birthday",birthday);
                String address=editAddress.getText().toString();
                bun.putString("address",address);
                int sex=0;
                if(!rMale.isChecked())
                {
                    sex=1;
                }
                bun.putInt("sex",sex);
                String favorites="";
                if(cSport.isChecked())
                {
                    favorites+="sport,";
                }
                if(cTravel.isChecked())
                {
                    favorites+="travel,";
                }
                if(cReadBook.isChecked())
                {
                    favorites+="readbook,";
                }
                bun.putString("favorites",favorites);
                if(status==2) {
                    bun.putInt("id", bundle.getInt("id"));
                    bun.putInt("position",bundle.getInt("position"));
                }
                intent.putExtras(bun);
                setResult(StudentDetailActivity.RESULT_OK,intent);
                finish();
            }
        });
        //intent.putExtras();

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(StudentDetailActivity.RESULT_CANCELED,intent);
                finish();
            }
        });
    }
    public void showDetail()
    {
        int sex=bundle.getInt("sex");
        String fullname=bundle.getString("fullname");
        String mssv=bundle.getString("mssv");
        String birthday=bundle.getString("birthday");
        String address=bundle.getString("address");
        String favorites=bundle.getString("favorites");
        //
        editName.setText(fullname);
        editAddress.setText(address);
        editBirth.setText(birthday);
        editMssv.setText(mssv);
        if(sex==0)
        {
            rMale.setChecked(true);
        }
        else
        {
            rFeMale.setChecked(true);
        }
        if(favorites.contains("sport"))
        {
            cSport.setChecked(true);
        }
        if(favorites.contains("travel"))
        {
            cTravel.setChecked(true);
        }
        if(favorites.contains("readbook"))
        {
            cReadBook.setChecked(true);
        }
    }
    public void resetValue()
    {
        editName.setText("");
        editBirth.setText("");
        editAddress.setText("");
        editMssv.setText("");
        cReadBook.setChecked(false);
        cSport.setChecked(false);
        cTravel.setChecked(false);
        rMale.setChecked(true);
    }
}
