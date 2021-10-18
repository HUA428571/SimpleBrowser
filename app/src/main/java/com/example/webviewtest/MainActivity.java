package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

	private WebView webView;
	private long exitTime = 0;

	//设定一个flag表示现在底边栏的显示状态
	private boolean flag_isBarVisible =true;


	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.botton_bar);
		//setContentView(R.layout.botton_bar);
		//隐藏标题栏
		getSupportActionBar().hide();

		webView = (WebView) findViewById(R.id.web_view_ButtomBar);
		// 启用 js 功能
		webView.getSettings().setJavaScriptEnabled(true);
		// 支持缩放，默认为true。是下面那个的前提。
		webView.getSettings().setSupportZoom(true);
		// 设置内置的缩放控件。若为false，则该 WebView 不可缩放
		webView.getSettings().setBuiltInZoomControls(true);
		// 隐藏原生的缩放控件
		webView.getSettings().setDisplayZoomControls(false);


		webView.setWebViewClient(new WebViewClient()
		{

			//WebView.HitTestResult hit = view.getHitTestResult();
			//我们测试的时候发现，对于某些公司（譬如百度）会在访问其网址后加载自己的私有协议打头的地址

			private String startUrl;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				startUrl= url;
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if(startUrl != null&&startUrl.equals(request)){
					if (request.toString().startsWith("http://") || request.toString().startsWith("https://"))
					{
						//加载的url是http\https协议地址
						view.loadUrl(request.getUrl().toString());
						return false; //返回false表示此url默认由系统处理,否则不加载新的页面
					} else
						Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.Tip_IllegalURL), Toast.LENGTH_SHORT).show();
				}
				else{
					return super.shouldOverrideUrlLoading(view, request);
				}
				return true;
			}
		});
		webView.loadUrl("http://www.njupt.edu.cn");

		webView.setOnTouchListener(new View.OnTouchListener()
		{
			//监听滑动的主要目的就在于bar的隐藏于显示，我们在这里首先
			View bar = findViewById(R.id.constraintLayout_bottom_bar);
			//滑动监听相关
			//手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
			float x1 = 0;
			float x2 = 0;
			float y1 = 0;
			float y2 = 0;
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent)
			{
				//手指按下
				if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
				{
					x1 = motionEvent.getX();
					y1 = motionEvent.getY();
				}
//				do
//				{
//					x2 = motionEvent.getX();
//					y2 = motionEvent.getY();
//					//向上滑动
//					if(y1 - y2 > 50)
//					{
//						//Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
//						if(flag_isBarVisible ==true){
//							animationBarGone(bar);
//							flag_isBarVisible=false;
//						}
//						bar.setVisibility(View.INVISIBLE);
//					}
//					else if(y2 - y1 > 50)
//					{
//						//Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
//						flag_isBarVisible=true;
//						bar.setVisibility(View.VISIBLE);
//					}
//					else if(x1 - x2 > 50)
//					{
//						Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//					}
//					else if(x2 - x1 > 50)
//					{
//						Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//					}
//				}while (motionEvent.getAction() != MotionEvent.ACTION_UP);



				//手指离开
				if(motionEvent.getAction() == MotionEvent.ACTION_UP)
				{
					x2 = motionEvent.getX();
					y2 = motionEvent.getY();
					//向上滑动
					if(y1 - y2 > 50)
					{
						//Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
						if(flag_isBarVisible ==true){
							animationBarGone(bar);
							flag_isBarVisible=false;
						}
						bar.setVisibility(View.INVISIBLE);
					}
					else if(y2 - y1 > 50)
					{
						//Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();

						if(flag_isBarVisible ==false){
							animationBarVisible(bar);
							flag_isBarVisible=true;
						}
//						bar.setVisibility(View.INVISIBLE);
//						flag_isBarVisible=true;
						bar.setVisibility(View.VISIBLE);
					}
					else if(x1 - x2 > 50)
					{
						Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
					}
					else if(x2 - x1 > 50)
					{
						Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
					}
				}
				return false;
			}
		});



		ImageButton Btn_FullScrean = (ImageButton) findViewById(R.id.imageButton_FullScreen);
//		Btn_FullScrean.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
//				requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
//				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
//			}
//		});
		Button Btn_GO = (Button) findViewById(R.id.goToWebButton);
		Btn_GO.setOnClickListener(this);
		ImageButton Btn_Back = (ImageButton) findViewById(R.id.imageButton_Back);
		Btn_Back.setOnClickListener(this);
		ImageButton Btn_GoForward = (ImageButton) findViewById(R.id.imageButton_Forward);
		Btn_GoForward.setOnClickListener(this);
	}

	private void animationBarVisible(View view)
	{
		Animation animation =AnimationUtils.loadAnimation(MainActivity.this,R.anim.anim_bottpm_bar_visile);
		view.startAnimation(animation);
	}

	private void animationBarGone(View view)
	{
		Animation animation =AnimationUtils.loadAnimation(MainActivity.this,R.anim.anim_bottom_bar_gone);
		view.startAnimation(animation);
//		Animation animation =AnimationUtils.loadAnimation(MainActivity.this,R.anim.anim_bottom_bar_gone);
//		view.startAnimation(animation);

	}

	@Override
	public void onBackPressed()
	{
		// 能够返回则返回上一页
		if (webView.canGoBack())
		{
			webView.goBack();
		} else
		{
			if ((System.currentTimeMillis() - exitTime) > 2000)
			{
				// 连点两次退出程序
				Toast.makeText(MainActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else
			{
				super.onBackPressed();
			}
		}
	}


	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.imageButton_Back:
				webView.goBack();
				break;
			case R.id.imageButton_Forward:
				webView.goForward();
				break;
			case R.id.goToWebButton:
				EditText editUrl = (EditText) findViewById(R.id.urlTextInput);

				//注意，这边我们指定输入法模式，在布局文件中
				String urlInput = editUrl.getText().toString();

				//test
				//Toast.makeText(MainActivity.this,urlInput, Toast.LENGTH_SHORT).show();
				//webView.loadUrl("http://www.bing.com");
				//test over

				webView.loadUrl("http://" + urlInput);
				break;

		}

	}


//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		//继承了Activity的onTouchEvent方法，直接监听点击事件
//		if(event.getAction() == MotionEvent.ACTION_DOWN) {
//			//当手指按下的时候
//			x1 = event.getX();
//			y1 = event.getY();
//		}
//		if(event.getAction() == MotionEvent.ACTION_UP) {
//			//当手指离开的时候
//			x2 = event.getX();
//			y2 = event.getY();
//			if(y1 - y2 > 50) {
//				Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
//			} else if(y2 - y1 > 50) {
//				Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
//			} else if(x1 - x2 > 50) {
//				Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
//			} else if(x2 - x1 > 50) {
//				Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
//			}
//		}
//		return super.onTouchEvent(event);
//	}

}