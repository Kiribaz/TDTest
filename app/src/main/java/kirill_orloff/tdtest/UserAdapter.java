package kirill_orloff.tdtest;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Kirill Orloff on 25.07.17.
 */

class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserModel> users;
    private Context context;

    UserAdapter(List<UserModel> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = users.get(position);
        Resources resources = context.getResources();

        // Возраст
        holder.user_age.setText(String.valueOf(user.getAge()) + " " + resources.getString(R.string.years) + ",");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        try {
            Date lastSeenDate = format.parse(user.getLastSeen());
            long lastSeenDateInMills = lastSeenDate.getTime();
            Date todayDate = new Date();
            long todayDateInMills = todayDate.getTime();
            // 86400000 - количество миллисекунд в 1 сутках
            // Прошло менее суток
            if (todayDateInMills - lastSeenDateInMills < 86400000) {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(lastSeenDate);
                holder.user_lastSeen.setText(resources.getString(R.string.today) + ", " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            } else {
                // Прошло более 1 суток, но меньше 2 суток
                if ((todayDateInMills - lastSeenDateInMills >= 86400000) && (todayDateInMills - lastSeenDateInMills < 86400000 * 2)) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(lastSeenDate);
                    holder.user_lastSeen.setText(resources.getString(R.string.yesterday) + ", " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                } else {
                    // Прошло более 2 суток
                    if (todayDateInMills - lastSeenDateInMills > 86400000 * 2) {
                        holder.user_lastSeen.setText(user.getLastSeen());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Имя
        holder.user_name.setText(user.getName());

        // Similarity
        holder.user_similarity.setText(resources.getString(R.string.similarity, user.getSimilarity()));
        if (user.getSimilarity() < 40) {
            holder.user_similarity.setTextColor(Color.RED);
        } else {
            if ((user.getSimilarity() >= 40) && (user.getSimilarity() < 70)) {
                holder.user_similarity.setTextColor(Color.YELLOW);
            } else {
                if (user.getSimilarity() > 70) {
                    holder.user_similarity.setTextColor(Color.GREEN);
                }
            }
        }

        // Статус
        GradientDrawable gd = (GradientDrawable) holder.user_statusCircle.getDrawable().getCurrent();
        if (user.getStatus().equals("dnd")) {
            gd.setColor(Color.RED);
        } else {
            if (user.getStatus().equals("away")) {
                gd.setColor(Color.YELLOW);

            } else {
                if (user.getStatus().equals("online")) {
                    gd.setColor(Color.GREEN);
                }
            }
        }

        // Непрочитанные сообщения
        if (user.getUnreadMessages() == 0) {
            holder.user_unreadMessages.setVisibility(View.GONE);
            holder.user_unreadMessagesCircle.setVisibility(View.GONE);
        } else {
            if (user.getUnreadMessages() > 999) {
                holder.user_unreadMessages.setText(resources.getString(R.string.many_messages));
            } else {
                holder.user_unreadMessages.setText(String.valueOf(user.getUnreadMessages()));
            }
        }

        // Аватар
        Picasso.with(this.context).load(user.getAvatar()).transform(new ImageCircleTransform()).into(holder.user_avatar);
    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_age;
        TextView user_lastSeen;
        TextView user_name;
        TextView user_similarity;
        TextView user_unreadMessages;
        ImageView user_avatar;
        ImageView user_statusCircle;
        ImageView user_unreadMessagesCircle;

        ViewHolder(View itemView) {
            super(itemView);
            user_age = (TextView) itemView.findViewById(R.id.user_age);
            user_lastSeen = (TextView) itemView.findViewById(R.id.user_lastSeen);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_similarity = (TextView) itemView.findViewById(R.id.user_similarity);
            user_unreadMessages = (TextView) itemView.findViewById(R.id.user_unreadMessages);
            user_avatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            user_statusCircle = (ImageView) itemView.findViewById(R.id.user_statusCircle);
            user_unreadMessagesCircle = (ImageView) itemView.findViewById(R.id.user_unreadMessagesCircle);
        }
    }
}