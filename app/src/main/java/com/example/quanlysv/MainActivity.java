package com.example.quanlysv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysv.model.Student;
import com.example.quanlysv.studentAdapter.studentAdapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    studentAdapter adapter;
    SearchView searchView;
    List<Student> items;
    MyDataBaseQLSV myDataBaseQLSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        myDataBaseQLSV=new MyDataBaseQLSV(this);
        items=myDataBaseQLSV.getAllStudent();

        RecyclerView recyclerView=findViewById(R.id.layout_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new studentAdapter(items);
        recyclerView.setAdapter(adapter);


    }


    //option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        searchView=(SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>1)
                {
                    adapter.search(newText);
                    Log.v("TAG",String.valueOf(adapter.getItemCount()));
                }
                else
                {
                    adapter.showAll();
                    Log.v("TAG",String.valueOf(adapter.getItemCount()));
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_search)
        {

        }
        else if(id==R.id.action_add_student)
        {
            Intent intent=new Intent(MainActivity.this,StudentDetailActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("status",0);
            intent.putExtras(bundle);
            startActivityForResult(intent,1111);

        }
        return super.onOptionsItemSelected(item);
    }
    //end option menu

    //context menu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case 0: //Thông tin chi tiết
                showDetailStudent(item.getGroupId(),1);
                return true;
            case 1: //Cập nhật
                showDetailStudent(item.getGroupId(),2);
                return true;
            case 2: //Xóa
                showAlertDialogDelete(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void showAlertDialogDelete(final int pos)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        AlertDialog dialog=builder.setTitle("Xóa sinh viên")
                .setMessage("Bạn có chắc muốn xóa?")
                .setNegativeButton("No",null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int c=myDataBaseQLSV.deleteStudent(adapter.getItem(pos));
                        if(c>0) {//xóa item
                            adapter.removeSV(pos);
                            Toast toast = Toast.makeText(MainActivity.this, "delete success", Toast.LENGTH_LONG);
                            toast.setMargin(0, 0);
                            toast.show();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(MainActivity.this, "delete False", Toast.LENGTH_LONG);
                            toast.setMargin(0, 0);
                            toast.show();
                        }
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    //

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                //tại đây sẽ insert dữ liệu vào database
                Bundle bundle=data.getExtras();
                int status=bundle.getInt("status");
                String image;
                String fullname=bundle.getString("fullname");
                String mssv=bundle.getString("mssv");
                String birthday=bundle.getString("birthday");
                String address=bundle.getString("address");
                int sex=bundle.getInt("sex");
                String favorites=bundle.getString("favorites");

                Student student=new Student();
                student.setFavorites(favorites);
                student.setBirthday(birthday);
                student.setMssv(mssv);
                student.setFullname(fullname);
                student.setSex(sex);
                student.setAddress(address);

                if(status==0) {
                    //thêm mới
                    Long id=myDataBaseQLSV.addStudent(student);
                    if(id>0) {
                        student.setId(Math.toIntExact(id));
                        adapter.addSv(student);
                        Toast toast = Toast.makeText(MainActivity.this, "Add Oki", Toast.LENGTH_LONG);
                        toast.setMargin(0, 0);
                        toast.show();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(MainActivity.this, "Add False", Toast.LENGTH_LONG);
                        toast.setMargin(0, 0);
                        toast.show();
                    }
                }
                else if(status==2)
                {
                    //cập nhật
                    student.setId(bundle.getInt("id"));
                    int pos=bundle.getInt("position");
                    int c=myDataBaseQLSV.updateStudent(student);
                    if(c>0) {
                        adapter.changeSV(student,pos);
                        Toast toast = Toast.makeText(MainActivity.this, "update Oki", Toast.LENGTH_LONG);
                        toast.setMargin(0, 0);
                        toast.show();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(MainActivity.this, "update False", Toast.LENGTH_LONG);
                        toast.setMargin(0, 0);
                        toast.show();
                    }
                }
            }
            else if(resultCode==Activity.RESULT_CANCELED)
            {
                Toast toast=Toast.makeText(MainActivity.this,"Cancel",Toast.LENGTH_LONG);
                toast.setMargin(0,0);
                toast.show();
            }
        }
    }
    public void showDetailStudent(int pos,int stt)
    {
        Student student=adapter.getItem(pos);

        Bundle bundle=new Bundle();
        bundle.putInt("position",pos);
        bundle.putInt("status",stt);
        bundle.putInt("id",student.getId());
        bundle.putString("fullname",student.getFullname());
        bundle.putString("mssv",student.getMssv());
        bundle.putString("address",student.getAddress());
        bundle.putInt("sex",student.getSex());
        bundle.putString("birthday",student.getBirthday());
        bundle.putString("favorites",student.getFavorites());

        Intent intent=new Intent(MainActivity.this,StudentDetailActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1111);
    }
}
