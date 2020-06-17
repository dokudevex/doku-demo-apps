package com.doku.android.samplesdkapps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.doku.android.samplesdkapps.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dedyeirawan on 26,May,2020
 */
public class PaymentPageAdapter extends ExpandableRecyclerAdapter<PaymentPageAdapter.MenuItem> {
    public static final int TYPE_MENU = 1001;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public PaymentPageAdapter(Context context) {
        super(context);
        this.context = context;
        setItems(getItems());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String childTitle);
    }

    public static class MenuItem extends ExpandableRecyclerAdapter.ListItem {
        public String titleGroup;
        public Integer logoIdVal;

        public MenuItem(String group,Integer logoId) {
            super(TYPE_HEADER);

            titleGroup = group;
            logoIdVal =logoId;
        }

        public MenuItem(String first, String last,Integer logoId) {
            super(TYPE_MENU);

            titleGroup = first + " " + last;
            logoIdVal =logoId;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView textviewMenuToptitle;
        ImageView imageviewMenuLogo;
        Integer logoId =null;

        public HeaderViewHolder(View view) {
            super(view, view.findViewById(R.id.imageview_menu_arrow), view.findViewById(R.id.textview_menu_toptitle));
            textviewMenuToptitle = view.findViewById(R.id.textview_menu_toptitle);
            imageviewMenuLogo = view.findViewById(R.id.imageview_menu_logo);
        }

        public void bind(int position) {
            super.bind(position);
            textviewMenuToptitle.setText(visibleItems.get(position).titleGroup);
            logoId = visibleItems.get(position).logoIdVal;
            imageviewMenuLogo.setBackgroundResource(logoId);
        }
    }

    public class MenuViewHolder extends ExpandableRecyclerAdapter.ViewHolder implements View.OnClickListener {
        TextView textviewContentTitle;
        ImageView imageviewContentLogo;
        Integer logoId =null;
        ConstraintLayout constraintlayoutMenuContent;

        public MenuViewHolder(View view) {
            super(view);

            textviewContentTitle = view.findViewById(R.id.textview_content_title);
            imageviewContentLogo = view.findViewById(R.id.imageview_content_logo);
            constraintlayoutMenuContent = view.findViewById(R.id.constraintlayout_menu_content);
            itemView.setOnClickListener(this);
        }

        public void bind(final int position) {
            logoId = visibleItems.get(position).logoIdVal;
            imageviewContentLogo.setBackgroundResource(logoId);
            textviewContentTitle.setText(visibleItems.get(position).titleGroup);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, visibleItems.get(getLayoutPosition()).titleGroup);
            }
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.paymentpage_header_adapter, parent));
            case TYPE_MENU:
            default:
                return new MenuViewHolder(inflate(R.layout.paymentpage_content_adapter, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_MENU:
            default:
                ((MenuViewHolder) holder).bind(position);
                break;
        }
    }

    private List<MenuItem> getItems() {
        List<MenuItem> items = new ArrayList<>();

        items.add(new MenuItem(context.getResources().getString(R.string.credit_card),R.mipmap.icon_cc));
        items.add(new MenuItem(context.getResources().getString(R.string.emoney),R.mipmap.icon_emoney));
        items.add(new MenuItem(context.getResources().getString(R.string.virtual_account),R.mipmap.icon_transfer));
        items.add(new MenuItem(context.getResources().getString(R.string.bank),context.getResources().getString(R.string.bank_mandiri),R.drawable.logo_mandiri));
        items.add(new MenuItem(context.getResources().getString(R.string.bank) ,context.getResources().getString(R.string.bank_mandiri_syariah),R.drawable.logo_mandiri_syariah));
        items.add(new MenuItem(context.getResources().getString(R.string.convenience_store),R.mipmap.icon_store));
        items.add(new MenuItem(context.getResources().getString(R.string.internet_banking),R.mipmap.icon_ib));
        items.add(new MenuItem(context.getResources().getString(R.string.personal_finance),R.mipmap.icon_personal));

        return items;
    }
}
