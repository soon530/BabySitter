package tw.tasker.babysitter.presenter.adapter;

import tw.tasker.babysitter.R;
import tw.tasker.babysitter.R.drawable;
import tw.tasker.babysitter.R.id;
import tw.tasker.babysitter.R.layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater myInflater;
	CharSequence[] title = null;
	CharSequence[] info = null;

	public MyAdapter(LayoutInflater layoutInflater, CharSequence[] title,
			CharSequence[] info) {
		myInflater = layoutInflater;
		this.title = title;
		this.info = info;
	}

	@Override
	public int getCount() {
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		return title[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 自訂類別，表達個別listItem中的view物件集合。
		ViewTag viewTag;

		if (convertView == null) {
			// 取得listItem容器 view
			convertView = myInflater.inflate(R.layout.list_item_card, null);

			// 建構listItem內容view
			viewTag = new ViewTag(
					(ImageView) convertView
							.findViewById(R.id.babysitter_avator),
					(TextView) convertView
							.findViewById(R.id.babysitter_name),
					(TextView) convertView
							.findViewById(R.id.babysitter_address));

			// 設置容器內容
			convertView.setTag(viewTag);
		} else {
			viewTag = (ViewTag) convertView.getTag();
		}

		// 設定內容圖案
		//switch (position) {
		//case 0:
			viewTag.icon.setBackgroundResource(R.drawable.ic_launcher);
		// break;
		// case 1:
		// viewTag.icon.setBackgroundResource(R.drawable.ic_launcher);
		// break;
		// case 2:
		// viewTag.icon.setBackgroundResource(R.drawable.ic_launcher);
		// break;
		// }

		// 設定標題文字
		viewTag.title.setText(title[position]);
		// 設定內容文字
		viewTag.info.setText(info[position]);

		return convertView;
	}

	// 自訂類別，表達個別listItem中的view物件集合。
	class ViewTag {
		ImageView icon;
		TextView title;
		TextView info;

		public ViewTag(ImageView icon, TextView title, TextView info) {
			this.icon = icon;
			this.title = title;
			this.info = info;
		}
	}

}
