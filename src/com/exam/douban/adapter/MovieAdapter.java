package com.exam.douban.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.exam.douban.activity.HistoryActivity;
/**
 * �Զ���������
 */
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {
	private Context context;
	private List<MovieData> ml;
	private List<PersonData> pl;
	private boolean busy = true;
	private String info;
	
	public MovieAdapter(Context context,List<MovieData> movieList) {
		this.context = context;
		ml = movieList;
	}

	public MovieAdapter(HistoryActivity context2, List<PersonData> personList) {
		// TODO Auto-generated constructor stub
		this.context = context2;
		pl = personList;
	}

	@Override
	public int getCount() {
		if(ml!=null)
		return ml.size();
		else if(pl!=null)
			return pl.size();
		return 0;
	}
	/**
	 * ���ּ������flag
	 * @param busy
	 */
	public void setFlagBusy(boolean busy) {
		this.busy = busy;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	/**
	 * ��������õ�����<MovieData>��ʽ���б�������search_row.xml�ķ�ʽ������activity_main.xml
	 * �õ������ļ�����search_row.xml������һ��id����������ҵ�
	 * ���ﲻ����setContentView(R.layout.activity_main)�õ�����
	 * ��inflate�����е�һ�������ж�Ӧ���࣬�ڶ���������Ҫ��null����Ȼ�޷���ʾ��
	 * @vonvertView ������ʾ�����view��
	 * @position ������Ԫ�ص�λ��
	 * ����ͼƬ��û�м�⻺�棬ÿ�ζ�����������
	 */
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		convertView = LayoutInflater.from(context).inflate(R.layout.search_row,null);
		TextView title = (TextView) convertView.findViewById(R.id.tv_row_search);
		ImageView cover = (ImageView) convertView.findViewById(R.id.img_row_search);
		if(ml!=null){
			String t = ml.get(position).getTitle();
			String y = ml.get(position).getRating();
			String c = ml.get(position).getYear();
			title.setText(t+"\n"+y+"\n"+c);
			cover.setImageBitmap(ml.get(position).getImg());
		}
		if(pl!=null){
			String t = pl.get(position).getName();
			String y = pl.get(position).getName_en();
			String c = pl.get(position).getBorn_place();
			title.setText(t+"\n"+y+"\n"+"�����أ�"+c);
			cover.setImageBitmap(pl.get(position).getImg());
		}
		
		
		return convertView;
	}
	
	public String getInfo(){
		return info;
	}
	public void setInfo(String s){
		info = s;
	}
}
