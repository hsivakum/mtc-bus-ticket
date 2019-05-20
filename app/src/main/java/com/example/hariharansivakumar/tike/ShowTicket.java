package com.example.hariharansivakumar.tike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShowTicket extends AppCompatActivity {

    String text2Qr;
    ImageView imageView;
    TextView from,to,amounts,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ticket);
        imageView = (ImageView)findViewById(R.id.imageView);
        from =(TextView)findViewById(R.id.from);
        amounts =(TextView)findViewById(R.id.amount);
        date =(TextView)findViewById(R.id.dates);
        to =(TextView)findViewById(R.id.to);
        amounts =(TextView)findViewById(R.id.amount);
        date =(TextView)findViewById(R.id.dates);
        from.setText(getIntent().getStringExtra("from"));
        to.setText(getIntent().getStringExtra("to"));
        amounts.setText(getIntent().getStringExtra("amount"));
        date.setText(getIntent().getStringExtra("date"));
        text2Qr=getIntent().getStringExtra("id").trim();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString("hello", "lol");
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }
}
