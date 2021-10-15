package com.example.webviewtest;

import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

	private WebView webView;
	private long exitTime=0;

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
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, final String url)
			{

				//WebView.HitTestResult hit = view.getHitTestResult();
				//我们测试的时候发现，对于某些公司（譬如百度）会在访问其网址后加载自己的私有协议打头的地址

				if (url.startsWith("http://") || url.startsWith("https://"))
				{ //加载的url是http/https协议地址
					view.loadUrl(url);
					return false; //返回false表示此url默认由系统处理,否则不加载新的页面
				} else
					Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.Tip_IllegalURL), Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		webView.loadUrl("http://www.njupt.edu.cn");

		Button Btn_GO = (Button) findViewById(R.id.goToWebButton);
		Btn_GO.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				EditText editUrl = (EditText) findViewById(R.id.urlTextInput);

				//注意，这边我们指定输入法模式，在布局文件中
				String urlInput = editUrl.getText().toString();

				//test
				//Toast.makeText(MainActivity.this,urlInput, Toast.LENGTH_SHORT).show();
				//webView.loadUrl("http://www.bing.com");
				//test over

				webView.loadUrl("http://" + urlInput);
			}
		});

		Button Btn_FullScrean = (Button) findViewById(R.id.Btn_FullScrean);
//		Btn_FullScrean.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
//				requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
//				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏
//			}
//		});
		Button Btn_GoBack =(Button) findViewById(R.id.Btn_GoBack);
		Btn_GoBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (webView.canGoBack())
				{
					webView.goBack();
				}
			}
		});
		Button Btn_GoForward =(Button) findViewById(R.id.Btn_GoForward);
		Btn_GoBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				webView.goForward();
			}
		});
	}

	/**
	 * 返回按钮处理
	 */
	@Override
	public void onBackPressed() {
		// 能够返回则返回上一页
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				// 连点两次退出程序
				Toast.makeText(MainActivity.this, "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
			}
		}
	}


}