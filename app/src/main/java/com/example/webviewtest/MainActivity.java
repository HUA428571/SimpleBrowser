package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.slice.SliceItem;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

	SQLiteDatabase db;
	String [] res= {"菠菜宠物店1","菠菜宠物店2", "菠菜宠物店3", "菠菜宠物店4"};
	int sum;
	private WebView webView;
	private long exitTime = 0;
	public static final String TAG= "MainActivity";
	String NowUrl= null;
	private AutoCompleteTextView autoCompleteTextView;
	application app;

	ImageButton btn_GO,btn_Back,btn_GoForward,btn_Settings,btn_downLoad,btn_NoPictureBrowsing,btn_FullScreen,btn_RotationLock;
	EditText editText_URL;
	TextView text_FullScreen;

	//设定一个flag表示现在底边栏的显示状态
	private boolean flag_isBarVisible = true;
	//设定一个flag表示现在设置窗口的显示状态
	private boolean flag_isSettingsVisible = false;
	//设定一个flag表示现在是否处于无图模式
	private boolean flag_isNoPictureBrowsing = false;
	//设定一个flag表示现在是否处于全屏模式
	private boolean flag_isFullScreen = false;
	//设定一个flag表示现在是否处于旋转锁定模式
	private boolean flag_isRotationLock = false;

	public void SaveHtml(String str){
		try
		{
			URL url=null;
			url=new URL(str);
			BufferedReader in=new BufferedReader(new InputStreamReader(url.openStream()));
			File appDir = new File("data/data/com.example.webviewtest/SaveInternet/html");//xxxx为手机本地生成的文件夹名//称，自定义
			if (!appDir.exists()) {
				appDir.mkdir();
			}
			@SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date curDate =  new Date(System.currentTimeMillis());
			//获取当前时间
			String  str0 =  formatter.format(curDate);
			//当前时间来命名图片,这样就不会覆盖之前的图片,如果此值固定就只有一张图片，以前的会被替换；
			String fileName = str0 + ".html";
			File filePath = new File(appDir,fileName);
			if (!filePath.exists()) {
				Log.d(TAG,"file is not exist");
				filePath.createNewFile();
			}
			FileWriter fin=new FileWriter(filePath);
			String line;
			while((line=in.readLine())!=null)
			{
				fin.write(line);
			}
			fin.close();
			in.close();
		}catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.botton_bar);
		//隐藏标题栏
		getSupportActionBar().hide();

		app = (application) getApplication();
		app.setTotal_favourite_num(0);

		//设置菜单栏的隐藏
		View settings = findViewById(R.id.constraintLayout_menu);
		settings.setVisibility(View.INVISIBLE);

		//初始化控件
		initView();

		ImageButton Btn_Favourite = (ImageButton) findViewById(R.id.imageButton_Favourite);
		Btn_Favourite.setOnClickListener(this);
		ImageButton Btn_History = (ImageButton) findViewById(R.id.imageButton_History);
		Btn_History.setOnClickListener(this);

		//创建三张表：历史记录(test)、收藏的页面(favouriteWebsite)、收藏夹内文件夹(favourite)
		db=openOrCreateDatabase("TestDB", Context.MODE_PRIVATE,null);
		String createHistoryTable="CREATE TABLE IF NOT EXISTS test (title VARCHAR(32),url VARCHAR(32))";
		String createFavouriteWebsiteTable="CREATE TABLE IF NOT EXISTS favouriteWebsite (id INT,title VARCHAR(32),url VARCHAR(32),favouriteId INT)";
		String createFavouriteTable="CREATE TABLE IF NOT EXISTS favourite (id INT,name VARCHAR(32))";
		db.execSQL(createHistoryTable);
		db.execSQL(createFavouriteWebsiteTable);
		db.execSQL(createFavouriteTable);

		// 启用 js 功能
		webView.getSettings().setJavaScriptEnabled(true);
		// 支持缩放，默认为true。是下面那个的前提。
		webView.getSettings().setSupportZoom(true);
		// 设置内置的缩放控件。若为false，则该 WebView 不可缩放
		webView.getSettings().setBuiltInZoomControls(true);
		// 隐藏原生的缩放控件
		webView.getSettings().setDisplayZoomControls(false);

		webView.getSettings().setDomStorageEnabled(true);

		//加载主页
		webView.loadUrl("https://"+getResources().getString(R.string.url_home));
		autoCompleteTextView.setText(webView.getTitle());

		//解决重定向导致网页无法访问以及返回键的问题
		webView.setWebViewClient(new WebViewClient()
		{
			//我们测试的时候发现，对于某些公司（譬如百度）会在访问其网址后加载自己的私有协议打头的地址,而这样的网址是webView无法访问的
			private String startUrl;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				startUrl = url;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				//System.out.println(url);
				Cursor cur=db.rawQuery("select * from test",null);
				ContentValues cv = new ContentValues(2);
				int sum=cur.getCount();
				System.out.println("sum:"+sum);
				if(sum==0){
					cv.put("url",url);
					cv.put("title",view.getTitle());
					db.insert("test", null,cv);
					//System.out.println("111111111111111111");
				}
				else{
					for(int i=0;i<sum;i++) {
						cur.moveToPosition(i);
						System.out.println("url:"+url);
						System.out.println("cur:"+cur.getString(1));
						if(url.equals(cur.getString(1)))
						{
							//System.out.println("151566116516561");
							db.execSQL("DElETE  FROM test where url= ?", new String[]{url});
							//db.delete("test","url= ? ",new String[] {url});
							cv.put("url",url);
							cv.put("title",view.getTitle());
							db.insert("test", null,cv);
							//db.close();
							break;
						}
						if(i==sum-1)
						{
							cv.put("url",url);
							cv.put("title",view.getTitle());
							db.insert("test", null,cv);
							//System.out.println("111111111111111111");
						}

					}
				}

			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
			{
				if (startUrl != null && startUrl.equals(request))
				{
					if (request.toString().startsWith("http://") || request.toString().startsWith("https://"))
					{
						//加载的url是http\https协议地址
						view.loadUrl(request.getUrl().toString());
						return false; //返回false表示此url默认由系统处理,否则不加载新的页面
					} else
						Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.Tip_IllegalURL), Toast.LENGTH_SHORT).show();
				} else
				{
					return super.shouldOverrideUrlLoading(view, request);
				}
				return true;
			}
		});

		//监听滑动以设置自动隐藏bar栏
		webView.setOnTouchListener(new View.OnTouchListener()
		{
			//监听滑动的主要目的就在于bar的隐藏于显示，我们在这里首先
			View bar = findViewById(R.id.constraintLayout_bottom_bar);
			View settings = findViewById(R.id.constraintLayout_menu);
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
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
				{
					x1 = motionEvent.getX();
					y1 = motionEvent.getY();
				}
				//手指离开
				if (motionEvent.getAction() == MotionEvent.ACTION_UP)
				{
					x2 = motionEvent.getX();
					y2 = motionEvent.getY();
					//向上滑动
					if (y1 - y2 > 50)
					{
						//Toast.makeText(MainActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
						if (flag_isBarVisible == true)
						{
							animationBarGone(bar);
							flag_isBarVisible = false;
						}
						bar.setVisibility(View.INVISIBLE);
						//同时隐藏菜单栏（如果有）
						settings.setVisibility(View.INVISIBLE);
						flag_isSettingsVisible = false;
					} else if (y2 - y1 > 50)
					{
						//Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();

						if (flag_isBarVisible == false)
						{
							animationBarVisible(bar);
							flag_isBarVisible = true;
						}
//						bar.setVisibility(View.INVISIBLE);
//						flag_isBarVisible=true;
						bar.setVisibility(View.VISIBLE);
					} else if (x1 - x2 > 50)
					{
						Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
					} else if (x2 - x1 > 50)
					{
						Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
					}
				}
				return false;
			}
		});

		//设置输入框
		autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View view, boolean focus)
			{
				if(focus)
				{
					//显示当前网址
					autoCompleteTextView.setText(webView.getUrl());
//					//光标置于末尾
//					//editText_URL.setSelection(editText_URL.getText().length());
//					//显示选中
//					editText_URL.setSelection(0,editText_URL.getText().length());
//				//(editText_URL.getText());
				}
				else
				{
					//显示网站名
					autoCompleteTextView.setText(webView.getTitle());
				}
			}
		});

		// 监听键盘回车搜索
		autoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
					// 执行搜索
					btn_GO.callOnClick();
					autoCompleteTextView.clearFocus();
				}
				return false;
			}
		});


		//自动匹配


	}

	private void initView()
	{
		webView = (WebView) findViewById(R.id.webview_main);
		editText_URL = findViewById(R.id.urlTextInput);
		text_FullScreen = findViewById(R.id.textView_FullScreen);
		//绑定按钮点击事件
		btn_GO = (ImageButton) findViewById(R.id.imageButton_GO);
		btn_GO.setOnClickListener(this);
		btn_Back = (ImageButton) findViewById(R.id.imageButton_Back);
		btn_Back.setOnClickListener(this);
		btn_GoForward = (ImageButton) findViewById(R.id.imageButton_Forward);
		btn_GoForward.setOnClickListener(this);
		btn_Settings = (ImageButton) findViewById(R.id.imageButton_Settings);
		btn_Settings.setOnClickListener(this);
		btn_downLoad= (ImageButton) findViewById(R.id.imageButton_Download);
		btn_downLoad.setOnClickListener(this);
		btn_NoPictureBrowsing= (ImageButton) findViewById(R.id.imageButton_NoPictureBrowsing);
		btn_NoPictureBrowsing.setOnClickListener(this);
		btn_FullScreen= (ImageButton) findViewById(R.id.imageButton_FullScreen);
		btn_FullScreen.setOnClickListener(this);
		btn_RotationLock= (ImageButton) findViewById(R.id.imageButton_RotationLock);
		btn_RotationLock.setOnClickListener(this);
	}

	private void animationBarVisible(View view)
	{
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_bottpm_bar_visile);
		view.startAnimation(animation);
	}

	private void animationBarGone(View view)
	{
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_bottom_bar_gone);
		view.startAnimation(animation);
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
			case R.id.imageButton_Settings:
				View settings = findViewById(R.id.constraintLayout_menu);
				if (flag_isSettingsVisible)
				{
					settings.setVisibility(View.INVISIBLE);
					flag_isSettingsVisible = false;
				}
				else
				{
					settings.setVisibility(View.VISIBLE);
					flag_isSettingsVisible = true;
				}
				break;
			case R.id.imageButton_GO:
				EditText editText_URL = findViewById(R.id.urlTextInput);
				//注意，这边我们指定输入法模式，在布局文件中
				String urlInput = editText_URL.getText().toString();

				//test
				//Toast.makeText(MainActivity.this,urlInput, Toast.LENGTH_SHORT).show();
				//webView.loadUrl("http://www.bing.com");
				//test over

				//地址栏有焦点，跳转
				if (editText_URL.hasFocus()) {
					//隐藏键盘
					InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					if (manager.isActive()) {
						manager.hideSoftInputFromWindow(editText_URL.getApplicationWindowToken(), 0);
					}
//					if (!isHttpUrl(urlInput)) {
//						// 不是网址，加载搜索
//						try {
//							// URL 编码
//							urlInput = URLEncoder.encode(urlInput, "utf-8");
//						} catch (UnsupportedEncodingException e) {
//							e.printStackTrace();
//						}
//						urlInput = "https://www.baidu.com/s?wd=" + urlInput + "&ie=UTF-8";
//					}
//					else
//					{
					if(!(urlInput.startsWith("http://") || urlInput.startsWith("https://")))
						urlInput = "http://" + urlInput;
//					}
					webView.loadUrl(urlInput);
					ContentValues cv = new ContentValues(2);
					// 取消掉地址栏的焦点
					editText_URL.clearFocus();
				} else
				{
					// 地址栏没焦点，是刷新
					webView.reload();
				}
				break;
			case R.id.imageButton_Download:
				File appDir0 = new File("data/data/com.example.webviewtest/SaveInternet");//xxxx为手机本地生成的文件夹名//称，自定义
				if (!appDir0.exists()) {
					appDir0.mkdir();
				}
				NowUrl= webView.getOriginalUrl();
				new Thread(runnable).start();
				@SuppressLint("SimpleDateFormat") SimpleDateFormat   formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date curDate =  new Date(System.currentTimeMillis());
				//获取当前时间
				String  str =  formatter.format(curDate);
				File appDir1 = new File("data/data/com.example.webviewtest/SaveInternet/mht");//xxxx为手机本地生成的文件夹名//称，自定义
				if (!appDir1.exists()) {
					appDir1.mkdir();
				}
				webView.saveWebArchive(appDir1+"/"+str+".mht",false,null);
				String local= "浏览器：网页已保存到data/data/com.example.webviewtest/SaveInternet目录下(html,mht)";
				Toast.makeText(MainActivity.this,local,Toast.LENGTH_SHORT).show();
				break;
			case R.id.imageButton_NoPictureBrowsing:
				//设置无图模式
				if(!flag_isNoPictureBrowsing)
				{
					webView.getSettings().setBlockNetworkImage(true);
					btn_FullScreen.setBackgroundResource(R.drawable.btn_picture);
					text_FullScreen.setText(getResources().getString(R.string.imageButton_WithPictureBrowsing));
				}
				else
				{
					webView.getSettings().setBlockNetworkImage(false);
					btn_FullScreen.setBackgroundResource(R.drawable.btn_picture);
					text_FullScreen.setText(getResources().getString(R.string.imageButton_NoPictureBrowsing));
				}
				break;
			case R.id.imageButton_FullScreen:
				if(flag_isFullScreen==false)
				{
					//去掉最上面时间、电量等
					this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					//把地下的地址栏也去掉
					View settingsInFullScreen = findViewById(R.id.constraintLayout_menu);
					settingsInFullScreen.setVisibility(View.INVISIBLE);
					flag_isSettingsVisible = false;
					View bar = findViewById(R.id.constraintLayout_bottom_bar);
					animationBarGone(bar);
					bar.setVisibility(View.INVISIBLE);
					flag_isBarVisible = false;
					flag_isFullScreen=true;
					//最后我们将图标改成退出全屏的样式
					btn_FullScreen.setBackgroundResource(R.mipmap.btn_no_full_screen);
					text_FullScreen.setText(getResources().getString(R.string.imageButton_NoFullScreen));
				}
				else
				{
					this.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
					flag_isFullScreen=false;
					//最后我们将图标改成进入全屏的样式
					btn_FullScreen.setBackgroundResource(R.mipmap.btn_full_screen);
					text_FullScreen.setText(getResources().getString(R.string.imageButton_FullScreen));
				}
				break;
			case R.id.imageButton_RotationLock:
				if(flag_isRotationLock==false)
				{
					this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
					flag_isRotationLock=true;
 				}
				else
				{
					this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
					flag_isRotationLock=false;
				}
				break;
			case R.id.imageButton_Favourite:
				ContentValues FavouriteCv = new ContentValues(4);
				//int totalFavouriteWebsiteNum = app.getTotal_favourite_website_num()+1;
				//System.out.println("geturl:"+totalFavouriteWebsiteNum);
				//System.out.println("gettitle:"+webView.getTitle());
				app.setTotal_favourite_website_num(app.getTotal_favourite_website_num()+1);
				FavouriteCv.put("id",app.getTotal_favourite_website_num());
				FavouriteCv.put("title",webView.getTitle());
				FavouriteCv.put("url",webView.getUrl());
				FavouriteCv.put("favouriteId",0);
				db.insert("favouriteWebsite", null,FavouriteCv);
				Intent favouriteIntent = new Intent(MainActivity.this,FavouriteActivity.class);
				startActivity(favouriteIntent);
				break;
				//setContentView(R.layout.activity_favourite);
			case R.id.imageButton_History:
				Intent historyIntent = new Intent(MainActivity.this,HistoryActivity.class);
				startActivity(historyIntent);
				break;
		}
	}

	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			/**
			 * 要执行的操作
			 */
			SaveHtml(NowUrl);
			// 执行完毕后给handler发送一个空消息
			handler.sendEmptyMessage(0);
		}
	};

	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			return false;
		}
	});

	@Override
	protected void onRestart() {
		super.onRestart();
		//webView.loadUrl(app.getUrl_from_favourite());
	}
	@Override
	protected void onResume() {
		webView = (WebView) findViewById(R.id.webview_main);
		EditText editText_URL = findViewById(R.id.urlTextInput);
		//ContentValues cv = new ContentValues(3);
		//System.out.println("url:"+webView.getUrl());
		//cv.put("url",webView.getUrl());
		//cv.put("title",webView.getTitle());
		//db.insert("test", null,cv);
		super.onResume();
		application app = (application) getApplication();

		if(app.getUrl_from_favourite()=="")
		{
			//webView.loadUrl();
		}
		else
		{
			//System.out.println("123456");
			webView.loadUrl(app.getUrl_from_favourite());
			editText_URL.setText(app.getTitle_from_favourite());
			app.setUrl_from_favourite("");
		}

		if(app.getUrl_from_history()=="")
		{
			//webView.loadUrl();
		}
		else
		{
			//System.out.println("123456");
			webView.loadUrl(app.getUrl_from_history());
			editText_URL.setText(app.getTitle_from_history());
			app.setUrl_from_history("");
		}


		//System.out.println("123456");
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