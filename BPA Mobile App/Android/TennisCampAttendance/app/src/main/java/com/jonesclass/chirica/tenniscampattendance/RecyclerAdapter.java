package com.jonesclass.chirica.tenniscampattendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Custom class that adds features to the adapter of a RecyclerView.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    //Fields.
    private List<Member> memberList;
    private DatabaseHelper databaseHelper;
    Context context;

    /**
     * Parameterized constructor for this class.
     * @param memberList = List of members.
     * @param databaseHelper = DatabaseHelper, custom class.
     * @param context = Application context.
     */
    public RecyclerAdapter(List<Member> memberList, DatabaseHelper databaseHelper, Context context) {
        this.memberList = memberList;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }

    @NonNull
    @Override
    /**
     * If the RecyclerView is a table, this is what makes the rows..
     */
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_members, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    /**
     * Populates each row in the RecyclerView.
     */
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getFirstName() != null) {
                String name = memberList.get(position).getFirstName() + " "
                        + memberList.get(position).getLastName();
                holder.nameTextView.setText(name);
                holder.activeCheckBox.setChecked(memberList.get(position).isActive());
            }
        }

        //Allows for users to update member information if they click on where the information is displayed.
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member editMember = memberList.get(holder.getAdapterPosition());
                Intent editIntent = new Intent(context, EditMemberActivity.class);
                editIntent.putExtra("com.jonesclass.chirica.roster.EDIT_MEMBER", editMember);
                context.startActivity(editIntent);
            }
        });

        //Updating member's active status.
        holder.activeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Member editMember = memberList.get(holder.getAdapterPosition());
                editMember.setActive(holder.activeCheckBox.isChecked());
                databaseHelper.updateMember(editMember);
            }
        });
    }

    @Override
    /**
     * Returns how many members there are.
     */
    public int getItemCount() {
        return memberList.size();
    }

    /**
     * Inner class, where basic information about each member is displayed.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private CheckBox activeCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_name);
            activeCheckBox = itemView.findViewById(R.id.checkBox_active);
        }
    }
}
