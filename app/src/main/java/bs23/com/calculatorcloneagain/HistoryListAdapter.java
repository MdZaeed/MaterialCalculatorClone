package bs23.com.calculatorcloneagain;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by BS-86 on 2/8/2016.
 */
public class HistoryListAdapter extends BaseAdapter {

    Context context;
    ArrayList<History> data;
    private static LayoutInflater inflater;
    TextView rawTextView,resultTextView;

    public void changeData(ArrayList<History> data)
    {
        this.data=data;
    }

    public HistoryListAdapter(Context context,ArrayList<History> data,TextView textView1,TextView textView2)
    {
        this.context=context;
        this.data=data;
        rawTextView=textView1;
        resultTextView=textView2;

        for(int i=0;i<data.size();i++)
        {
            Log.d("Data" , data.get(0).getEqaution());
        }

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Aschi",data.get(position).getId()+"   " + data.get(position).getEqaution());
        View resultView;
        resultView=inflater.inflate(R.layout.list_items,null);

        TextView first=(TextView) resultView.findViewById(R.id.equationText);
        TextView second=(TextView) resultView.findViewById(R.id.resultTextSec);
        TextView third=(TextView) resultView.findViewById(R.id.timeText);

        first.setText(data.get(position).getEqaution());
        second.setText(data.get(position).getResult());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String TIME=dateFormat.format(date);
        String[] timeNow=data.get(position).getTime().split("\\s+");

        String[] time=data.get(position).getTime().split("\\s+");
        if(timeNow[0].equals(time[0])) {
            third.setText(time[1]);
        }
        else
        {
            third.setText(time[0]);
        }
        return resultView;
    }
}
