package com.simple.cat.androidapt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simple.cat.aptannotation.BindActivity;
import com.simple.cat.aptannotation.BindView;

@BindActivity
public class MainActivity extends AppCompatActivity {

		@BindView(R.id.tv_content)
		TextView mTvContent;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				DIMainActivity.bindView(this);
				mTvContent.setText("android apt test");
				mTvContent.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								Toast.makeText(MainActivity.this,
									"apt test progress",
									Toast.LENGTH_SHORT).show();
						}
				});
		}
}
