package khengat.sagar.scanqrc.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.model.History;
import khengat.sagar.scanqrc.util.MyAdapterListener;

/**
 * Created by sagar on 2/13/18.
 */

public class CustomHistory extends RecyclerView.Adapter<CustomHistory.ViewHolder> {


    private Context context;
    public MyAdapterListener onClickListener;


    List<History> productList;
    Cart product;


    public CustomHistory(List<History> products, Context context, MyAdapterListener listener){
        super();
        //Getting all the superheroes
        this.productList = products;
//        this.alQuantity= q;
        this.context = context;
        this.onClickListener = listener;
    }
    public CustomHistory(Cart product, Context context){
        super();
        //Getting all the superheroes
        this.product = product;
//        this.alQuantity= q;
        this.context = context;
    }
    @Override
    public CustomHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_history, parent, false);
        CustomHistory.ViewHolder viewHolder = new CustomHistory.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomHistory.ViewHolder holder, int position) {

        final History product =  productList.get(position);







        holder.textViewName.setText(product.getProductName());

        holder.tvUnit.setText(" "+product.getProductUnit());

        holder.textViewBrand.setText(product.getProductBrand());
        String stringdouble= Integer.toString(product.getProductQuantity());
        String stringPrice= Double.toString(product.getProductTotalPrice());
        holder.tvTotalPrice.setText(stringPrice);
        holder.tvQuantity.setText(product.getProductSize());









    }

//    public void showCartDialog()
//    {
//
//
//    }





    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView delete;
        public TextView textViewName;
        public TextView textViewBrand;

        public TextView tvQuantity;
        public TextView tvUnit;

        public TextView tvTotalPrice;



        public ViewHolder(View itemView) {
            super(itemView);

            delete = (ImageView) itemView.findViewById(R.id.delete);
            textViewName = (TextView) itemView.findViewById(R.id.product_brand);
            textViewBrand= (TextView) itemView.findViewById(R.id.product_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.quantity);
            tvUnit = (TextView) itemView.findViewById(R.id.unit);

            tvTotalPrice = (TextView)itemView.findViewById(R.id.total_price);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.imageViewOnClick(v, getAdapterPosition());
                }
            });

        }
    }
}