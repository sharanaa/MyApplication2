package com.example.student.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    //Step 1:
    RecyclerView mUsersList;
    DatabaseReference mDatabase;
    FloatingActionButton mFloat;
    private LocationListener locationListener;
    private LocationManager locationManager;
    String lat, lng;
    Context mContext;
    String phoneNo="8072468184";
    SmsManager manager;
    boolean alert=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        manager = SmsManager.getDefault();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mFloat=(FloatingActionButton)findViewById(R.id.float_alert);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        //Step 2: Recycler view
        mUsersList=(RecyclerView)findViewById(R.id.user_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, "Hello", null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }


    //Step 3: ctrl+O

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseRecyclerAdapter
        // refer :  https://github.com/Sivakumar00/HumanBeing/blob/master/app/src/main/java/siva/com/weengineers/UsersActivity.java
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.single_user_layout,
                UserViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, Users model, final int position) {
                //mainpart
                viewHolder.setTextView(model.getEmail());
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    /*String key=getRef(position).getKey();
                        String email=dataSnapshot.child(key).child("email").getValue().toString();
                        String name=dataSnapshot.child(key).child("username").getValue().toString();
                        viewHolder.setTextView(email+" "+name);
*/
    @Override
    public void onLocationChanged(Location location) {

        lat= String.valueOf(location.getLatitude());
        lng=String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        //Step 4: create view object
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            //step 5: initialize view obj
            mView=itemView;
        }
        //Step 6: methods for setText
        public void setTextView(String text){
            TextView textView=(TextView)mView.findViewById(R.id.txtData);
            textView.setText(text);
        }
    }
}
