package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityEditHikeBinding;
import com.example.m_hike.databinding.ActivityMainBinding;
import com.example.m_hike.model.Hike;

import java.util.Calendar;

public class EditHikeActivity extends AppCompatActivity {

    private ActivityEditHikeBinding binding;
    DatabaseHelper databaseHelper;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String[] difficultyList = {"Easy", "Intermediate", "Strenuous"};

    private int selectedRadioId = -1;
    RadioButton selectedRadioButton;
    private String hikeName, location, dateOfHike, lengthOfHike, difficulty, parkingAvailable, startPoint, endPoint, description;

    int id;

    Hike hike;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("hike_edit_id");

        hike = databaseHelper.getHikeWithHikeId(id);

        setUpHikeData();

        doAfterTextChange();

        binding.etHikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        // For Difficulty DropDown
        binding.actvDifficulty.setDropDownBackgroundResource(R.color.white);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, R.layout.difficulty_list_item, difficultyList);
        binding.actvDifficulty.setAdapter(difficultyAdapter);

        binding.actvDifficulty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                difficulty = parent.getItemAtPosition(position).toString();
            }
        });

        binding.mbSaveHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();

                if (hikeName.length() != 0 && location.length() != 0 && dateOfHike.length() != 0 &&
                        selectedRadioId != -1 && lengthOfHike.length() != 0 && difficulty.length() != 0) {


                    String details = "Name of Hike : " + hikeName + "\n" +
                            "Location : " + location + "\n" +
                            "Date : " + dateOfHike + "\n" +
                            "Parking Available : " + parkingAvailable + "\n" +
                            "Length of Hike : " + lengthOfHike + "\n" +
                            "Difficulty : " + difficulty + "\n";

                    startPoint = binding.etStartPoint.getText().toString();
                    endPoint = binding.etEndPoint.getText().toString();
                    description = binding.etDescription.getText().toString();
                    if (startPoint.length() != 0) {
                        details += "Start Point : " + startPoint + "\n";
                    }
                    if (endPoint.length() != 0) {
                        details += "End Point : " + endPoint + "\n";
                    }
                    if (description.length() != 0) {
                        details += "Description : " + description;
                    }

                    builder = new AlertDialog.Builder(EditHikeActivity.this);
                    builder.setTitle("Are you sure to update hiking?");
                    builder.setMessage(details);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveHikeDetails();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }

            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setUpHikeData() {

        binding.etHikeName.setText(hike.getName());
        binding.etLocation.setText(hike.getLocation());
        binding.etHikeDate.setText(hike.getDateOfHike());

        if (hike.getParkingAvailable().equals("Yes")) {
            binding.rbYes.setChecked(true);
        } else {
            binding.rbNo.setChecked(true);
        }

        binding.etLengthOfHike.setText(hike.getLengthOfHeight());
        binding.actvDifficulty.setText(hike.getDifficulty());
        binding.etStartPoint.setText(hike.getStartPoint());
        binding.etEndPoint.setText(hike.getEndPoint());
        binding.etDescription.setText(hike.getDescription());

    }

    private void checkValidation() {

        // Check Hike Name
        hikeName = binding.etHikeName.getText().toString().trim();
        if (hikeName.length() == 0) {
            binding.tilHikeName.setErrorEnabled(true);
            binding.tilHikeName.setError("Name of hike is required.");
        } else {
            binding.tilHikeName.setErrorEnabled(false);
        }

        // Check Hike Location
        location = binding.etLocation.getText().toString().trim();
        if (location.length() == 0) {
            binding.tilLocation.setErrorEnabled(true);
            binding.tilLocation.setError("Location is required.");
        } else {
            binding.tilLocation.setErrorEnabled(false);
        }

        // Check Hike Date
        dateOfHike = binding.etHikeDate.getText().toString().trim();
        if (dateOfHike.length() == 0) {
            binding.tilHikeDate.setErrorEnabled(true);
            binding.tilHikeDate.setError("Date of Hike is required.");
        } else {
            binding.tilHikeDate.setErrorEnabled(false);
        }

        // Check Parking Available
        selectedRadioId = binding.rbGroup.getCheckedRadioButtonId();
        selectedRadioButton = findViewById(selectedRadioId);
        if (selectedRadioId == -1) {
            Toast.makeText(getApplicationContext(), "Choose Parking Available", Toast.LENGTH_SHORT).show();
        } else {
            parkingAvailable = selectedRadioButton.getText().toString();
        }

        // Check Length of Hike
        lengthOfHike = binding.etLengthOfHike.getText().toString().trim();
        if (lengthOfHike.length() == 0) {
            binding.tvKilomiter.setVisibility(View.GONE);
            binding.tilLengthOfHike.setErrorEnabled(true);
            binding.tilLengthOfHike.setError("Length of Hike is required.");
        } else {
            binding.tvKilomiter.setVisibility(View.VISIBLE);
            binding.tilLengthOfHike.setErrorEnabled(false);
        }

        // Check Level of Difficulty
        difficulty = binding.actvDifficulty.getText().toString().trim();
        if (difficulty.length() == 0) {
            binding.tilDifficulty.setErrorEnabled(true);
            binding.tilDifficulty.setError("Level of Difficulty is required.");
        } else {
            binding.tilDifficulty.setErrorEnabled(false);
        }

    }

    private void openDatePicker() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, setSelectedDate(), year, month, day);
        datePickerDialog.show();

    }

    private DatePickerDialog.OnDateSetListener setSelectedDate() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = months[month] + " " + dayOfMonth + ", " + year;
                binding.etHikeDate.setText(selectedDate);
            }
        };
    }

    private void doAfterTextChange() {

        binding.etHikeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilHikeName.setErrorEnabled(true);
                    binding.tilHikeName.setError("Name of hike is required.");
                } else {
                    binding.tilHikeName.setErrorEnabled(false);
                }
            }
        });

        binding.etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilLocation.setErrorEnabled(true);
                    binding.tilLocation.setError("Location is required.");
                } else {
                    binding.tilLocation.setErrorEnabled(false);
                }
            }
        });

        binding.etHikeDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilHikeDate.setErrorEnabled(false);
            }
        });

        binding.etLengthOfHike.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tvKilomiter.setVisibility(View.GONE);
                    binding.tilLengthOfHike.setErrorEnabled(true);
                    binding.tilLengthOfHike.setError("Length of Hike is required.");
                } else {
                    binding.tvKilomiter.setVisibility(View.VISIBLE);
                    binding.tilLengthOfHike.setErrorEnabled(false);
                }
            }
        });

        binding.actvDifficulty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilDifficulty.setErrorEnabled(true);
                    binding.tilDifficulty.setError("Level of Difficulty is required.");
                } else {
                    binding.tilDifficulty.setErrorEnabled(false);
                }
            }
        });

    }

    private void saveHikeDetails() {
        databaseHelper = new DatabaseHelper(this);

        Hike hike = new Hike(id, hikeName, location, dateOfHike, parkingAvailable, lengthOfHike, difficulty, startPoint, endPoint, description);
        databaseHelper.updateHikeDetails(hike);

        Toast.makeText(this, "Successfully updated.", Toast.LENGTH_SHORT).show();

        finish();

    }
}