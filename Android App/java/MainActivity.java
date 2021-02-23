package com.example.escw1;

import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements OnClickListener, ExampleDialog.ExampleDialogListener  {

    static String MQTTHOST = "tcp://test.mosquitto.org:1883";
    String topicStr = "IC.embedded/Team_ALG/#";

    MqttAndroidClient client;

    Button settings_btn, forward_btn, backward_btn, rotate_btn, water_btn;
    TextView plant_name, temp_text, humid_text, pressure_text, rotate_text, water_text, position_text;

    Vibrator vibrator;
    Ringtone myRingtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        myRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);

        try {
            setw();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String clientId = MqttClient.generateClientId();
        client =  new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);

        MqttConnectOptions options = new MqttConnectOptions();

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    setSubs();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Connection failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback((new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject obj = new JSONObject(new String(message.getPayload()));

                temp_text.setText(obj.getString("temp").substring(0,5) + " C");
                humid_text.setText(obj.getString("humidity").substring(0,5) + " %");
                pressure_text.setText(obj.getString("pressure").substring(0,7) + " Pa");
                rotate_text.setText(obj.getString("last_rotate_hr") + ":00");
                water_text.setText(obj.getString("last_water_hr") + ":00");

                if (obj.getString("flowerpot").equals("true")) {
                    position_text.setText("FELL DOWN");
                    vibrator.vibrate(2000);
                    myRingtone.play();
                } else {
                    position_text.setText("In Position");
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        }));

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setw() throws IOException {

        plant_name = (TextView)findViewById(R.id.plant_name);
        temp_text = (TextView)findViewById(R.id.temp_text);
        humid_text = (TextView)findViewById(R.id.humid_text);
        pressure_text = (TextView)findViewById(R.id.pressure_text);
        rotate_text = (TextView)findViewById(R.id.rotate_text);
        water_text = (TextView)findViewById(R.id.water_text);
        position_text = (TextView)findViewById(R.id.position_text);


        settings_btn = (Button) findViewById(R.id.settings_btn);
        forward_btn = (Button) findViewById(R.id.forward_btn);
        backward_btn = (Button) findViewById(R.id.backward_btn);
        rotate_btn = (Button) findViewById(R.id.rotate_btn);
        water_btn = (Button) findViewById(R.id.water_btn);


        settings_btn.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        openDialog();

                        return false;
                    }
                }
        );

        forward_btn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("Action", "manual_return");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String payload = jo.toString();
                        pub(payload);
                    }
                }
        );

        backward_btn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("Action", "manual_backward");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String payload = jo.toString();
                        pub(payload);

                    }
                }
        );

        rotate_btn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("Action", "manual_turn");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String payload = jo.toString();
                        pub(payload);
                    }
                }
        );

        water_btn.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("Action", "water");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String payload = jo.toString();
                        pub(payload);
                    }
                }
        );

    }

    @Override
    public void onClick (View v) {
        try {

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String name, int distance, Boolean sun, Boolean rain, Boolean wind, int water_freq, int water_amt, int rotate) {
        plant_name.setText(name);

        JSONObject jo = new JSONObject();
        try {
            jo.put("phone_data", "yo");
            jo.put("move_time", distance);
            jo.put("max_num", 2); // motor backward

            if (sun) {
                jo.put("Temp_threshold", 40);

            } else {
                jo.put("Temp_threshold", 90);

            }

            if (rain) {
                jo.put("Humid_threshold", 70);

            } else {
                jo.put("Humid_threshold", 1000);

            }

            if (wind) {
                jo.put("Pressure_threshold", 1017);

            } else {
                jo.put("Pressure_threshold", 2000);

            }

            jo.put("water_time", water_amt);
            jo.put("each_hr_for_water", water_freq);

            jo.put("each_hr_for_rotation", rotate);
            jo.put("rotate_time", 1); //testing 90 degree

            jo.put("time_for_return", 0.75);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        String payload = jo.toString();
        pub(payload);
    }

    public void pub(String payload) {

        String topic = "IC.embedded/Team_ALG/test";

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            client.publish(topic, message,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //toast
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    //toast
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void setSubs() {
        try {
            client.subscribe(topicStr, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}
