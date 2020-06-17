package com.fridayapp.roomdatabase.activities;

import android.widget.Filter;

import com.fridayapp.roomdatabase.adapter.TaskAdapter;
import com.fridayapp.roomdatabase.model.Task;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    TaskAdapter adapter;
    ArrayList<Task> filterList;

    public CustomFilter(ArrayList<Task> filterList,TaskAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Task> filteredPets=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPets.add(filterList.get(i));
                }
            }

            results.count=filteredPets.size();
            results.values=filteredPets;

        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.tasks= (ArrayList<Task>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();

    }
}