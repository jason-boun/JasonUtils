package com.jason.jasonutils.gridview.complex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.jasonutils.R;
import com.jason.jasonutils.gridview.singleline.Person;
import com.jason.jasonutils.gridview.singleline.PersonDataUtil;

public class GridViewComplexDemo extends Activity {
	
	private boolean isSuperAdmin = true;
	private boolean DeleteMode = false;
	private MyGridView groupGridVeiw;
	private List<Person> groupMemberList ;
	private GridViewAdapter myAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_complexclick_demo_activity);
		init();
	}
	
	private void init() {
		groupMemberList = new ArrayList<Person>();
		groupMemberList.addAll(PersonDataUtil.dataFactory(10,0));
		groupGridVeiw = (MyGridView) findViewById(R.id.mygv_complex_demo_show);
		myAdapter = new GridViewAdapter(this, groupMemberList);
		groupGridVeiw.setAdapter(myAdapter);
		operateGridViewItem();
	}

	/**
	 * GridView点击事件
	 */
	private void operateGridViewItem(){
		//处理gridView的非添加和删除条目子控件的点击事件
		groupGridVeiw.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == groupMemberList.size()){
					if(DeleteMode){
						DeleteMode = false;
					}else{
						//"+"——添加成员
						Intent intentData = new Intent(GridViewComplexDemo.this, PersonListActivity.class)
						.putExtra(PersonListActivity.LIST_PRE_SIZE, groupMemberList.size())
						.putExtra(PersonDataUtil.INTENT_FROM_TYPE, PersonDataUtil.INTENT_FROM_COMPLEX_GRIDVIEW);
						startActivityForResult(intentData, 0);
					}
				}else if(position == groupMemberList.size()+1){
					if(groupMemberList.size()==0){
						DeleteMode =false;
					}else{
						DeleteMode = DeleteMode?false:true;//显示删除联系人图标
					}
					myAdapter.notifyDataSetChanged();
				}else if(position < groupMemberList.size()){
					final Object obj = groupGridVeiw.getItemAtPosition(position);
					if(obj !=null && obj instanceof Person){
						Person delPerson = (Person) obj;
						if(DeleteMode){
							if("123456".equals(delPerson.getJid())){
								//本人头像
								Toast.makeText(GridViewComplexDemo.this, "不能删除自己", Toast.LENGTH_SHORT).show();
								DeleteMode = false;
							}else{
								groupMemberList.remove(delPerson);
								DeleteMode = false;
							}
							myAdapter.notifyDataSetChanged();
						}else{
							if(!TextUtils.isEmpty(delPerson.getJid())&& !TextUtils.isEmpty(delPerson.getNickname())){
								//跳转到名片页面
								Toast.makeText(GridViewComplexDemo.this, "跳转到该联系人对应的名片页面", Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
			}
		});
		
		//处理gridView中添加条目和删除条目以及非条目区域的响应事件
		groupGridVeiw.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    GridView gv = (GridView)v;
                    int childSize = gv.getChildCount();
                    for(int i = 0; i < childSize; i++){
                        View child = gv.getChildAt(i);
                        Rect r = new Rect();
                        child.getGlobalVisibleRect(r);
                        if(rawX > r.left && rawX < r.right && rawY > r.top && rawY < r.bottom){
                        	 if("+".equals(child.getTag()) || "-".equals(child.getTag())) {
                        		 if(DeleteMode){
                                 	DeleteMode = false;
                                 	myAdapter.notifyDataSetChanged();
                                 }
                             }
                            return false;
                        }
                    }
                    if(DeleteMode){
                    	DeleteMode = false;
                    	myAdapter.notifyDataSetChanged();
                    }
                    return false; 
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && data!=null){
			@SuppressWarnings("unchecked")
			List<Person> addList = (List<Person>) data.getSerializableExtra(PersonListActivity.ADD_PERSON_SESULT_KEY);
			groupMemberList.addAll(addList);
			myAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * GridView适配器
	 */
	public class GridViewAdapter extends BaseAdapter{
		Context context;
		List<Person> GroupMemberList ;
        
		GridViewAdapter(Context context,List<Person> GroupMemberList){
			this.context = context;
			this.GroupMemberList = GroupMemberList;
		}
		
		@Override
		public int getCount() {
			if(isSuperAdmin){
				return GroupMemberList.size()+2;
			}else{
				return GroupMemberList.size()+1;
			}
		}

		@Override
		public Object getItem(int position) {
			if(position<GroupMemberList.size()){
				return GroupMemberList.get(position);
			}else{
				return "";
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View personView = View.inflate(context, R.layout.gridview_complex_demo_item, null);
			TextView tv_delete_icon = (TextView) personView.findViewById(R.id.tv_delete_icon);
			TextView tv_groupmember_name = (TextView) personView.findViewById(R.id.tv_groupmember_name);
			ImageView iv_groupmember_photo = (ImageView) personView.findViewById(R.id.iv_groupmember_photo);
			
			if(DeleteMode){
				tv_delete_icon.setVisibility(View.VISIBLE);
			}else{
				tv_delete_icon.setVisibility(View.INVISIBLE);
			}
			if(position<GroupMemberList.size()){
				Person person = GroupMemberList.get(position);
				if(person != null){
					if(!TextUtils.isEmpty(person.getNickname())){
						tv_groupmember_name.setText(person.getNickname());
					}else if(!TextUtils.isEmpty(person.getName())){
						tv_groupmember_name.setText(person.getName());
					}
					byte[] photo = person.getPhoto();
					if(photo != null){
						ByteArrayInputStream bais = new ByteArrayInputStream(photo);
						iv_groupmember_photo.setImageBitmap(PersonDataUtil.getRoundedCornerBitmap(BitmapFactory.decodeStream(bais), 10, 10));
					}else{
						Bitmap btmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.use_bg_dimen);
						iv_groupmember_photo.setImageBitmap(PersonDataUtil.getRoundedCornerBitmap(btmap,10,10));
					}
				}
			}else if(position ==GroupMemberList.size()){
				personView.setTag("+");
				if(DeleteMode){
					personView.setVisibility(View.INVISIBLE);
				}else{
					tv_groupmember_name.setText("");
					Bitmap btmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.add_groupchat_member_pic);
					iv_groupmember_photo.setImageBitmap(PersonDataUtil.getRoundedCornerBitmap(btmap,10,10));
				}
			}else if(position ==GroupMemberList.size()+1){
				personView.setTag("-");
				if(DeleteMode){
					personView.setVisibility(View.INVISIBLE);
				}else{
					tv_groupmember_name.setText("");
					Bitmap btmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete_groupchat_member_pic);
					iv_groupmember_photo.setImageBitmap(PersonDataUtil.getRoundedCornerBitmap(btmap,10,10));
				}
			}
			return personView;
		}
	}
	
	/**
	 * 头像byte[]数据
	 * @param context
	 * @return
	 */
	public static byte[] getPhotoData(Context context){
		Bitmap btmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete_groupchat_member_pic);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		btmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] result = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
