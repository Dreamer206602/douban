package com.exam.douban.adapter;

import java.util.List;
import java.util.zip.Inflater;

/**
 * �Զ���������
 */
import com.exam.douban.activity.MovieData;
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
	public MovieAdapter(Context context,List<MovieData> movieList) {
		this.context = context;
		ml = movieList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
	 */
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.search_row, null);
		TextView title = (TextView) convertView.findViewById(R.id.tv_row_search_title);
		TextView rating = (TextView) convertView.findViewById(R.id.tv_row_search_rating);
		TextView year = (TextView) convertView.findViewById(R.id.tv_row_searc_date);
		ImageView cover = (ImageView) convertView.findViewById(R.id.img_row_search);

		title.setText(ml.get(position).getmTitle());
		rating.setText(ml.get(position).getmRating());
		year.setText(ml.get(position).getmYear());
		cover.setImageBitmap(ml.get(position).getmImgSmall());
		System.out.println("--------");
//		cover.setImageBitmap(ml.get(position).getmImg());
		return null;
	}

}
