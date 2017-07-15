package com.wanta.mobile.wantaproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanta.mobile.wantaproject.R;
import com.wanta.mobile.wantaproject.activity.ChuanDaLingGanActivity;
import com.wanta.mobile.wantaproject.customview.CustomSimpleDraweeView;
import com.wanta.mobile.wantaproject.domain.ChuanDaLingGanInfo;
import com.wanta.mobile.wantaproject.utils.Constants;

import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 */

public class ChuanDaLingGanRecycviewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ChuanDaLingGanInfo> chuanDaLingGanInfoList;

    public ChuanDaLingGanRecycviewAdapter(Context context, List<ChuanDaLingGanInfo> chuanDaLingGanInfoList) {
        this.context = context;
        this.chuanDaLingGanInfoList = chuanDaLingGanInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_chuandalinggan, null);
        ChuanDaLingGanViewHolder holder = new ChuanDaLingGanViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChuanDaLingGanInfo chuanDaLingGanInfo = chuanDaLingGanInfoList.get(position);
        ((ChuanDaLingGanViewHolder) holder).item_chuandalinggan_draweeview.setWidth(Constants.PHONE_WIDTH/2);
        ((ChuanDaLingGanViewHolder) holder).item_chuandalinggan_draweeview.setHeight(Constants.PHONE_HEIGHT/2);
        ((ChuanDaLingGanViewHolder) holder).item_chuandalinggan_draweeview.setImageURI(Uri.parse("http://1zou.me/images/" + chuanDaLingGanInfo.getImage_path()));
    }

    @Override
    public int getItemCount() {
        return chuanDaLingGanInfoList.size();
    }

    class ChuanDaLingGanViewHolder extends RecyclerView.ViewHolder {

        private final CustomSimpleDraweeView item_chuandalinggan_draweeview;

        public ChuanDaLingGanViewHolder(View itemView) {
            super(itemView);
            item_chuandalinggan_draweeview = (CustomSimpleDraweeView) itemView.findViewById(R.id.item_chuandalinggan_draweeview);
        }
    }
}
