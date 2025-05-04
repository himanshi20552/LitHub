package com.example.myapplication;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CabinAdapter extends RecyclerView.Adapter<CabinAdapter.CabinViewHolder> {
    private List<Cabin> cabinList;
    private Context context;

    public CabinAdapter(List<Cabin> cabinList, Context context) {
        this.cabinList = cabinList;
        this.context = context;
    }

    @NonNull
    @Override
    public CabinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cabin, parent, false);
        return new CabinViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CabinViewHolder holder, int position) {
        try {
            Cabin cabin = cabinList.get(position);
            System.out.println("Binding cabin at position " + position + ": " + cabin.name);
            
            holder.name.setText(cabin.name);
            holder.capacity.setText("Capacity: " + cabin.capacity);

            holder.bookBtn.setOnClickListener(v -> {
                showBookingDialog(cabin);
            });
        } catch (Exception e) {
            System.out.println("Error binding cabin at position " + position + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showBookingDialog(Cabin cabin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.booking_form_dialog, null);
        builder.setView(dialogView);

        EditText en1 = dialogView.findViewById(R.id.en1);
        EditText en2 = dialogView.findViewById(R.id.en2);
        EditText en3 = dialogView.findViewById(R.id.en3);
        EditText en4 = dialogView.findViewById(R.id.en4);
        Button startTimeButton = dialogView.findViewById(R.id.start_time_button);
        Button endTimeButton = dialogView.findViewById(R.id.end_time_button);
        TextView selectedStartTime = dialogView.findViewById(R.id.selected_start_time);
        TextView selectedEndTime = dialogView.findViewById(R.id.selected_end_time);
        Button submitBtn = dialogView.findViewById(R.id.submit_booking_button);

        // Initialize time variables
        final int[] startHour = new int[1];
        final int[] startMinute = new int[1];
        final int[] endHour = new int[1];
        final int[] endMinute = new int[1];
        selectedStartTime.setText("No start time selected");
        selectedEndTime.setText("No end time selected");

        // Start time picker click listener
        startTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    startHour[0] = hourOfDay;
                    startMinute[0] = minute;
                    String amPm = hourOfDay < 12 ? "AM" : "PM";
                    int hour12 = hourOfDay % 12;
                    if (hour12 == 0) hour12 = 12;
                    String timeString = String.format("%d:%02d %s", hour12, minute, amPm);
                    selectedStartTime.setText("Start Time: " + timeString);
                },
                9, // Default hour (9 AM)
                0, // Default minute
                false // 12-hour format
            );
            timePickerDialog.setTitle("Select Start Time");
            timePickerDialog.show();
        });

        // End time picker click listener
        endTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    endHour[0] = hourOfDay;
                    endMinute[0] = minute;
                    String amPm = hourOfDay < 12 ? "AM" : "PM";
                    int hour12 = hourOfDay % 12;
                    if (hour12 == 0) hour12 = 12;
                    String timeString = String.format("%d:%02d %s", hour12, minute, amPm);
                    selectedEndTime.setText("End Time: " + timeString);
                },
                10, // Default hour (10 AM)
                0, // Default minute
                false // 12-hour format
            );
            timePickerDialog.setTitle("Select End Time");
            timePickerDialog.show();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        submitBtn.setOnClickListener(v -> {
            String enr1 = en1.getText().toString();
            String enr2 = en2.getText().toString();
            String enr3 = en3.getText().toString();
            String enr4 = en4.getText().toString();
            String startTimeText = selectedStartTime.getText().toString();
            String endTimeText = selectedEndTime.getText().toString();

            if (enr1.isEmpty() || enr2.isEmpty() || enr3.isEmpty() || enr4.isEmpty() || 
                startTimeText.equals("No start time selected") || 
                endTimeText.equals("No end time selected")) {
                Toast.makeText(context, "Please fill all fields and select time range", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate time range
            int startTimeInMinutes = startHour[0] * 60 + startMinute[0];
            int endTimeInMinutes = endHour[0] * 60 + endMinute[0];
            
            if (endTimeInMinutes <= startTimeInMinutes) {
                Toast.makeText(context, "End time must be after start time", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            String bookingKey = ref.child("bookings").push().getKey();
            
            // Format times for display and storage
            String startAmPm = startHour[0] < 12 ? "AM" : "PM";
            String endAmPm = endHour[0] < 12 ? "AM" : "PM";
            int startHour12 = startHour[0] % 12;
            int endHour12 = endHour[0] % 12;
            if (startHour12 == 0) startHour12 = 12;
            if (endHour12 == 0) endHour12 = 12;
            
            String startTimeString = String.format("%d:%02d %s", startHour12, startMinute[0], startAmPm);
            String endTimeString = String.format("%d:%02d %s", endHour12, endMinute[0], endAmPm);

            Map<String, Object> booking = new HashMap<>();
            booking.put("cabinNumber", cabin.name.toLowerCase().replace(" ", ""));
            booking.put("enrollment1", enr1);
            booking.put("enrollment2", enr2);
            booking.put("enrollment3", enr3);
            booking.put("enrollment4", enr4);
            booking.put("startTime", startTimeString);
            booking.put("endTime", endTimeString);

            // Update both booking and cabin status in a single transaction
            Map<String, Object> updates = new HashMap<>();
            updates.put("/bookings/" + bookingKey, booking);
            updates.put("/cabins/" + cabin.name.toLowerCase().replace(" ", "") + "/isBooked", true);
            updates.put("/cabins/" + cabin.name.toLowerCase().replace(" ", "") + "/bookings/" + bookingKey, booking);

            ref.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    Toast.makeText(context, "Cabin Booked Successfully!", Toast.LENGTH_SHORT).show();
                    // Remove the booked cabin from the list
                    int position = cabinList.indexOf(cabin);
                    if (position != -1) {
                        cabinList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to book cabin. Please try again.", Toast.LENGTH_SHORT).show();
                });
        });
    }

    @Override
    public int getItemCount() {
        int count = cabinList.size();
        System.out.println("Adapter item count: " + count);
        return count;
    }

    public static class CabinViewHolder extends RecyclerView.ViewHolder {
        TextView name, capacity;
        Button bookBtn;

        public CabinViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cabinName);
            capacity = itemView.findViewById(R.id.cabinCapacity);
            bookBtn = itemView.findViewById(R.id.bookButton);
        }
    }
}

