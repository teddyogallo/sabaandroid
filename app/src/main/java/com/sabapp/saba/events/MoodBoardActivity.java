package com.sabapp.saba.events;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.sabapp.saba.R;
import com.sabapp.saba.adapters.MasonryAdapter;
import com.sabapp.saba.adapters.PaletteAdapter;
import com.sabapp.saba.adapters.VendorColorAdapter;
import com.sabapp.saba.application.sabaapp;
import com.sabapp.saba.data.model.ImageItem;
import com.sabapp.saba.data.model.VendorMatch;

public class MoodBoardActivity extends AppCompatActivity implements PaletteAdapter.OnPaletteClickListener,
        VendorColorAdapter.OnVendorActionListener {

    private RecyclerView masonryRecycler, paletteRecycler, vendorRecycler;

    MasonryAdapter masonryAdapter;
    PaletteAdapter paletteAdapter;

    VendorColorAdapter vendorAdapter;
    private List<Integer> extractedPalette = new ArrayList<>();
    private boolean isDropperActive = false;



    // Data Lists (Declare here)
    private List<ImageItem> imageList;
    private List<VendorMatch> vendorList;

    private PermissionListener permissionlistener;

    sabaapp app;

    private String Document_img1="";

    private String encodedImage; // For Base64 storage

    @Override
    public void onColorSelected(int color) {
        showColorSpecs(color); // Show the Dialog
        refreshVendorMatches(color); // Run the math and update the list
    }


    @Override
    public void onRequestSample(VendorMatch vendor) {
        // This logic runs when the "Request Sample" button is clicked
        Toast.makeText(this, "Sample requested from: " + vendor.name, Toast.LENGTH_SHORT).show();

        // Here you would typically trigger an API call to notify the vendor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodboardcontainer);
        app = (sabaapp) getApplicationContext();

        imageList = new ArrayList<>();
        vendorList = new ArrayList<>();
        extractedPalette = new ArrayList<>();

        initViews();
        loadMockData();
        setupMasonry();
        setupVendorList();

        // Automatically extract palette from the first placeholder image if it exists
        if (!imageList.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageList.get(0).imageRes);
            extractSmartPalette(bitmap);
        }


        findViewById(R.id.colorDropper).setOnClickListener(v -> {
            isDropperActive = !isDropperActive;
            Toast.makeText(this, isDropperActive ? "Dropper Active: Tap an image" : "Dropper Off", Toast.LENGTH_SHORT).show();
        });


        // The "Add" button in your XML
        findViewById(R.id.btnUpload).setOnClickListener(v -> {
            checkPermissionsAndSelect();
        });

        setupPermissionListener();

    }

    private void initViews() {
        masonryRecycler = findViewById(R.id.moodBoardMasonry);
        paletteRecycler = findViewById(R.id.paletteBar);
        vendorRecycler = findViewById(R.id.vendorProcurementRecycler);
    }

    private void setupMasonry() {
        // 1. Setup Masonry Grid
        masonryAdapter = new MasonryAdapter(imageList, new MasonryAdapter.OnImageInteractionListener() {
            @Override
            public void onImageTouch(ImageView view, MotionEvent event, ImageItem item) {
                if (isDropperActive) extractColorFromTouch(view, event);
            }

            @Override
            public void onImageLocked(ImageItem item) {
                Toast.makeText(MoodBoardActivity.this, "Design Spec Locked", Toast.LENGTH_SHORT).show();
            }
        });

        // IMPORTANT: Set LayoutManager and Adapter
        masonryRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        masonryRecycler.setAdapter(masonryAdapter);

        // 2. Setup Palette Bar
        paletteAdapter = new PaletteAdapter(extractedPalette, this);
        paletteRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        paletteRecycler.setAdapter(paletteAdapter);
    }


    private void loadMockData() {
        // Fill Mood Board with initial inspirations
        imageList.add(new ImageItem(R.drawable.weddingrose));
        imageList.add(new ImageItem(R.drawable.tableblue));
        imageList.add(new ImageItem(R.drawable.velvetpallet));

        // Fill Vendor List based on event bookings
        vendorList.add(new VendorMatch("Flora & Bloom", "90% Match Sample", "#2196F3", "#228B22"));
        vendorList.add(new VendorMatch("Linens & Drapes", "Exact Match Available", "#1B5E20", "#1B5E20"));
        vendorList.add(new VendorMatch("Gourmet Bites", "Color Matching Custom Cake", "#E91E63", "#FFC0CB"));
    }

    private void extractColorFromTouch(ImageView view, MotionEvent event) {
        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        // Calculate coordinates relative to bitmap scale
        int x = (int) (event.getX() * bitmap.getWidth() / view.getWidth());
        int y = (int) (event.getY() * bitmap.getHeight() / view.getHeight());

        if (x >= 0 && x < bitmap.getWidth() && y >= 0 && y < bitmap.getHeight()) {
            int pixel = bitmap.getPixel(x, y);
            showColorDetailDialog(pixel);
        }
    }

    private void extractSmartPalette(Bitmap bitmap) {
        Palette.from(bitmap).generate(palette -> {
            if (palette != null) {
                extractedPalette.clear();
                // Get a wider variety of colors for a better UI
                extractedPalette.add(palette.getVibrantColor(Color.LTGRAY));
                extractedPalette.add(palette.getDarkVibrantColor(Color.DKGRAY));
                extractedPalette.add(palette.getMutedColor(Color.GRAY));
                extractedPalette.add(palette.getLightMutedColor(Color.WHITE));
                extractedPalette.add(palette.getDominantColor(Color.BLACK));

                paletteAdapter.notifyDataSetChanged(); // Just refresh the existing adapter
            }
        });
    }


    private void showColorDetailDialog(int color) {
        String hex = String.format("#%06X", (0xFFFFFF & color));
        String rgb = "RGB: " + Color.red(color) + ", " + Color.green(color) + ", " + Color.blue(color);

        new AlertDialog.Builder(this)
                .setTitle("Color Captured")
                .setMessage(hex + "\n" + rgb + "\nPantone: 19-4028 TCX")
                .setPositiveButton("Add to Palette", (d, w) -> {
                    extractedPalette.add(0, color);
                    updatePaletteBar();
                })
                .show();
    }

    private void updatePaletteBar() {
        // Pass 'this' as the second argument to satisfy the constructor
        PaletteAdapter adapter = new PaletteAdapter(extractedPalette, this);

        paletteRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        paletteRecycler.setAdapter(adapter);
    }




    private void setupVendorList() {
        // Clear and fill your list (using the class-level vendorList variable we declared earlier)
        vendorList = new ArrayList<>();
        vendorList.add(new VendorMatch("Flora & Bloom", "90% Match Sample", "#2196F3", "#FFC0CB"));
        vendorList.add(new VendorMatch("Linens & Drapes", "Exact Match Available", "#1B5E20", "#FFC0CB"));

        vendorRecycler.setLayoutManager(new LinearLayoutManager(this));

        // PASS 'this' as the second argument here
        vendorAdapter = new VendorColorAdapter(vendorList, this);
        vendorRecycler.setAdapter(vendorAdapter);
    }


    private void setupPermissionListener() {
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                showImageSourceOptions();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MoodBoardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void checkPermissionsAndSelect() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Enable permissions in Settings > Permissions to upload.")
                .setPermissions(permissions)
                .check();
    }


    private void showImageSourceOptions() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Inspiration to Mood Board");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryLauncher.launch(intent);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // 📸 CAMERA LAUNCHER
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    processNewImage(bitmap);
                }
            }
    );


    // 🖼️ GALLERY LAUNCHER
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try (InputStream inputStream = getContentResolver().openInputStream(selectedImage)) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        processNewImage(bitmap);
                    } catch (Exception e) {
                        Log.e("MOOD_UPLOAD", "Error loading gallery image", e);
                    }
                }
            }
    );


    private void processNewImage(Bitmap bitmap) {
        if (bitmap == null) return;

        Bitmap resized = getResizedBitmap(bitmap, 800);

        // 1. Convert to Base64 (using your provided method)
        encodedImage = BitMapToString(resized);

        // 2. Add to Masonry List
        ImageItem newItem = new ImageItem(resized); // Updated model to take Bitmap
        imageList.add(0, newItem); // Add to the top
        masonryAdapter.notifyItemInserted(0);
        masonryRecycler.scrollToPosition(0);

        // 3. Trigger Palette Extraction on the new image
        extractSmartPalette(resized);

        Toast.makeText(this, "Mood Board Updated", Toast.LENGTH_SHORT).show();
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        app.setCreateAdImage(Document_img1);




        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private void showColorSpecs(int color) {
        // 1. Convert to HEX
        // The mask 0xFFFFFF removes the Alpha (transparency) channel for a standard Hex code
        String hexColor = String.format("#%06X", (0xFFFFFF & color));

        // 2. Convert to RGB
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String rgbText = String.format("RGB: (%d, %d, %d)", r, g, b);

        // 3. Approximate Pantone
        // Real Pantone matching usually requires a database;
        // for this UI, we simulate the spec metadata
        String pantoneSpec = "19-4028 TCX (Estimated)";

        // 4. Build and Show the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create a custom view or a simple colored header for the dialog
        View titleView = getLayoutInflater().inflate(R.layout.dialog_color_header, null);
        titleView.setBackgroundColor(color);
        builder.setCustomTitle(titleView);

        builder.setMessage("DESIGN SPECIFICATIONS\n\n" +
                        "HEX: " + hexColor + "\n" +
                        rgbText + "\n" +
                        "PANTONE: " + pantoneSpec)
                .setPositiveButton("Lock Design Spec", (dialog, which) -> {
                    lockCurrentSelection(hexColor);
                    Toast.makeText(this, "Color Locked for Vendors", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Close", null)
                .show();
    }


    private void lockCurrentSelection(String hex) {
        // Save to SharedPreferences so it persists when the app closes
        SharedPreferences pref = getSharedPreferences("DesignSpecs", MODE_PRIVATE);
        pref.edit().putString("locked_event_color", hex).apply();

        // Update the UI to show a locked state if necessary
        // You could also trigger a refresh of the Vendor List here
        //refreshVendorMatches(hex);
    }

    // --- THE MATH LOGIC ---
    public int calculateMatchPercentage(int lockedColor, int vendorColor) {
        int r1 = Color.red(lockedColor);
        int g1 = Color.green(lockedColor);
        int b1 = Color.blue(lockedColor);

        int r2 = Color.red(vendorColor);
        int g2 = Color.green(vendorColor);
        int b2 = Color.blue(vendorColor);

        double distance = Math.sqrt(
                Math.pow(r2 - r1, 2) +
                        Math.pow(g2 - g1, 2) +
                        Math.pow(b2 - b1, 2)
        );

        double maxDistance = 441.67;
        double match = 100 * (1 - (distance / maxDistance));

        return (int) Math.max(0, Math.min(100, match));
    }

    // --- THE UPDATE LOGIC ---
    private void refreshVendorMatches(int selectedColor) {



        for (VendorMatch vendor : vendorList) {
            // Assuming VendorMatch model has an 'inventoryHex' field
            if (vendor.inventoryHex == null || vendor.inventoryHex.isEmpty()) {
                vendor.status = "Not Applicable";
            } else {
                // Run calculateMatchPercentage here
                int vColor = Color.parseColor(vendor.inventoryHex);
                int percent = calculateMatchPercentage(selectedColor, vColor);

                vendor.status = percent + "% Match Found";
                // Update badge color based on threshold
                vendor.badgeColor = (percent > 85) ? "#1B5E20" : "#FBC02D";
            }


        }
        vendorAdapter.notifyDataSetChanged(); // Refresh the Recycler
    }






}