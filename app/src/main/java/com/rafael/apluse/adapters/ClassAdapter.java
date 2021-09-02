package com.rafael.apluse.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.rafael.apluse.R;
import com.rafael.apluse.classes.StudentClass;
import com.rafael.apluse.classes.TinyDB;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    //private ArrayList<About> about ;
    private ArrayList<StudentClass> classes;
    private final Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
       // ImageView image;
        TextView className;
        TextView proName;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
           // image = itemView.findViewById(R.id.ivHomeRVImage);
            className = itemView.findViewById(R.id.tvClassNameHomeRVCard);
            proName = itemView.findViewById(R.id.tvProNameHomeRVCard);
            cardView = itemView.findViewById(R.id.classRVCardView);
        }

    }

    public ClassAdapter(Context context,ArrayList<StudentClass>classes) {
        mContext = context;
        this.classes = classes;
        //this.businesses = businesses;

       // Log.d("GGGGGGGGGGGGG",about.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_class_card_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        TinyDB tinyDB = new TinyDB(mContext);

      //  Log.d(TAG, "onBindViewHolder: called.");
       // Business currentBusiness = businesses.get(position);
        StudentClass currentClass = classes.get(position);
        //  About currentAbout = about.get(position);
        tinyDB.putInt("currentPosition",position);
      //  Log.d("GGGGGGGGGHHH",about.toString());
        holder.className.setText(currentClass.getClassName());
        holder.proName.setText(currentClass.getProName());
        holder.cardView.setCardBackgroundColor(Color.parseColor(currentClass.getColor()));
       // String color = currentClass.getColor();

//        lightBlue">#8197F5</color>
//        lightYellow">#F5CD81</color>
//        lightPurple">#B281F5</color>
//        lightRed">#F58181</color>
//        lightCyan">#81D1F5</color>
//        lightGreen">#81F5A4</color>
//        primary">#1400FF</color>
//        if(color.equals("Blue"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#8197F5"));
//
//        }
//        else if(color.equals("Yellow"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#F5CD81"));
//        }
//        else if(color.equals("Purple"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#B281F5"));
//
//        }
//        else if(color.equals("Red"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#F58181"));
//        }
//        else if(color.equals("Cyan"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#81D1F5"));
//
//        }
//        else if(color.equals("Green"))
//        {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#81F5A4"));
//
//        }
//        Glide.with(mContext)
//                .load(currentBusiness.getAbout().getImageUrl())
//                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GGGGGXXXXXX",position+"");
                //tinyDB.putInt("POSITION_KEY",position);
                String className = classes.get(position).getClassName();
                tinyDB.putObject("RVOnClickClassName",className);

                Toast.makeText(view.getContext(),className, Toast.LENGTH_SHORT).show();
                //tinyDB.putListObject("Services",ArrayList(businesses));

              //  Intent intent = new Intent(mContext, BusinessDetail.class);
             //   mContext.startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        Log.d("GGGGGGGGGZZZ",classes.size()+"");
        return classes.size();


    }


//    public void filterZipCodeList(@NotNull ArrayList<Business> filteredList) {
//        businesses = filteredList;
//        notifyDataSetChanged();
//    }
//
//
//    public void filterNameList(@NotNull ArrayList<Business> filteredList) {
//        businesses.clear();
//        businesses = filteredList;
//        notifyDataSetChanged();
//
//    }

}
