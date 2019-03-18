package com.bignerdranch.android.weather;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.weather.model.find.FindCity;
import com.bignerdranch.android.weather.viewmodels.WeatherViewModel;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AddCityFragment extends Fragment {

    private AppCompatActivity mActivity;
    private WeatherViewModel viewModel;

    public AddCityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.getSupportActionBar().setTitle(getResources().getString(R.string.add_city));
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
        return inflater.inflate(R.layout.find_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button submit = view.findViewById(R.id.search_submit);
        final EditText query = view.findViewById(R.id.search_query);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.find(query.getText().toString()).observe(AddCityFragment.this, new Observer<Resource<FindCity>>() {
                    @Override
                    public void onChanged(@Nullable Resource<FindCity> find) {
                        if (find.status == Resource.Status.LOADING) {
                            Toast.makeText(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT).show();
                        }

                        if (find.status == Resource.Status.SUCCESS) {
                            if (find.data.getList().size() == 1) {
                                viewModel.create(find.data.getList().get(0));
                                Toast.makeText(getContext(), getString(R.string.add_success), Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else {
                                //todo: add alert dialog on error
                            }
                        }

                        if (find.status == Resource.Status.ERROR) {
                            Toast.makeText(getContext(), find.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}

