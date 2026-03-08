package com.mocktail.app.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mocktail.app.R;
import com.mocktail.app.models.Address;

import java.util.ArrayList;
import java.util.List;

public class SavedAddressesActivity extends AppCompatActivity {

    private LinearLayout addressesContainer;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_addresses);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        addressesContainer = findViewById(R.id.ll_addresses_container);

        // Initialize sample addresses
        addresses = new ArrayList<>();
        addresses.add(new Address("Home", "home",
                "123 Premium Lane, Luxury Heights",
                "Manhattan, NY 10001", true));
        addresses.add(new Address("Office", "office",
                "456 Business Park, Suite 200",
                "Financial District, NY 10012", false));
        addresses.add(new Address("Gym", "gym",
                "789 Fitness St, Wellness District",
                "Brooklyn, NY 11201", false));

        // Add New Address button
        findViewById(R.id.btn_add_address).setOnClickListener(v -> {
            showAddressDialog(null, -1);
        });

        renderAddresses();
    }

    private void showAddressDialog(Address editAddress, int editIndex) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_address);

        // Make dialog background transparent so our rounded corners show
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
        EditText etName = dialog.findViewById(R.id.et_address_name);
        EditText etLine1 = dialog.findViewById(R.id.et_address_line1);
        EditText etLine2 = dialog.findViewById(R.id.et_address_line2);

        FrameLayout btnTypeHome = dialog.findViewById(R.id.btn_type_home);
        FrameLayout btnTypeOffice = dialog.findViewById(R.id.btn_type_office);
        FrameLayout btnTypeGym = dialog.findViewById(R.id.btn_type_gym);

        FrameLayout flCheckbox = dialog.findViewById(R.id.fl_checkbox);
        ImageView ivCheck = dialog.findViewById(R.id.iv_check);
        LinearLayout btnToggleDefault = dialog.findViewById(R.id.btn_toggle_default);

        // Track selected type
        final String[] selectedType = {"home"};
        final boolean[] isDefaultChecked = {false};

        // Type selector logic
        View.OnClickListener typeClickListener = v -> {
            // Reset all to inactive
            btnTypeHome.setBackgroundResource(R.drawable.bg_btn_default_inactive);
            btnTypeOffice.setBackgroundResource(R.drawable.bg_btn_default_inactive);
            btnTypeGym.setBackgroundResource(R.drawable.bg_btn_default_inactive);

            // Style inactive icons/text
            styleTypeButton(btnTypeHome, false);
            styleTypeButton(btnTypeOffice, false);
            styleTypeButton(btnTypeGym, false);

            // Set active
            FrameLayout clicked = (FrameLayout) v;
            clicked.setBackgroundResource(R.drawable.bg_btn_default_active);
            styleTypeButton(clicked, true);

            if (v.getId() == R.id.btn_type_home) selectedType[0] = "home";
            else if (v.getId() == R.id.btn_type_office) selectedType[0] = "office";
            else if (v.getId() == R.id.btn_type_gym) selectedType[0] = "gym";
        };

        btnTypeHome.setOnClickListener(typeClickListener);
        btnTypeOffice.setOnClickListener(typeClickListener);
        btnTypeGym.setOnClickListener(typeClickListener);

        // Default checkbox toggle
        btnToggleDefault.setOnClickListener(v -> {
            isDefaultChecked[0] = !isDefaultChecked[0];
            ivCheck.setVisibility(isDefaultChecked[0] ? View.VISIBLE : View.GONE);
            flCheckbox.setBackgroundResource(isDefaultChecked[0]
                    ? R.drawable.bg_btn_default_active
                    : R.drawable.bg_btn_default_inactive);
        });

        // If editing, pre-fill fields
        if (editAddress != null) {
            tvTitle.setText("Edit Address");
            etName.setText(editAddress.getName());
            etLine1.setText(editAddress.getLine1());
            etLine2.setText(editAddress.getLine2());
            isDefaultChecked[0] = editAddress.isDefault();
            ivCheck.setVisibility(isDefaultChecked[0] ? View.VISIBLE : View.GONE);
            if (isDefaultChecked[0]) {
                flCheckbox.setBackgroundResource(R.drawable.bg_btn_default_active);
            }

            // Set correct type button active
            selectedType[0] = editAddress.getType();
            btnTypeHome.setBackgroundResource(R.drawable.bg_btn_default_inactive);
            btnTypeOffice.setBackgroundResource(R.drawable.bg_btn_default_inactive);
            btnTypeGym.setBackgroundResource(R.drawable.bg_btn_default_inactive);
            styleTypeButton(btnTypeHome, false);
            styleTypeButton(btnTypeOffice, false);
            styleTypeButton(btnTypeGym, false);

            switch (editAddress.getType()) {
                case "office":
                    btnTypeOffice.setBackgroundResource(R.drawable.bg_btn_default_active);
                    styleTypeButton(btnTypeOffice, true);
                    break;
                case "gym":
                    btnTypeGym.setBackgroundResource(R.drawable.bg_btn_default_active);
                    styleTypeButton(btnTypeGym, true);
                    break;
                default:
                    btnTypeHome.setBackgroundResource(R.drawable.bg_btn_default_active);
                    styleTypeButton(btnTypeHome, true);
                    break;
            }

            dialog.findViewById(R.id.btn_save).setOnClickListener(sv -> {
                ((TextView) dialog.findViewById(R.id.btn_save)).setText("Update");
            });
        }

        // Cancel
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        // Save / Update
        dialog.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String line1 = etLine1.getText().toString().trim();
            String line2 = etLine2.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Please enter a name");
                etName.requestFocus();
                return;
            }
            if (line1.isEmpty()) {
                etLine1.setError("Please enter address");
                etLine1.requestFocus();
                return;
            }
            if (line2.isEmpty()) {
                etLine2.setError("Please enter city/state");
                etLine2.requestFocus();
                return;
            }

            // If setting as default, clear others first
            if (isDefaultChecked[0]) {
                for (Address a : addresses) a.setDefault(false);
            }

            if (editAddress != null && editIndex >= 0) {
                // Update existing
                Address updated = addresses.get(editIndex);
                updated.setName(name);
                updated.setType(selectedType[0]);
                updated.setLine1(line1);
                updated.setLine2(line2);
                updated.setDefault(isDefaultChecked[0]);
                Toast.makeText(this, "Address updated!", Toast.LENGTH_SHORT).show();
            } else {
                // Add new
                Address newAddr = new Address(name, selectedType[0], line1, line2, isDefaultChecked[0]);
                addresses.add(newAddr);
                Toast.makeText(this, "Address added!", Toast.LENGTH_SHORT).show();
            }

            renderAddresses();
            dialog.dismiss();
        });

        // Update button text for edit mode
        if (editAddress != null) {
            ((TextView) dialog.findViewById(R.id.btn_save)).setText("Update Address");
        }

        dialog.show();
    }

    private void styleTypeButton(FrameLayout btn, boolean active) {
        LinearLayout inner = (LinearLayout) btn.getChildAt(0);
        if (inner == null) return;
        int color = active ? 0xFFFFB300 : 0xFF8B93A6;
        for (int i = 0; i < inner.getChildCount(); i++) {
            View child = inner.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(color);
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }
    }

    private void renderAddresses() {
        addressesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            View card = inflater.inflate(R.layout.item_address, addressesContainer, false);

            // Set icon based on type
            ImageView ivIcon = card.findViewById(R.id.iv_address_icon);
            switch (address.getType()) {
                case "home":
                    ivIcon.setImageResource(R.drawable.ic_home);
                    break;
                case "office":
                    ivIcon.setImageResource(R.drawable.ic_briefcase);
                    break;
                case "gym":
                    ivIcon.setImageResource(R.drawable.ic_fitness);
                    break;
                default:
                    ivIcon.setImageResource(R.drawable.ic_location_pin);
                    break;
            }

            // Set name
            TextView tvName = card.findViewById(R.id.tv_address_name);
            tvName.setText(address.getName());

            // Set DEFAULT badge visibility
            TextView tvBadge = card.findViewById(R.id.tv_default_badge);
            tvBadge.setVisibility(address.isDefault() ? View.VISIBLE : View.GONE);

            // Set address lines
            ((TextView) card.findViewById(R.id.tv_address_line1)).setText(address.getLine1());
            ((TextView) card.findViewById(R.id.tv_address_line2)).setText(address.getLine2());

            // Configure Default button state
            LinearLayout btnSetDefault = card.findViewById(R.id.btn_set_default);
            TextView tvDefaultText = card.findViewById(R.id.tv_default_text);

            if (address.isDefault()) {
                btnSetDefault.setBackgroundResource(R.drawable.bg_btn_default_active);
                tvDefaultText.setText("Default");
                tvDefaultText.setTextColor(0xFFFFB300);
            } else {
                btnSetDefault.setBackgroundResource(R.drawable.bg_btn_default_inactive);
                tvDefaultText.setText("Set as Default");
                tvDefaultText.setTextColor(0xFF8B93A6);
            }

            // Set as Default click
            final int idx = i;
            btnSetDefault.setOnClickListener(v -> {
                for (Address a : addresses) a.setDefault(false);
                addresses.get(idx).setDefault(true);
                renderAddresses();
                Toast.makeText(this, address.getName() + " set as default", Toast.LENGTH_SHORT).show();
            });

            // Edit click → open dialog pre-filled
            card.findViewById(R.id.btn_edit).setOnClickListener(v -> {
                showAddressDialog(address, idx);
            });

            // Delete click
            card.findViewById(R.id.btn_delete).setOnClickListener(v -> {
                addresses.remove(idx);
                renderAddresses();
                Toast.makeText(this, address.getName() + " deleted", Toast.LENGTH_SHORT).show();
            });

            addressesContainer.addView(card);
        }
    }
}
