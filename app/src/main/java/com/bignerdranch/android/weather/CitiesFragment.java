package com.bignerdranch.android.weather;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.weather.model.weathers.WeatherList;
import com.bignerdranch.android.weather.model.weathers.WeatherMain;
import com.bignerdranch.android.weather.viewmodels.WeatherViewModel;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class CitiesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AppCompatActivity mActivity;
    private WeatherViewModel viewModel;

    public CitiesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.getSupportActionBar().setTitle("Погода");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = ((AppCompatActivity) getActivity());
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cities_rv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_cities);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.get().observe(this, new Observer<Resource<ArrayList<WeatherMain>>>() {
            @Override
            public void onChanged(@Nullable Resource<ArrayList<WeatherMain>> currentWeatherResource) {
                if (currentWeatherResource.status == Resource.Status.LOADING) {
                    Toast.makeText(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show();
                }

                if (currentWeatherResource.status == Resource.Status.SUCCESS) {
                    mRecyclerView.setAdapter(new RecyclerAdapter(currentWeatherResource.data));
                }

                if (currentWeatherResource.status == Resource.Status.ERROR) {
                    Toast.makeText(getContext(), currentWeatherResource.message, Toast.LENGTH_SHORT).show();
                }
            }
        });


        FloatingActionButton addCity = view.findViewById(R.id.add_city);
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().getSupportFragmentManager().beginTransaction()
                       .add(R.id.fl_content, new AddCityFragment(), AddCityFragment.class.getSimpleName())
                       .addToBackStack(AddCityFragment.class.getSimpleName())
                       .commit();
            }
        });
    }


    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
        private ArrayList<WeatherMain> items;

        RecyclerAdapter(ArrayList<WeatherMain> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ViewGroup item = (ViewGroup) getLayoutInflater().inflate(R.layout.cities_item_rv, viewGroup, false);
            return new RecyclerViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerViewHolder recyclerViewHolder, int i) {
            recyclerViewHolder.onBind(items.get(i));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView city;
            private ImageView img;
            private TextView tempa;
            private LinearLayout mLinearLayout;

            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                city = itemView.findViewById(R.id.txt);
                img = itemView.findViewById(R.id.img);
                tempa = itemView.findViewById(R.id.tempa);
                mLinearLayout = itemView.findViewById(R.id.Linear);
                mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", "pressed");
                    }
                });
            }

            public void onBind(WeatherMain currentWeather) {
                city.setText(currentWeather.getCity().getName());
                tempa.setText(String.valueOf(currentWeather.getList().get(0).getMain().getTemp()));
            }
        }
    }


}

