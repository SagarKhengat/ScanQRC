package khengat.sagar.scanqrc.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Area;
import khengat.sagar.scanqrc.model.Store;

public class SpinnerStoreAdapter extends ArrayAdapter<Store> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Store> values;
    LayoutInflater flater;

    public SpinnerStoreAdapter(Context context, int textViewResourceId,
                              List<Store> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Store getItem(int position){

        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
//        TextView label = new TextView(context);
//        label.setTextColor(Color.BLACK);
//        // Then you can get the current item using the values array (Users array) and the current position
//        // You can NOW reference each method you has created in your bean object (User class)
//        Store area = values.get(position);
//        label.setText(area.getStoreName());
//
//        // And finally return your dynamic (or custom) view for each spinner item
//        return label;
        flater = LayoutInflater.from(context);

        if(convertView == null){
            convertView = flater.inflate(R.layout.spinner,parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        txtTitle.setTextColor(Color.BLACK);
        Store area = values.get(position);
        txtTitle.setText(area.getStoreName());

        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        flater = LayoutInflater.from(context);

        if(convertView == null){
            convertView = flater.inflate(R.layout.spinner,parent, false);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        txtTitle.setTextColor(Color.BLACK);
        Store area = values.get(position);
        txtTitle.setText(area.getStoreName());

        return convertView;
    }
}