package ru.pavelmorozovtk.interestingplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class ShowPointActivity extends AppCompatActivity implements View.OnClickListener, InputListener {

    private MapView mapView;
    Button btn_back;
    Button btn_save;
    Point point;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_show_point);

        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        mapView = findViewById(R.id.mapview);
        bitmap = getBitmapFromVectorDrawable(this, R.drawable.ic_mark);
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            if (extra.getBoolean("show")){
                btn_save.setVisibility(View.GONE);
                double[] data = getPointDoubleArr(extra.getString("point"));
                Point points = new Point(data[0], data[1]);
                mapView.getMap().move(new CameraPosition(new Point(data[0], data[1]), 16.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0), null);
                mapView.getMap().getMapObjects().addPlacemark(new Point(data[0], data[1]), ImageProvider.fromBitmap(bitmap));
            } else {
                if (extra.getString("point").length() > 5) {
                    double[] data = getPointDoubleArr(extra.getString("point"));
                    Point point = new Point(data[0], data[1]);
                    mapView.getMap().move(new CameraPosition(point, 16.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 0), null);
                    mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(bitmap));
                    mapView.getMap().addInputListener(this);
                } else {
                    mapView.getMap().move(new CameraPosition(new Point(55.751574, 37.573856), 0.0f, 0.0f, 0.0f),
                            new Animation(Animation.Type.SMOOTH, 0), null);
                    mapView.getMap().addInputListener(this);
                }
            }
        }
    }

    public double[] getPointDoubleArr(String points) {
        String[] temp = points.split(" ");
        return new double[]{Double.parseDouble(temp[0]), Double.parseDouble(temp[1])};
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (view.getId() == R.id.btn_save) {
            if(point != null)
            sendMessage(String.valueOf(point.getLatitude()) + " " + String.valueOf(point.getLongitude()));
        }
    }

    private void sendMessage(String message){
        Intent data = new Intent();
        data.putExtra("point", message);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point getpoint) {
        map.getMapObjects().clear();
        Bitmap bitmap = getBitmapFromVectorDrawable(this, R.drawable.ic_mark);
        map.getMapObjects().addPlacemark(getpoint, ImageProvider.fromBitmap(bitmap));

        this.point = getpoint;

        Toast.makeText(this, getpoint.getLatitude() + "  " + getpoint.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}