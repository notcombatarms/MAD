package edu.curtin.citysimulator.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.curtin.citysimulator.R;
import edu.curtin.citysimulator.model.GameData;
import edu.curtin.citysimulator.model.MapElement;
import edu.curtin.citysimulator.model.Structure;

/**
 * This activity is to view details of a block from the map, with functionality to edit the
 * name of the block and take photo to update the image of the block.
 * Third screen of the app.
 *
 * @author Tao Hu
 * date:    31/10/2020
 */
public class DetailActivity extends AppCompatActivity
{
    //activity requires the row and col to access the map element
    public static final String DETAILS_ROW = "edu.curtin.detailsRow";
    public static final String DETAILS_COL = "edu.curtin.detailsCol";

    //photoTaken need to persist through recreations when screen rotates.
    //photoTaken tells the map fragment whether a photo has being taken, whether the map fragment
    //should update what was displayed for that block.
    private static final String PHOTO_TAKEN = "edu.curtin.phototaken";

    private static final int REQUEST_THUMBNAIL = 1;

    private TextView tvRow;
    private TextView tvColumn;
    private TextView tvStructureType;
    private TextView tvEditableName;
    private EditText etEditableName;
    private Button btnUpdateEditableName;
    private Button btnTakePhoto;
    private ImageView ivPhotoTaken;

    private Intent intentTakePhoto;

    private MapElement mapElement;

    private boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null)
        {
            photoTaken = savedInstanceState.getBoolean(PHOTO_TAKEN);
        }

        Intent intent = getIntent();

        int row = intent.getIntExtra(DETAILS_ROW, 0);
        int col = intent.getIntExtra(DETAILS_COL, 0);

        mapElement = GameData.getInstance().getMapElement(row, col);

        findView();
        initView(row, col);

        //check if phone can take photo if not display the button to take photo.
        intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager pm = getPackageManager();

        if (pm.resolveActivity(intentTakePhoto, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            btnTakePhoto.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //photo was taken
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ivPhotoTaken.setImageBitmap(thumbnail);
            mapElement.setImage(thumbnail);

            photoTaken = true;
        }
    }

    @Override
    public void onBackPressed()
    {
        //if photo is taken then signal the map the update its viewHolder.
        //the map fragment will test resultCode, RESULT_OK = photo was taken and updated.
        int resultCode = Activity.RESULT_CANCELED;

        if (photoTaken)
        {
            resultCode = Activity.RESULT_OK;
        }

        setResult(resultCode, new Intent());

        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(PHOTO_TAKEN, photoTaken);

        super.onSaveInstanceState(outState);
    }

    public static Intent getIntent(int row, int col, Context context)
    {
        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(DETAILS_ROW, row);
        intent.putExtra(DETAILS_COL, col);

        return intent;
    }

    private void findView()
    {
        tvRow = findViewById(R.id.tvRow);
        tvColumn = findViewById(R.id.tvCol);
        tvStructureType = findViewById(R.id.tvStructureType);
        tvEditableName = findViewById(R.id.tvName);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        ivPhotoTaken = findViewById(R.id.ivPhotoTaken);
        etEditableName = findViewById(R.id.etEditableName);
        btnUpdateEditableName = findViewById(R.id.btnUpdateEditableName);
    }

    private void initView(int row, int col)
    {
        tvRow.setText(getString(R.string.detailsRow, row + 1));
        tvColumn.setText(getString(R.string.detailsCol, col + 1));
        tvStructureType.setText(getString(R.string.detailsStructureType, Structure.getStructureType(mapElement.getStructure())));
        String editableName = mapElement.getEditableName();

        if (editableName == null)
        {
            editableName = Structure.getStructureType(mapElement.getStructure());
        }

        tvEditableName.setText(getString(R.string.detailsEditableName, editableName));

        btnUpdateEditableName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = etEditableName.getText().toString();
                tvEditableName.setText(getString(R.string.detailsEditableName, name));
                mapElement.setEditableName(name);
                etEditableName.setText("");
            }
        });

        if (mapElement.getImage() != null)
        {
            ivPhotoTaken.setImageBitmap(mapElement.getImage());
        }

        btnTakePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(intentTakePhoto, REQUEST_THUMBNAIL);
            }
        });
    }
}