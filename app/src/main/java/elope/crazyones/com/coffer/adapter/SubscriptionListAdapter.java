package elope.crazyones.com.coffer.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import elope.crazyones.com.coffer.R;
import elope.crazyones.com.coffer.model.Subscription;

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListAdapter.MyViewHolder> {

    private OnSubscriptionItemClickListener onsubscriptionItemClickListener;
    private Context mcontext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvSubscriptionName;
        public TextView tvSubscriptionCharges;
        public TextView tvSubscriptionDueDate;
        public ImageView ivSubscriptionIcon;
        public ImageButton ibtCancelSubscription;
        int position = 0;


        public void setPosition(int position) {
            this.position = position;
        }

        public MyViewHolder(View view) {
            super(view);
            ivSubscriptionIcon = (ImageView) view.findViewById(R.id.iv_subscription_icon);
            tvSubscriptionName = (TextView)view.findViewById(R.id.tv_subscription_name);
            tvSubscriptionCharges = (TextView)view.findViewById(R.id.tv_subscription_charges);
            tvSubscriptionDueDate = (TextView)view.findViewById(R.id.tv_subscription_duedate);
            ibtCancelSubscription = (ImageButton)view.findViewById(R.id.ibt_cancel_subscription);
//            ibtCancelSubscription.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onsubscriptionItemClickListener.cancelSubscriptionItemClicked(position);
//                }
//            });
        }

    }

    public List<Subscription> subscriptionList;

    public SubscriptionListAdapter(Context context, OnSubscriptionItemClickListener onSubscriptionItemClickListener) {
        subscriptionList = new ArrayList<Subscription>();
        this.mcontext = context;
        this.onsubscriptionItemClickListener=onSubscriptionItemClickListener;
    }

    @Override
    public SubscriptionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_main_subscription, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Subscription sub = subscriptionList.get(position);
        holder.tvSubscriptionName.setText(sub.getSubscriptionName());
        holder.setPosition(position);
        holder.tvSubscriptionCharges.setText(sub.getSubscriptionCharges());
        holder.tvSubscriptionDueDate.setText(sub.getSubscriptionDueDate());
        Glide.with(mcontext)
                .asBitmap()
                .load(sub.getSubscriptionIconLink())
                .into(holder.ivSubscriptionIcon);
        holder.ibtCancelSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put in app opening
            }
        });
    }

    @Override
    public int getItemCount() {
        if(subscriptionList != null) {
            return subscriptionList.size();
        }
        return 0;
    }

    public void addMindMap(Subscription Sub){
        if(subscriptionList != null){
            subscriptionList.add(Sub);
            notifyDataSetChanged();
        }
    }

    public void replaceMindMapList(ArrayList<Subscription> SubsList){
        if(SubsList != null){
            this.subscriptionList = SubsList;
            notifyDataSetChanged();
        }
    }

    public interface OnSubscriptionItemClickListener {
        public void selectSubscriptionItemClicked(int position);
        public void cancelSubscriptionItemClicked(int position);
    }

}
