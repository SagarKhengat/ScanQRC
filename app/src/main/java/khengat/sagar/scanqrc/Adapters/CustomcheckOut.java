package khengat.sagar.scanqrc.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.List;

import khengat.sagar.scanqrc.R;
import khengat.sagar.scanqrc.model.Cart;
import khengat.sagar.scanqrc.util.MyAdapterListener;


public class CustomcheckOut extends RecyclerView.Adapter<CustomcheckOut.ViewHolder> {


    private Context context;
    public MyAdapterListener onClickListener;


    List<Cart> productList;
    Cart product;


    public CustomcheckOut(List<Cart> products, Context context, MyAdapterListener listener){
        super();
        //Getting all the superheroes
        this.productList = products;
//        this.alQuantity= q;
        this.context = context;
        this.onClickListener = listener;
    }
    public CustomcheckOut(Cart product, Context context){
        super();
        //Getting all the superheroes
        this.product = product;
//        this.alQuantity= q;
        this.context = context;
    }
    @Override
    public CustomcheckOut.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_checkout, parent, false);
        CustomcheckOut.ViewHolder viewHolder = new CustomcheckOut.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomcheckOut.ViewHolder holder, int position) {

        final Cart product =  productList.get(position);







        holder.textViewName.setText(product.getProductName());

        holder.tvUnit.setText(" "+product.getProductUnit());
        holder.textViewBrand.setText(product.getProductBrand());

        int value = product.getProductQuantity();

        double multiQ = value * product.getProductTotalPrice();

        String stringdouble= Integer.toString(product.getProductQuantity());
        String stringPrice= Double.toString(multiQ);
        holder.tvTotalPrice.setText(stringPrice);
        holder.tvQuantity.setText(stringdouble);









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