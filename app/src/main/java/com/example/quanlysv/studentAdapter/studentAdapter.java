package com.example.quanlysv.studentAdapter;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysv.R;
import com.example.quanlysv.model.Student;

import java.util.ArrayList;
import java.util.List;

public class studentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Student> allItem;
    List<Student> displayItem;
    String keyword;

    public studentAdapter(List<Student> items) {
        this.allItem=items;//lấy địa chỉ của danh sách items trong MainActivity luôn -> khi thay đổi allItem thì MainAc cũng thay đổi theo
        this.displayItem = new ArrayList<>();
        this.keyword="";
        this.displayItem.addAll(items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_layout,parent,false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Student student=displayItem.get(position);
        itemViewHolder viewHolder=(itemViewHolder)holder;
        viewHolder.img_icon.setText("H");
        viewHolder.txt_birthday.setText(student.getBirthday());
        viewHolder.txt_mssv.setText(student.getMssv());
        viewHolder.txt_name.setText(student.getFullname());
    }

    @Override
    public int getItemCount() {
        return displayItem.size();
    }

    class itemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView img_icon,txt_name,txt_birthday,txt_mssv;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon=itemView.findViewById(R.id.img_icon);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_birthday=itemView.findViewById(R.id.txt_birthday);
            txt_mssv=itemView.findViewById(R.id.txt_mssv);

            //
            RelativeLayout layout_item=itemView.findViewById(R.id.layout_item);
            layout_item.setLongClickable(true);// khi click hoặc giữ 1 lúc
            layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            layout_item.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Student student=displayItem.get(this.getAdapterPosition());
            menu.setHeaderTitle("Quản lý sv");
            menu.add(this.getAdapterPosition(),0,0,"Thông tin chi tiết");
            menu.add(this.getAdapterPosition(),1,0,"Cập nhật");
            menu.add(this.getAdapterPosition(),2,0,"Xóa");
        }
    }
    public void removeSV(int pos)
    {
        Student student=displayItem.get(pos);
        allItem.remove(student);
        displayItem.remove(pos);
        notifyItemRemoved(pos);
    }
    public void addSv(Student student)
    {
        displayItem.add(student);
        allItem.add(student);
        notifyItemInserted(displayItem.size()-1);
    }
    public void changeSV(Student student, int pos)
    {
        Student s=displayItem.get(pos);
        s.setMssv(student.getMssv());
        s.setFullname(student.getFullname());
        s.setImage(student.getImage());
        s.setAddress(student.getAddress());
        s.setBirthday(student.getBirthday());
        s.setSex(student.getSex());
        s.setFavorites(student.getFavorites());
        notifyItemChanged(pos,student);
    }
    public Student getItem(int pos)
    {
        return displayItem.get(pos);
    }
    public void showAll()
    {
        displayItem.clear();
        displayItem.addAll(allItem);
        notifyDataSetChanged();
    }
    public void search(String keyword)
    {
        this.keyword=keyword;
        displayItem.clear();
        //Log.v("TAG",keyword);
        for(Student student: allItem)
        {
            if(student.getMssv().contains(keyword.trim())|| student.getFullname().contains(keyword.trim()))
            {
                displayItem.add(student);
            }
        }
        notifyDataSetChanged();
    }

}
