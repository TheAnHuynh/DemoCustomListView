package d14cqcp01.group5.democustomlistview;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String DANHBA = "DANHBA";
    public static final int REQUEST_CODE = 100;
    public static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private CustomAdapter adapter;
    private ArrayList<DanhBa> danhBaList;

    DatabaseReference mData;
    StorageReference storageRef;

    private EditText edtPhone, edtName;
    private CircleImageView avatar;
    private Switch swtThem;
    private Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference("/image");
        //uploadData();
        listView = findViewById(R.id.listview);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        avatar = findViewById(R.id.imgAvatar);
        swtThem = findViewById(R.id.swtThem);
        btnThem = findViewById(R.id.btnThem);


        danhBaList = new ArrayList<>();
        adapter = new CustomAdapter(MainActivity.this,R.layout.custom_layout, danhBaList);
        listView.setAdapter(adapter);

        mData.child(DANHBA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot db : dataSnapshot.getChildren()){
                    DanhBa a = db.getValue(DanhBa.class);
                    danhBaList.add(a);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        swtThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtThem.isChecked()){
                    edtName.setEnabled(true);
                    edtPhone.setEnabled(true);
                }
                else {
                    edtName.setText("");
                    edtPhone.setText("");
                    avatar.setImageResource(R.mipmap.ic_launcher_round);
                    edtName.setEnabled(false);
                    edtPhone.setEnabled(false);
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtThem.isChecked()){
                    //Todo: mở giao diện chọn hình ảnh.
                    try{
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(i,"Chọn ảnh đại diện"), REQUEST_CODE);
                    }catch (Exception e){
                        Log.d(TAG, e.toString());
                    }
                }
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swtThem.isChecked()){
                    final String name = edtName.getText().toString().trim();
                    final String phone = edtPhone.getText().toString().trim();

                    if(name.isEmpty() || phone.isEmpty()){
                        Toast.makeText(MainActivity.this, "Nhập đủ Tên và Số Điện Thoại", Toast.LENGTH_SHORT).show();
                    }else{
                        avatar.setDrawingCacheEnabled(true);
                        avatar.buildDrawingCache();
                        Bitmap anh = avatar.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        anh.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        Calendar calendar = Calendar.getInstance();
                        final String photoName = "image" + calendar.getTimeInMillis() + ".png";

                        StorageReference imgRef = storageRef.child(photoName);
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("image/png")
                                .build();
                        UploadTask uploadTask = imgRef.putBytes(data,metadata);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                DanhBa db = new DanhBa(phone,name,downloadUrl.toString());
                                mData.child(DANHBA).push().setValue(db);
                                Toast.makeText(MainActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                                swtThem.setChecked(false);
                                edtName.setText("");
                                edtPhone.setText("");
                                avatar.setImageResource(R.mipmap.ic_launcher_round);
                                edtName.setEnabled(false);
                                edtPhone.setEnabled(false);
                            }
                        });
                    }
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                avatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadData(){
        DanhBa db1 = new DanhBa("01220 xxx xxx","Phạm Thị Thu Thảo",
                "https://firebasestorage.googleapis.com/v0/b/democustomlistview-db7f1.appspot.com/o/image%2F21014010_1502187323157759_1650711566686879595_o.png?alt=media&token=3260d5b5-bfd5-441f-8d5f-cef36a2b5310");
        DanhBa db2 = new DanhBa("01937 xxx xxx","Nguyễn Thị Thu Thảo",
                "https://firebasestorage.googleapis.com/v0/b/democustomlistview-db7f1.appspot.com/o/image%2F22815086_474010416306546_4531836460605842533_n.png?alt=media&token=c54f94eb-db96-4ae2-b24d-aabb523a54a5");
        DanhBa db3 = new DanhBa("01636 xxx xxx","Isla",
                "https://firebasestorage.googleapis.com/v0/b/democustomlistview-db7f1.appspot.com/o/image%2FIsla.jpg?alt=media&token=fb60bd7c-d764-4eb4-b9ef-b0a990b063ac");

        mData.child(DANHBA).push().setValue(db1);
        mData.child(DANHBA).push().setValue(db2);
        mData.child(DANHBA).push().setValue(db3);
    }

}