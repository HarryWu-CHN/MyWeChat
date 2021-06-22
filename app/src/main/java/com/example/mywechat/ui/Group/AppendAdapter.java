package com.example.mywechat.ui.Group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.Contact;

import java.util.ArrayList;

public class AppendAdapter extends RecyclerView.Adapter<AppendAdapter.AppendHolder> {
    private ArrayList<Contact> members;

    private AppendAdapter.onRecyclerItemClickerListener listener;

    public AppendAdapter() {
        members = new ArrayList<>();
        members.add(new Contact(null, null));
    }

    public void setMembers(ArrayList<Contact> members) {
        this.members = members;
        this.members.add(new Contact(null, null));
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AppendAdapter.onRecyclerItemClickerListener listener) {
        this.listener = listener;
    }

    private View.OnClickListener getOnClickListener(final int position) {
        return v -> listener.onRecyclerItemClick(v, position);
    }

    public interface onRecyclerItemClickerListener {
        void onRecyclerItemClick(View view, int position);
    }

    @NonNull
    @Override
    public AppendAdapter.AppendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_invite_member, parent, false);
        return new AppendHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppendAdapter.AppendHolder holder, int position) {
        Contact member = this.members.get(position);
        holder.getMemberNameView().setText(member.getNickName());
        ImageView memberView = holder.getMemberImageView();
        if (member.getNickName() == null) {
            memberView.setImageResource(R.drawable.icon_plus_rect);
        } else {
            memberView.setImageResource(R.drawable.avatar1);
        }
        memberView.setOnClickListener(getOnClickListener(position));
    }

    @Override
    public int getItemCount() {
        return this.members.size();
    }

    public static class AppendHolder extends RecyclerView.ViewHolder {
        private final TextView memberNameView;
        private final ImageView memberImageView;

        public AppendHolder(@NonNull View itemView) {
            super(itemView);
            this.memberNameView = itemView.findViewById(R.id.memberName);
            this.memberImageView = itemView.findViewById(R.id.memberImageView);
        }

        public TextView getMemberNameView() {
            return memberNameView;
        }

        public ImageView getMemberImageView() {
            return memberImageView;
        }
    }
}
