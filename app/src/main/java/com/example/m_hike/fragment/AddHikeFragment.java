package com.example.m_hike.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.m_hike.R;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.FragmentAddHikeBinding;
import com.example.m_hike.model.Hike;

import java.util.Calendar;


public class AddHikeFragment extends Fragment {

    private FragmentAddHikeBinding binding;
    DatabaseHelper databaseHelper;

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    private int selectedRadioId = -1;
    RadioButton selectedRadioButton;
    private String hikeName, location, dateOfHike, lengthOfHike, difficulty, parkingAvailable, startPoint, endPoint, description;

    String[] difficultyList = {"Easy", "Intermediate", "Strenuous"};

    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddHikeBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvHikingRecordTitle.setText(R.string.add_hiking_details);

        doAfterTextChange();

        binding.etHikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        // For Difficulty DropDown
        binding.actvDifficulty.setDropDownBackgroundResource(R.color.white);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(requireContext(), R.layout.difficulty_list_item, difficultyList);
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

                    builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Are you sure to save hiking?");
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

    }

    private void saveHikeDetails() {
        databaseHelper = new DatabaseHelper(requireContext());

        Hike hike = new Hike(0, hikeName, location, dateOfHike, parkingAvailable, lengthOfHike, difficulty, startPoint, endPoint, description);
        long hide_id = databaseHelper.saveHikeDetails(hike);

        if (hide_id != 0) {
            binding.etHikeName.getText().clear();
            binding.etLocation.getText().clear();
            binding.etHikeDate.getText().clear();
            binding.etLengthOfHike.getText().clear();
            binding.actvDifficulty.getText().clear();
            binding.etStartPoint.getText().clear();
            binding.etEndPoint.getText().clear();
            binding.etDescription.getText().clear();
            binding.rbGroup.clearCheck();
            Toast.makeText(requireActivity(), "Successfully Saved.", Toast.LENGTH_SHORT).show();
        }
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
        selectedRadioButton = requireActivity().findViewById(selectedRadioId);
        if (selectedRadioId == -1) {
            Toast.makeText(requireContext(), "Choose Parking Available", Toast.LENGTH_SHORT).show();
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), setSelectedDate(), year, month, day);
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

}