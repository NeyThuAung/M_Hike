package com.example.m_hike;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityAddObservationBinding;
import com.example.m_hike.model.Hike;
import com.example.m_hike.model.Observation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddObservationActivity extends AppCompatActivity {

    private ActivityAddObservationBinding binding;

    int hikeId;

    AlertDialog.Builder builder;
    DatabaseHelper databaseHelper;

    private String animals, vagetation, weather, trails, date, time, comments;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] weatherList = {"Clear", "Misty", "Cloudy", "Rain", "Snow", "Windy", "Storm"};

    Bitmap bitmapImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddObservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvObservationFormTitle.setText(R.string.add_observation);

        Intent intent = getIntent();
        hikeId = intent.getExtras().getInt("hike_edit_id");

        doAfterTextChange();

        // For Difficulty DropDown
        binding.actvWeatherConditions.setDropDownBackgroundResource(R.color.white);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, R.layout.difficulty_list_item, weatherList);
        binding.actvWeatherConditions.setAdapter(difficultyAdapter);

        binding.actvWeatherConditions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weather = parent.getItemAtPosition(position).toString();
            }
        });

        // Current date and time in initial
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);

        binding.etObservationDate.setText(formattedDate);
        binding.etObservationTime.setText(formattedTime);

        binding.etObservationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        binding.etObservationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.mbSaveObservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();

                if (animals.length() != 0 && vagetation.length() != 0 && weather.length() != 0 && trails.length() != 0 && date.length() != 0 && time.length() != 0) {


                    String details = "Sightings of Animals : " + animals + "\n" +
                            "Types of vagetation : " + vagetation + "\n" +
                            "Weather Conditions : " + weather + "\n" +
                            "Conditions of trails : " + trails + "\n" +
                            "Observation Date : " + date + "\n" +
                            "Observation Time : " + time + "\n";

                    comments = binding.etAdditional.getText().toString();

                    if (comments.length() != 0) {
                        details += "Comments : " + comments + "\n";
                    }

                    builder = new AlertDialog.Builder(AddObservationActivity.this);
                    builder.setTitle("Are you sure to save observation?");
                    builder.setMessage(details);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveObservationDetails();
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

        binding.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(openCamera,100);
                activityResultLauncher.launch(openCamera);
            }
        });

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap photo = (Bitmap) bundle.get("data");
                    binding.ivCamera.setVisibility(View.GONE);
                    binding.ivSelectedImage.setVisibility(View.VISIBLE);
                    binding.ivSelectedImage.setImageBitmap(photo);
                    bitmapImage = photo;
                }
            }
    );

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap photo = (Bitmap) data.getExtras().get("data");
//        binding.ivCamera.setVisibility(View.GONE);
//        binding.ivSelectedImage.setVisibility(View.VISIBLE);
//        binding.ivSelectedImage.setImageBitmap(photo);
//        bitmapImage = photo;
//    }

    private void saveObservationDetails() {

        databaseHelper = new DatabaseHelper(this);

        Observation observation = new Observation(0, animals, vagetation, weather, trails, date, time, comments, bitmapImage, hikeId);
        long observation_id = databaseHelper.saveObservation(observation);

        if (observation_id != 0) {
            binding.etSightingsOfAnimals.getText().clear();
            binding.etTypesOfVegetation.getText().clear();
            binding.actvWeatherConditions.getText().clear();
            binding.etConditionsOfTrails.getText().clear();
            binding.etObservationDate.getText().clear();
            binding.etObservationTime.getText().clear();
            binding.etAdditional.getText().clear();
            Toast.makeText(this, "Successfully Saved.", Toast.LENGTH_SHORT).show();
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
                binding.etObservationDate.setText(selectedDate);
            }
        };
    }

    public void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                String amPm;
                if (selectedHour >= 12) {
                    amPm = "PM";
                    if (selectedHour > 12) {
                        selectedHour -= 12;
                    }
                } else {
                    amPm = "AM";
                }

                String selectedTime = String.format("%02d:%02d %s", selectedHour, selectedMinute, amPm);

                binding.etObservationTime.setText(selectedTime);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    private void checkValidation() {

        // Check animals
        animals = binding.etSightingsOfAnimals.getText().toString().trim();
        if (animals.length() == 0) {
            binding.tilSightingsOfAnimals.setErrorEnabled(true);
            binding.tilSightingsOfAnimals.setError("Sighting of Animals is required.");
        } else {
            binding.tilSightingsOfAnimals.setErrorEnabled(false);
        }

        // Check vegatation
        vagetation = binding.etTypesOfVegetation.getText().toString().trim();
        if (vagetation.length() == 0) {
            binding.tilTypesOfVegetation.setErrorEnabled(true);
            binding.tilTypesOfVegetation.setError("Types of Vagetation is required.");
        } else {
            binding.tilTypesOfVegetation.setErrorEnabled(false);
        }

        // Check weather condition
        weather = binding.actvWeatherConditions.getText().toString().trim();
        if (weather.length() == 0) {
            binding.tilWeatherConditions.setErrorEnabled(true);
            binding.tilWeatherConditions.setError("Weather conditions is required.");
        } else {
            binding.tilWeatherConditions.setErrorEnabled(false);
        }

        // Check trails
        trails = binding.etConditionsOfTrails.getText().toString().trim();
        if (trails.length() == 0) {
            binding.tilConditionsOfTrails.setErrorEnabled(true);
            binding.tilConditionsOfTrails.setError("Conditions of the trails is required.");
        } else {
            binding.tilConditionsOfTrails.setErrorEnabled(false);
        }

        // Check date
        date = binding.etObservationDate.getText().toString().trim();
        if (date.length() == 0) {
            binding.tilObservationDate.setErrorEnabled(true);
            binding.tilObservationDate.setError("Observation date is required.");
        } else {
            binding.tilObservationDate.setErrorEnabled(false);
        }

        // Check time
        time = binding.etObservationTime.getText().toString().trim();
        if (time.length() == 0) {
            binding.tilObservationTime.setErrorEnabled(true);
            binding.tilObservationTime.setError("Observation time is required.");
        } else {
            binding.tilObservationTime.setErrorEnabled(false);
        }

    }

    private void doAfterTextChange() {

        binding.etSightingsOfAnimals.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilSightingsOfAnimals.setErrorEnabled(true);
                    binding.tilSightingsOfAnimals.setError("Sighting of Animals is required.");
                } else {
                    binding.tilSightingsOfAnimals.setErrorEnabled(false);
                }
            }
        });

        binding.etTypesOfVegetation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilTypesOfVegetation.setErrorEnabled(true);
                    binding.tilTypesOfVegetation.setError("Types of Vagetation is required.");
                } else {
                    binding.tilTypesOfVegetation.setErrorEnabled(false);
                }
            }
        });

        binding.actvWeatherConditions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilWeatherConditions.setErrorEnabled(false);
            }
        });

        binding.etConditionsOfTrails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilConditionsOfTrails.setErrorEnabled(false);
            }
        });

        binding.etObservationDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilObservationDate.setErrorEnabled(true);
                    binding.tilObservationDate.setError("Observation date is required.");
                } else {
                    binding.tilObservationDate.setErrorEnabled(false);
                }
            }
        });

        binding.etObservationTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tilObservationTime.setErrorEnabled(true);
                    binding.tilObservationTime.setError("Observation time is required.");
                } else {
                    binding.tilObservationTime.setErrorEnabled(false);
                }
            }
        });

    }

}