package d14cqcp01.group5.democustomlistview;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends ArrayAdapter<DanhBa> {

    private Activity context;
    private int layout;
    private List<DanhBa> danhBaList;

    public CustomAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<DanhBa> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.danhBaList = objects;
    }

    @Override
    public int getCount() {
        return danhBaList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater  = this.context.getLayoutInflater();
        View view = inflater.inflate(layout,null);
        TextView txtHoten = view.findViewById(R.id.edtName);
        TextView txtPhone = view.findViewById(R.id.edtPhone);
        CircleImageView imgAvartar = view.findViewById(R.id.imgAvatar);

        DanhBa danhBa= danhBaList.get(position);

        txtHoten.setText(danhBa.getHoten());
        txtPhone.setText(danhBa.getPhone());
        Picasso.get().load(danhBa.getAvartar()).into(imgAvartar);

        return view;
    }
}
