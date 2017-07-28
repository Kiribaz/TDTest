package kirill_orloff.tdtest;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<UserModel> users;
    Context context = this;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    UserGetInterface UserGetInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://a11d.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserGetInterface = retrofit.create(UserGetInterface.class);

        users = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        UserAdapter adapter = new UserAdapter(users, context);
        recyclerView.setAdapter(adapter);

        getUsers();
    }

    public void getUsers() {
        UserGetInterface.getData().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (users.size() != 0) {
                    users.clear();
                }
                users.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
