package kr.spyec.workdevman.Activity.DialogActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.spyec.workdevman.Define.CDLockType;
import kr.spyec.workdevman.HttpService.API.GitRepoData;
import kr.spyec.workdevman.HttpService.API.GitRepoListService;
import kr.spyec.workdevman.HttpService.NetServiceFactory;
import kr.spyec.workdevman.R;
import retrofit.Call;

public class SelectRepoActivity extends AppCompatActivity {

    SharedPreferences pref;
    ListView listView;
    CustomAdapter adapter;
    String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_repo);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        accessToken = pref.getString("accessToken", "");

        listView = (ListView) findViewById(R.id.list_git_repo);

        adapter = new CustomAdapter(this, new ArrayList<GitRepoData>());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectRepoActivity.this, SelectLockDayActivity.class);
                intent.putExtra("lockType", CDLockType.REPO_LOCK);
                intent.putExtra("repoName", adapter.getItem(position).getName());
                startActivity(intent);
                finish();
            }
        });

        new NetTask().execute();
    }

    private class CustomAdapter extends ArrayAdapter<GitRepoData>{
        LayoutInflater inflater;
        public CustomAdapter(Context context, List<GitRepoData> items) {
            super(context, 0, items);
            inflater = LayoutInflater.from(context);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = inflater.inflate(R.layout.item_repo_list, parent, false);

            GitRepoData item = this.getItem(position);
            ((TextView)convertView.findViewById(R.id.text_repo_url)).setText(item.getUrl());
            ((TextView)convertView.findViewById(R.id.text_repo_name)).setText(item.getName());
            ((TextView)convertView.findViewById(R.id.text_repo_description)).setText(item.getDescription());

            return convertView;
        }
    }


    private class NetTask extends AsyncTask<Void, Void, List<GitRepoData>> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SelectRepoActivity.this, "", "서버에서 정보를 받아오는 중입니다...", true);
        }

        @Override
        protected List<GitRepoData> doInBackground(Void... params) {

            Call<List<GitRepoData>> call = NetServiceFactory.createAPIService(GitRepoListService.class).loadRepoList(accessToken, "pushed");
            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<GitRepoData> result) {

            if(result == null) {
                Snackbar.make(listView, "GitHub 데이터를 받아오지 못했습니다.\n네트워크 상태를 확인해주세요.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
                adapter.addAll(result);
                adapter.notifyDataSetChanged();
            }
            mProgressDialog.dismiss();

        }
    }
}
