package xyz.ayadev.junkcallblocker;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbhelper = null;
    private SharedPreferences SharedPreferences;
    private ProgressDialog mProgressDialog;
    private String BaseUrl = "http://ayadev.xyz/";
    private String DatabaseFile = "HKG.zip";
    private String VersionFile = "http://ayadev.xyz/HKG_INFO.xml";
    private String WorkingDirectory = Environment.getExternalStorageDirectory().getPath() + "/Android/data/xyz.ayadev.junkcallblocker/";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void Unzip(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream ZIS = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ZE;
            int count;
            byte[] buffer = new byte[8192];
            while ((ZE = ZIS.getNextEntry()) != null) {
                File file = new File(targetDirectory, ZE.getName());
                File dir = ZE.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ZE.isDirectory())
                    continue;
                try (FileOutputStream FOS = new FileOutputStream(file)) {
                    while ((count = ZIS.read(buffer)) != -1)
                        FOS.write(buffer, 0, count);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new CheckDatabase().execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File Dir = new File(WorkingDirectory);
            try {
                if (!Dir.mkdir()) {
                    Log.e("Junk Call Blocker Error", "Directory is not created.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("更新");
            mProgressDialog.setMessage("正在下載最新版本資料庫...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(WorkingDirectory + DatabaseFile);
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String Debug) {
            try {
                Unzip(new File(WorkingDirectory + DatabaseFile), new File(WorkingDirectory));
                mProgressDialog.dismiss();
                new InstallDatabase().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class InstallDatabase extends AsyncTask<Void, Integer, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("更新");
            mProgressDialog.setMessage("正在最佳化資料庫...\n此過程所需時間取決於裝置的處理器速度");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(0);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        protected Void doInBackground(Void... input) {
            try {
                Document DatabaseXml = Jsoup.parse(new File(WorkingDirectory + "HKG.xml"), "UTF-8", "");
                SharedPreferences = getSharedPreferences("Database", 0);
                dbhelper = new DbHelper(getApplicationContext());
                SQLiteDatabase Database = dbhelper.getWritableDatabase();
                Database.execSQL("DELETE FROM Business");
                Database.execSQL("DELETE FROM Type");
                Database.execSQL("DELETE FROM Level");
                Database.execSQL("DELETE FROM Company");
                Database.execSQL("DELETE FROM Phone");
                Database.execSQL("DELETE FROM Phone0");
                Database.execSQL("DELETE FROM Phone1");
                Database.execSQL("DELETE FROM Phone2");
                Database.execSQL("DELETE FROM Phone3");
                Database.execSQL("DELETE FROM Phone4");
                Database.execSQL("DELETE FROM Phone5");
                Database.execSQL("DELETE FROM Phone6");
                Database.execSQL("DELETE FROM Phone7");
                Database.execSQL("DELETE FROM Phone8");
                Database.execSQL("DELETE FROM Phone9");
                Element UpdateDate = DatabaseXml.select("update").first();
                Element UpdateTime = DatabaseXml.select("time").first();
                Element UpdateVersion = DatabaseXml.select("version").first();
                Element Business = DatabaseXml.select("CompanyCat2").first();
                Elements BusinessRecords = Business.getElementsByTag("record");
                Element Type = DatabaseXml.select("catmaster2").first();
                Elements TypeRecords = Type.getElementsByTag("record");
                Element Level = DatabaseXml.select("cLevel2").first();
                Elements LevelRecords = Level.getElementsByTag("record");
                Element Company = DatabaseXml.select("transword").first();
                Elements CompanyRecords = Company.getElementsByTag("record");
                Element Phone = DatabaseXml.select("md").first();
                Elements PhoneRecords = Phone.getElementsByTag("rc");
                for (Element BusinessRecord : BusinessRecords) {
                    ContentValues Values = new ContentValues();
                    Values.put("ID", BusinessRecord.attr("id"));
                    Values.put("Name", BusinessRecord.select("name[lang=zh_hk]").text());
                    Values.put("Desc", BusinessRecord.select("desc[lang=zh_hk]").text());
                    Database.insert("Business", null, Values);
                }
                for (Element TypeRecord : TypeRecords) {
                    ContentValues Values = new ContentValues();
                    Values.put("ID", TypeRecord.attr("id"));
                    Values.put("Name", TypeRecord.select("name[lang=zh_hk]").text());
                    Values.put("Desc", TypeRecord.select("desc[lang=zh_hk]").text());
                    Database.insert("Type", null, Values);
                }
                for (Element LevelRecord : LevelRecords) {
                    ContentValues Values = new ContentValues();
                    Values.put("ID", LevelRecord.attr("id"));
                    Values.put("Name", LevelRecord.select("name[lang=zh_hk]").text());
                    Values.put("Desc", LevelRecord.select("desc[lang=zh_hk]").text());
                    Database.insert("Level", null, Values);
                }
                for (Element CompanyRecord : CompanyRecords) {
                    ContentValues Values = new ContentValues();
                    Values.put("Name", CompanyRecord.select("words").text());
                    Values.put("Business", CompanyRecord.select("ccat").text());
                    Values.put("Frequency", CompanyRecord.select("fq").text());
                    Database.insert("Company", null, Values);
                }
                int Record = 0;
                mProgressDialog.setMax(DatabaseXml.getElementsByTag("pn").size());
                for (Element PhoneRecord : PhoneRecords) {
                    ContentValues Values = new ContentValues();
                    Values.put("Record", PhoneRecord.select("rc").attr("id"));
                    Values.put("Company", PhoneRecord.select("cn").text());
                    Values.put("Number", PhoneRecord.select("pn").text());
                    Values.put("Type", PhoneRecord.select("cat").text());
                    Values.put("Date", PhoneRecord.select("ud").text());
                    Values.put("Level", PhoneRecord.select("lvl").text());
                    Values.put("Business", PhoneRecord.select("ccat").text());
                    switch (PhoneRecord.select("pn").text().substring(0,1)) {
                        case "+":
                            Database.insert("Phone", null, Values);
                            break;
                        case "0":
                            Database.insert("Phone0", null, Values);
                            break;
                        case "1":
                            Database.insert("Phone1", null, Values);
                            break;
                        case "2":
                            Database.insert("Phone2", null, Values);
                            break;
                        case "3":
                            Database.insert("Phone3", null, Values);
                            break;
                        case "4":
                            Database.insert("Phone4", null, Values);
                            break;
                        case "5":
                            Database.insert("Phone5", null, Values);
                            break;
                        case "6":
                            Database.insert("Phone6", null, Values);
                            break;
                        case "7":
                            Database.insert("Phone7", null, Values);
                            break;
                        case "8":
                            Database.insert("Phone8", null, Values);
                            break;
                        case "9":
                            Database.insert("Phone9", null, Values);
                            break;
                    }
                    publishProgress(Record++);
                }
                SharedPreferences.edit().putString("date", UpdateDate.text()).apply();
                SharedPreferences.edit().putString("time", UpdateTime.text()).apply();
                SharedPreferences.edit().putString("version", UpdateVersion.text()).apply();
                dbhelper.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(Void output) {
            mProgressDialog.dismiss();
        }
    }

    private class CheckDatabase extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("更新");
            mProgressDialog.setMessage("正在檢查最新版本資料庫...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... Url) {
            try {
                Document VersionXml = Jsoup.connect(VersionFile).get();
                Element UpdateDate = VersionXml.select("update").first();
                Element UpdateTime = VersionXml.select("time").first();
                Element UpdateVersion = VersionXml.select("version").first();
                return UpdateDate.text() + UpdateTime.text() + UpdateVersion.text();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "0000-00-00000000";
        }

        @Override
        protected void onPostExecute(String Version) {
            mProgressDialog.dismiss();
            SharedPreferences = getSharedPreferences("Database", 0);
            if (!Version.equals(SharedPreferences.getString("date", "0000-00-00") +
                    SharedPreferences.getString("time", "0000") +
                    SharedPreferences.getString("version", "00"))) {
                new DownloadDatabase().execute(BaseUrl + DatabaseFile);
            } else {
                Toast.makeText(getApplicationContext(), "資料庫無需更新", Toast.LENGTH_SHORT).show();
            }
        }
    }

}