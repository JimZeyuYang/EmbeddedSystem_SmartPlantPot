package com.example.escw1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {

    private TextView plant_name;
    private NumberPicker plant_picker;

    private TextView distance_name;
    private NumberPicker distance_picker;
    private TextView water_freq_name;
    private NumberPicker water_freq_picker;
    private TextView water_amt_name;
    private NumberPicker water_amt_picker;
    private TextView rotate_name;
    private NumberPicker rotate_picker;

    private Switch sun;
    private Switch rain;
    private Switch wind;

    private ExampleDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Settings")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = Plants.getPlantDatabase().get(plant_picker.getValue()).getName();
                        int distance = distance_picker.getValue()/10;
                        int water_freq = water_freq_picker.getValue();
                        int water_amt = water_amt_picker.getValue() + 1;
                        int rotate = rotate_picker.getValue();
                        listener.applyTexts(name, distance, sun.isChecked(), rain.isChecked(), wind.isChecked(), water_freq, water_amt, rotate);
                    }
                });


        plant_name = (TextView) view.findViewById(R.id.plant_name);
        plant_picker = (NumberPicker) view.findViewById(R.id.plant_picker);
        distance_name = (TextView) view.findViewById(R.id.distance_name);
        distance_picker = (NumberPicker) view.findViewById(R.id.distance_picker);
        water_freq_name = (TextView) view.findViewById(R.id.water_freq_name);
        water_freq_picker = (NumberPicker) view.findViewById(R.id.water_freq_picker);
        water_amt_name = (TextView) view.findViewById(R.id.water_amt_name);
        water_amt_picker = (NumberPicker) view.findViewById(R.id.water_amt_picker);
        rotate_name = (TextView) view.findViewById(R.id.rotate_name);
        rotate_picker = (NumberPicker) view.findViewById(R.id.rotate_picker);

        sun = (Switch) view.findViewById(R.id.sun);
        rain = (Switch) view.findViewById(R.id.rain);
        wind = (Switch) view.findViewById(R.id.wind);


        distance_picker.setMaxValue(100);
        distance_picker.setMinValue(0);
        distance_picker.setValue(50);
        distance_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                distance_name.setText("Backward Distance:     " + newVal + " cm");
            }
        });


        Plants.initPlants();
        plant_picker.setMaxValue(Plants.getPlantDatabase().size() -1);
        plant_picker.setMinValue(0);
        plant_picker.setDisplayedValues(Plants.PlantNames());

        plant_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                plant_name.setText(Plants.getPlantDatabase().get(newVal).getName());
                water_freq_picker.setValue(Plants.getPlantDatabase().get(newVal).getWater_freq());
                water_freq_name.setText("Water Frequency: once every      " + Plants.getPlantDatabase().get(newVal).getWater_freq() + " hour");

                water_amt_picker.setValue((Plants.getPlantDatabase().get(newVal).getWater_amt() / 5) -1);
                water_amt_name.setText("Water Amount:        "+ Plants.getPlantDatabase().get(newVal).getWater_amt() + " ml");

                rotate_picker.setValue(Plants.getPlantDatabase().get(newVal).getRotate_freq());
                rotate_name.setText("Water Frequency: once every      " + Plants.getPlantDatabase().get(newVal).getRotate_freq() + " hour");

                sun.setChecked(!Plants.getPlantDatabase().get(newVal).getSun());
                rain.setChecked(!Plants.getPlantDatabase().get(newVal).getRain());
                wind.setChecked(!Plants.getPlantDatabase().get(newVal).getWind());

            }
        });

        water_freq_picker.setMaxValue(23);
        water_freq_picker.setMinValue(1);

        water_freq_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                water_freq_name.setText("Water Frequency: once every      " + newVal + " hour");
            }
        });

        final String[] water_amt = new String[10];
        for (int i = 0; i < water_amt.length; i++) {
            int vol = 5 * (i + 1);
            water_amt[i] = "" + vol;
        }

        water_amt_picker.setMaxValue(water_amt.length -1);
        water_amt_picker.setMinValue(0);
        water_amt_picker.setDisplayedValues(water_amt);

        water_amt_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                water_amt_name.setText("Water Amount:        "+ water_amt[newVal] + " ml");
            }
        });

        rotate_picker.setMaxValue(6);
        rotate_picker.setMinValue(1);

        rotate_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                rotate_name.setText("Rotate Frequency: once every    " + newVal + " hour");
            }
        });




        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String name, int distance, Boolean sun, Boolean rain, Boolean wind, int water_freq, int water_amt, int rotate);
    }
}
