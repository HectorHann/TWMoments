package com.han.moments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements XListView.IXListViewListener {
private ArrayList<Map<String, Object>> mlList,mmList,list;
private Context context;
private PopupWindow pw;
private RelativeLayout rl;
private MyAdapter adapter;
private XListView mListView;
private ListView lv_hf;
private MyHFAdapter myHFAdapter;
private HolderView holder;
private boolean flag=false;//�ж��Ƿ�ظ�ĳ��
//����ظ�ĳ�����ݵ�λ��
private int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findView();
        addData();
        loadData();
    }


	private void findView() {
		// TODO Auto-generated method stub
    	context=this;
    	//�ظ��б�����
    	mlList=new ArrayList<Map<String,Object>>();
    	mListView=(XListView) findViewById(R.id.lv);
    	rl=(RelativeLayout) findViewById(R.id.rl);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		lv_hf=(ListView) findViewById(R.id.lv_hf);
	}


	private void addPop() {
		// TODO Auto-generated method stub
		pw=new PopupWindow(context);
		pw.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		pw.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		pw.setContentView(View.inflate(context, R.layout.ll_bottom, null));
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
	}


	private void addData() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<5;i++){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("name", "we΢Ц"+i);
			map.put("content", "num"+i);
			mmList=new ArrayList<Map<String,Object>>();
			for(int j=0;j<3;j++){
				Map<String, Object> mp=new HashMap<String, Object>();
				mp.put("name", "we΢Ц"+i);
				mp.put("content", ": hello"+j);
				mmList.add(mp);
			}
			map.put("replys", mmList);
			mlList.add(map);
		}
		Log.i("test", ":"+mmList.size());
	}


	private void loadData() {
		// TODO Auto-generated method stub
		adapter=new MyAdapter();
		mListView.setAdapter(adapter);
	}



    /**
     * A placeholder fragment containing a simple view.
     */
public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mlList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mlList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(final int position, View convertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			holder=new HolderView();
			if(convertview==null){
				convertview=View.inflate(context, R.layout.tiezi_item, null);
				holder.tv_name=(TextView) convertview.findViewById(R.id.tv_name);
				holder.tv_hf=(TextView) convertview.findViewById(R.id.tv_hf);
				holder.tv_content=(TextView) convertview.findViewById(R.id.tv_content);
				holder.lv_hf=(ListView) convertview.findViewById(R.id.lv_hf);
				convertview.setTag(holder);
			}else{
				holder=(HolderView) convertview.getTag();
			}
			holder.tv_name.setText(mlList.get(position).get("name").toString());
			if(!"".equals(mlList.get(position).get("content").toString())){
				holder.tv_content.setVisibility(View.VISIBLE);
				holder.tv_content.setText(mlList.get(position).get("content").toString());
			}
			 list=(ArrayList<Map<String, Object>>) mlList.get(position).get("replys");
				myHFAdapter=new MyHFAdapter();
				holder.lv_hf.setAdapter(myHFAdapter);
				setListViewHeightBasedOnChildren(holder.lv_hf);
				myHFAdapter.notifyDataSetChanged();
				Log.i("test", ""+list.size());
			holder.tv_hf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pos=position;
					flag=true;
					Toast.makeText(context, pos+"", Toast.LENGTH_SHORT).show();
				}
			});
			return convertview;
		}
	
}private class HolderView{
	private TextView tv_name,tv_hf,tv_content;
	private ListView lv_hf;
}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
				
				mlList.clear();
				addData();
				adapter.notifyDataSetChanged();
				onLoad();
		
	}


	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
				// TODO Auto-generated method stub
				addData();
				adapter.notifyDataSetChanged();
				onLoad();
		
		
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("�ո�");
	}
	
	class MyHFAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View contentview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			HolderView1 view1=new HolderView1();
			if(contentview==null){
				contentview=View.inflate(context, R.layout.hf_item, null);
				view1.tv_name1=(TextView) contentview.findViewById(R.id.tv_name1);
				view1.tv_content1=(TextView) contentview.findViewById(R.id.tv_content1);
				contentview.setTag(view1);
			}else{
				view1=(HolderView1) contentview.getTag();
			}
			view1.tv_name1.setText(list.get(arg0).get("name").toString());
			view1.tv_content1.setText(list.get(arg0).get("content").toString());
			return contentview;
		}
		
		class HolderView1{
			private TextView tv_name1,tv_content1;
		}	
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {  
        //��ȡListView��Ӧ��Adapter  
    ListAdapter listAdapter = listView.getAdapter();  
    if (listAdapter == null) {  
        // pre-condition  
        return;  
    }  

    int totalHeight = 0;  
    for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()�������������Ŀ  
        View listItem = listAdapter.getView(i, null, listView);  
        listItem.measure(0, 0);  //��������View �Ŀ��  
        totalHeight += listItem.getMeasuredHeight();  //ͳ������������ܸ߶�  
    }  

    ViewGroup.LayoutParams params = listView.getLayoutParams();  
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
    //listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�  
    //params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�  
    listView.setLayoutParams(params);  
}  
}
