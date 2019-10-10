package com.example.evga7.hakseek2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Intent Pop_intent;
    Intent Charge_intent;
    Intent Payment_intent;
    int Sum=0;
    int Amoney=0;
    TextView Total_Sum;
    TextView Account_money;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mDate;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Total_Sum=(TextView)findViewById(R.id.Total_Sum);
        Account_money=(TextView)findViewById(R.id.Account_money);
        mListView = (ListView) findViewById(R.id.Menu_list);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        Pop_intent = new Intent(this,PopActivity.class);
        Charge_intent = new Intent(this,charge_account.class);
        Payment_intent=new Intent(this,Payment.class);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.remove(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Request",Integer.toString(requestCode));
        Log.e("resultdoe",Integer.toString(resultCode));
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String Price=data.getStringExtra("price");
                String result = data.getStringExtra("data");
                String Cnt = data.getStringExtra("cnt");
                int temp=(Integer.parseInt(Price)*Integer.parseInt(Cnt));
                Sum+=temp;
                byte[] arr = data.getByteArrayExtra("imgg");
                Bitmap bitmap = (Bitmap) BitmapFactory.decodeByteArray(arr,0,arr.length);
                Drawable drawable = new BitmapDrawable(bitmap);
                long now =System.currentTimeMillis();
                Date date=new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String getTime = sdf.format(date);
                mAdapter.addItem(drawable,result+" "+Cnt+"개",getTime,temp);
                dataChange();
                Total_Sum.setText("금액 : "+Sum);
            }
            else if (resultCode==-2)
            {
                String money=data.getStringExtra("money");
                Account_money.setText(money);
            }
            else if (resultCode==-4)
            {
                String money=data.getStringExtra("money");
                Account_money.setText(money);
                while(mAdapter.getCount()!=0)
                    mAdapter.remove(0);
                Sum=0;
                Total_Sum.setText("금액 : "+Sum);
            }
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);}


        @Override
        public long getItemId(int position) {
            return position;
        }
        public void addItem(Drawable icon, String mTitle, String mDate,int Price) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;
            addInfo.mDate = mDate;
            addInfo.Price=Price;
            mListData.add(addInfo);
        }
        public void remove(int position){
            ListData temp =null;
            temp=mListData.get(position);
            Sum-=temp.Price;
            mListData.remove(position);
            Total_Sum.setText("금액 : "+Sum);
            dataChange();
        }
        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.menu_list_view, null);

                holder.mIcon = (ImageView) convertView.findViewById(R.id.mImage);
                holder.mText = (TextView) convertView.findViewById(R.id.mText);
                holder.mDate = (TextView) convertView.findViewById(R.id.mDate);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);

            if (mData.mIcon != null) {
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }

            holder.mText.setText(mData.mTitle);
            holder.mDate.setText(mData.mDate);

            return convertView;
        }
    }

    public void dataChange(){
        mAdapter.notifyDataSetChanged();
    }
    public void M1_1Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.textView2);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o_2800);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);

    }

    public void M1_2Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.textView3);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dak_4000);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void M1_3Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.textView4);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_saetr);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }

    public void M2_1Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M2_T1);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s_o_3800);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void M2_2Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M2_T2);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.g_o_3800);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void M2_3Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M2_T3);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h_o_4000);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }



    public void M3_1Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M2_T3);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.don_o_4500);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void M3_2Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M3_T2);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bul_o_4500);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void M3_3Click(View v)
    {
        TextView txt =(TextView)findViewById(R.id.M3_T3);
        Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c_b_4300);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Pop_intent.putExtra("data", txt.getText().toString());
        Pop_intent.putExtra("img",byteArray);
        startActivityForResult(Pop_intent,1);
    }
    public void Charge_Account(View v)
    {
        TextView txt = (TextView) findViewById(R.id.Account_money);
        Charge_intent.putExtra("money",txt.getText().toString());
        startActivityForResult(Charge_intent,1);
    }
    public void Payment(View v)
    {
        TextView txt = (TextView) findViewById(R.id.Account_money);
        Amoney=Integer.parseInt(txt.getText().toString());
        Log.e("돈",Integer.toString(Amoney));
        if (Sum>=Amoney)
        {
            Payment_intent.putExtra("money", "9");
            Toast.makeText(this,"잔액이부족합니다",Toast.LENGTH_SHORT);
        }
        else {
            Payment_intent.putExtra("money", Integer.toString(Sum));
            Payment_intent.putExtra("Amoney", txt.getText().toString());
            startActivityForResult(Payment_intent,1);
        }
    }

}



