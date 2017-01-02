package com.a.listviewdemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"ArrayAdapter");
        menu.add(0,2,0,"SimpleCursorAdapter");
        menu.add(0,3,0,"SimpleAdapter");
        menu.add(0,4,0,"BaseAdapter");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                arrayAdapter();
                break;
            case 2:
                simpleCursorAdapter();
                break;
            case 3:
                simpleAdapter();
                break;
            case 4:
                baseAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void arrayAdapter(){
        final String[] weeks = {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
        listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,weeks));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,weeks[position],Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void simpleCursorAdapter(){
        final Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        startManagingCursor(cursor);
        listView.setAdapter(new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},

                new int[]{android.R.id.text1}
        ));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        MainActivity.this,
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    public void simpleAdapter(){
        final List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("title","G1");
        map.put("info","google1");
        map.put("img",R.drawable.icon1);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G2");
        map.put("info","google2");
        map.put("img",R.drawable.icon2);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G3");
        map.put("info","google");
        map.put("img",R.drawable.icon3);
        list.add(map);

        listView.setAdapter(new SimpleAdapter(
                this,
                list,
                R.layout.simpleadapter,
                new String[] {"img","title","info"},
                new int[] {R.id.imgView,R.id.titleView,R.id.infoView}
        ));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        list.get(position).get("title").toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    static class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
        public Button button;
        public LinearLayout layout;
    }
    public void baseAdapter(){
        class MyBaseAdapter extends BaseAdapter{
            private List<Map<String,Object>> data;
            private Context context;
            private LayoutInflater layoutInflater;
            public MyBaseAdapter(Context context,List<Map<String,Object>> data){
                this.data = data;
                this.context = context;
                this.layoutInflater = LayoutInflater.from(context);
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if(convertView == null){
                    viewHolder = new ViewHolder();
                    convertView = layoutInflater.inflate(R.layout.baseadapter,parent,false);
                    viewHolder.img = (ImageView) convertView.findViewById(R.id.imgView);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.titleView);
                    viewHolder.info = (TextView) convertView.findViewById(R.id.infoView);
                    viewHolder.button = (Button) convertView.findViewById(R.id.buttonView);
                    viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.baseadapter);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.img.setImageResource(Integer.parseInt(data.get(position).get("img").toString()));
                viewHolder.title.setText(data.get(position).get("title").toString());
                viewHolder.info.setText(data.get(position).get("info").toString());
                if(position % 2 == 1){
                    viewHolder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
                }else{
                    viewHolder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                }
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(
                                context,
                                "按钮点击"+position,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
                return convertView;
            }
        }
        final List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("title","G1");
        map.put("info","google1");
        map.put("img",R.drawable.icon1);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G2");
        map.put("info","google2");
        map.put("img",R.drawable.icon2);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G3");
        map.put("info","google3");
        map.put("img",R.drawable.icon3);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G4");
        map.put("info","google4");
        map.put("img",R.drawable.icon4);
        list.add(map);

        map = new HashMap<>();
        map.put("title","G5");
        map.put("info","google5");
        map.put("img",R.drawable.icon5);
        list.add(map);
        listView.setAdapter(new MyBaseAdapter(
                this,list
        ));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        MainActivity.this,
                        list.get(position).get("title").toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

}
